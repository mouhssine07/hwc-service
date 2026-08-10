package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.entity.MarketingSession;
import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.model.MarketingSessionSummary;
import hwc_backend.coach.marketing.repository.MarketingSessionMessageRepository;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import hwc_backend.entity.User;
import hwc_backend.repository.UserRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.entity.DiagnosticStatut;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingSessionServiceImpl implements MarketingSessionService {

    private static final Logger log = LoggerFactory.getLogger(MarketingSessionServiceImpl.class);
    private static final String SERVICE_ID = "MARKETING_STRATEGY";

    private final MarketingSessionRepository sessionRepository;
    private final MarketingSessionMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public MarketingSessionState createSession(String email) {
        User user = findUser(email);
        String sessionId = UUID.randomUUID().toString();
        MarketingSessionState state = initialState(sessionId, user);

        MarketingSession session = new MarketingSession();
        session.setId(sessionId);
        session.setUser(user);
        session.setServiceId(SERVICE_ID);
        diagnosticRepository.findByUserIdAndStatutOrderByDateDebutDesc(user.getId(), DiagnosticStatut.TERMINE)
                .stream().findFirst().ifPresent(diagnostic -> session.setDiagnosticId(diagnostic.getId()));
        session.setStage(state.getStage());
        session.setStateJson(writeState(state));
        sessionRepository.save(session);

        log.info("Marketing session created sessionId={} stage={} userId={}", sessionId, state.getStage(), user.getId());
        return state;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketingSessionSummary> listSessions(String email) {
        return sessionRepository.findByUserEmailOrderByUpdatedAtDesc(email).stream()
                .map(session -> {
                    MarketingSessionState state = readState(session);
                    String offer = state.getCompany().getProductsOrServices().stream()
                            .filter(value -> value != null && !value.isBlank()).findFirst()
                            .orElse(state.getCompany().getSector());
                    String company = session.getUser().getEntreprise();
                    String subject = offer == null || offer.isBlank() ? "Stratégie Marketing" : offer;
                    String title = company == null || company.isBlank() ? subject : subject + " — " + company;
                    return new MarketingSessionSummary(session.getId(), title, session.getServiceId(),
                            session.getDiagnosticId(), session.getStage(), session.isCompleted(), session.getUpdatedAt());
                })
                .toList();
    }

    @Override
    @Transactional
    public void deleteSession(String sessionId, String email) {
        MarketingSession session = findOwnedSession(sessionId, email);
        messageRepository.deleteBySessionId(sessionId);
        sessionRepository.delete(session);
        log.info("Marketing session deleted sessionId={} userId={}", sessionId, session.getUser().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public MarketingSessionState getState(String sessionId, String email) {
        return readState(findOwnedSession(sessionId, email));
    }

    @Override
    @Transactional
    public MarketingSessionState saveState(String sessionId, String email, MarketingSessionState state) {
        MarketingSession session = findOwnedSession(sessionId, email);
        validateStateIdentity(sessionId, state);

        MarketingStrategyStage previousStage = session.getStage();
        session.setStage(state.getStage());
        session.setCompleted(state.isCompleted());
        session.setStateJson(writeState(state));
        sessionRepository.save(session);

        log.info("Marketing session updated sessionId={} stage={} previousStage={} confidence={} missingInformationCount={}",
                sessionId, state.getStage(), previousStage, state.getConfidence(), state.getMissingInformation().size());
        return state;
    }

    @Override
    @Transactional
    public void appendMessage(
            String sessionId,
            String email,
            MarketingSessionMessage.Role role,
            String content,
            String ragContextJson
    ) {
        appendMessage(sessionId, email, role, content, ragContextJson, null, null);
    }

    @Override
    @Transactional
    public void appendMessage(String sessionId, String email, MarketingSessionMessage.Role role,
                              String content, String ragContextJson, String imageDataUrl, String imageName) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Le contenu du message est obligatoire");
        }
        MarketingSession session = findOwnedSession(sessionId, email);
        MarketingSessionMessage message = new MarketingSessionMessage();
        message.setSession(session);
        message.setRole(role);
        message.setContent(content.trim());
        message.setActiveStage(session.getStage().name());
        message.setRagContextJson(ragContextJson);
        message.setImageDataUrl(imageDataUrl);
        message.setImageName(imageName);
        messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketingSessionMessage> getMessages(String sessionId, String email) {
        findOwnedSession(sessionId, email);
        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Override
    @Transactional
    public void saveDeliverable(String sessionId, String email, String deliverableJson, String deliverableMarkdown) {
        MarketingSession session = findOwnedSession(sessionId, email);
        if (deliverableJson == null || deliverableJson.isBlank() || deliverableMarkdown == null || deliverableMarkdown.isBlank()) {
            throw new IllegalArgumentException("Le livrable Marketing est incomplet");
        }
        session.setDeliverableJson(deliverableJson);
        session.setDeliverableMarkdown(deliverableMarkdown);
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public String getDeliverableMarkdown(String sessionId, String email) {
        MarketingSession session = findOwnedSession(sessionId, email);
        if (session.getDeliverableMarkdown() == null || session.getDeliverableMarkdown().isBlank()) {
            throw new EntityNotFoundException("Livrable Marketing indisponible");
        }
        return session.getDeliverableMarkdown();
    }

    private MarketingSessionState initialState(String sessionId, User user) {
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId(sessionId);
        state.setServiceId(SERVICE_ID);
        state.setStage(MarketingStrategyStage.COMPANY_DISCOVERY);
        state.getCompany().setSector(user.getSecteur());
        state.getCompany().setSize(user.getTailleEntreprise());
        state.setMissingInformation(new ArrayList<>(List.of(
                "company.sector",
                "company.productsOrServices",
                "company.businessModel",
                "company.location",
                "company.size"
        )));
        if (user.getSecteur() != null && !user.getSecteur().isBlank()) {
            state.getMissingInformation().remove("company.sector");
        }
        if (user.getTailleEntreprise() != null && !user.getTailleEntreprise().isBlank()) {
            state.getMissingInformation().remove("company.size");
        }
        return state;
    }

    private void validateStateIdentity(String sessionId, MarketingSessionState state) {
        if (state == null || !SERVICE_ID.equals(state.getServiceId()) || !sessionId.equals(state.getSessionId())) {
            throw new IllegalArgumentException("L'état ne correspond pas à la session Marketing demandée");
        }
        if (state.getStage() == null || state.getConfidence() == null || state.getMissingInformation() == null) {
            throw new IllegalArgumentException("L'état de session est incomplet");
        }
        if (state.isCompleted() != (state.getStage() == MarketingStrategyStage.COMPLETED)) {
            throw new IllegalArgumentException("Le statut completed doit correspondre à l'étape COMPLETED");
        }
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));
    }

    private MarketingSession findOwnedSession(String sessionId, String email) {
        return sessionRepository.findByIdAndUserEmail(sessionId, email)
                .orElseThrow(() -> new EntityNotFoundException("Session Marketing introuvable"));
    }

    private String writeState(MarketingSessionState state) {
        try {
            return objectMapper.writeValueAsString(state);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Impossible de sérialiser l'état Marketing", exception);
        }
    }

    private MarketingSessionState readState(MarketingSession session) {
        try {
            return objectMapper.readValue(session.getStateJson(), MarketingSessionState.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("État Marketing persisté invalide", exception);
        }
    }
}
