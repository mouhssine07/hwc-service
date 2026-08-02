package hwc_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.dto.chat.ChatConversationDTO;
import hwc_backend.dto.chat.ChatMessageDTO;
import hwc_backend.dto.chat.ChatMessageRequestDTO;
import hwc_backend.dto.chat.ChatMessageResponseDTO;
import hwc_backend.entity.ChatConversation;
import hwc_backend.entity.ChatMessage;
import hwc_backend.entity.CoachObjectifResultat;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import hwc_backend.entity.Recommandation;
import hwc_backend.entity.Score;
import hwc_backend.entity.User;
import hwc_backend.repository.ChatConversationRepository;
import hwc_backend.repository.ChatMessageRepository;
import hwc_backend.repository.CoachObjectifResultatRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.RecommandationRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final ScoreRepository scoreRepository;
    private final RecommandationRepository recommandationRepository;
    private final ChatConversationRepository chatConversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CoachObjectifResultatRepository coachObjectifResultatRepository;

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model:llama3.2:latest}")
    private String ollamaModel;

    @Value("${chat.memory.max-messages:12}")
    private int maxMemoryMessages;

    @Override
    @Transactional
    public ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, String email) {
        User user = findUser(email);
        ChatConversation conversation = resolveConversation(request, user);

        ChatMessage userMessage = new ChatMessage();
        userMessage.setConversation(conversation);
        userMessage.setRole("user");
        userMessage.setContenu(request.getMessage().trim());
        chatMessageRepository.save(userMessage);

        List<ChatMessage> history = chatMessageRepository.findByConversationIdOrderByDateEnvoiAsc(conversation.getId());
        CoachObjectifResultat coachObjectif = resolveOwnedCoachObjectif(request.getCoachObjectifId(), user);
        String answer = generateAnswer(user, conversation, history, coachObjectif);

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setConversation(conversation);
        assistantMessage.setRole("assistant");
        assistantMessage.setContenu(answer);
        ChatMessage savedAssistantMessage = chatMessageRepository.save(assistantMessage);

        return new ChatMessageResponseDTO(
                conversation.getId(),
                savedAssistantMessage.getRole(),
                savedAssistantMessage.getContenu(),
                savedAssistantMessage.getDateEnvoi()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatConversationDTO> getConversations(String email) {
        User user = findUser(email);
        return chatConversationRepository.findByUserIdOrderByDateCreationDesc(user.getId()).stream()
                .map(this::toConversationDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long conversationId, String email) {
        ChatConversation conversation = findOwnedConversation(conversationId, email);
        return chatMessageRepository.findByConversationIdOrderByDateEnvoiAsc(conversation.getId()).stream()
                .map(this::toMessageDTO)
                .toList();
    }

    private ChatConversation resolveConversation(ChatMessageRequestDTO request, User user) {
        if (request.getConversationId() != null) {
            ChatConversation conversation = chatConversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));
            if (!conversation.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Conversation does not belong to authenticated user");
            }
            return conversation;
        }

        Diagnostic diagnostic = null;
        Long diagnosticId = request.getDiagnosticId();
        if (diagnosticId != null) {
            diagnostic = findOwnedDiagnostic(diagnosticId, user);
            if (diagnostic.getStatut() != DiagnosticStatut.TERMINE) {
                throw new IllegalStateException("Le diagnostic doit etre finalise pour alimenter le contexte du chatbot.");
            }
        } else {
            diagnostic = findLatestFinishedDiagnostic(user);
            diagnosticId = diagnostic == null ? null : diagnostic.getId();
        }

        ChatConversation conversation = new ChatConversation();
        conversation.setUser(user);
        conversation.setDiagnosticId(diagnosticId);
        conversation.setTitre(buildConversationTitle(request.getMessage()));
        return chatConversationRepository.save(conversation);
    }

    private String generateAnswer(User user, ChatConversation conversation, List<ChatMessage> history, CoachObjectifResultat coachObjectif) {
        String systemPrompt = buildSystemPrompt(user, conversation.getDiagnosticId(), coachObjectif);
        try {
            return callOllama(systemPrompt, history);
        } catch (RuntimeException exception) {
            return fallbackAnswer(conversation.getDiagnosticId());
        }
    }

    private String callOllama(String systemPrompt, List<ChatMessage> history) {
        RestClient restClient = RestClient.builder().baseUrl(ollamaBaseUrl).build();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        history.stream()
                .skip(Math.max(0, history.size() - Math.max(2, maxMemoryMessages)))
                .forEach(message -> messages.add(Map.of(
                        "role", message.getRole(),
                        "content", message.getContenu()
                )));

        Map<String, Object> request = Map.of(
                "model", ollamaModel,
                "stream", false,
                "messages", messages,
                "options", Map.of(
                        "temperature", 0.3,
                        "num_predict", 700
                )
        );

        String responseBody = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("Ollama response is empty");
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("message").path("content").asText();
            if (content == null || content.isBlank()) {
                throw new IllegalStateException("Ollama response content not found");
            }
            return content.trim();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse Ollama response", exception);
        }
    }

    private String buildSystemPrompt(User user, Long diagnosticId, CoachObjectifResultat coachObjectif) {
        return """
                Tu es l'assistant consultant de Harmony Works Consulting pour UN SEUL client connecte.

                REGLES STRICTES DE SECURITE ET D'EXACTITUDE
                - Reponds uniquement avec les donnees du CONTEXTE CLIENT et du CONTEXTE DIAGNOSTIC ci-dessous.
                - Ne melange jamais les informations d'un autre client, d'un autre diagnostic ou d'un exemple.
                - Quand tu cites un score, recopie exactement le score present dans le contexte.
                - N'invente jamais un score, une categorie, une entreprise, un nom, une recommandation ou une priorite.
                - Si une donnee n'est pas dans le contexte, dis clairement: "Je n'ai pas cette donnee dans votre diagnostic actuel."
                - Pour "le score le plus faible", utilise uniquement la ligne "Categorie la plus faible" du contexte.
                - Si plusieurs interpretations sont possibles, pose 1 ou 2 questions courtes au client avant de conclure.
                - Ne demande jamais de secrets, mots de passe, cles API ou donnees sensibles.

                STYLE DE REPONSE
                - Ta reponse doit etre moderne, courte, lisible et orientee decision.
                - Il est INTERDIT de repondre en un seul paragraphe.
                - Utilise exactement le format ci-dessous pour toute question d'analyse.
                - Garde des phrases courtes: 1 idee par phrase.
                - Ne mets pas d'introduction longue.

                FORMAT OBLIGATOIRE
                ## Synthese
                Une phrase directe avec le point principal.

                ## Ce que je vois dans votre diagnostic
                - 2 a 3 constats chiffres, uniquement depuis le contexte.
                - Chaque puce doit etre courte.

                ## Pourquoi c'est important
                - 2 a 3 impacts concrets pour l'entreprise du client.

                ## Actions prioritaires
                1. Une action simple a faire maintenant.
                2. Une action a planifier.
                3. Une action a suivre avec un indicateur.

                ## Question pour mieux vous guider
                Une seule question courte et precise.

                CONTRAINTES DE LONGUEUR
                - Maximum 220 mots sauf si le client demande un detail long.
                - Maximum 5 sections.
                - Maximum 3 puces ou actions par section.
                - Pas de formule vague comme "il est important de noter" si elle n'apporte rien.

                CONTEXTE CLIENT
                %s

                CONTEXTE DIAGNOSTIC
                %s

                OBJECTIF COACH ACTUEL
                %s

                Pour cet echange, concentre-toi exclusivement sur cet objectif. Guide le client pas a pas, pose une seule question a la fois et propose des livrables concrets. Ne declare jamais une action terminee sans confirmation explicite du client.
                """.formatted(buildUserContext(user), buildDiagnosticContext(user, diagnosticId), buildCoachObjectifContext(coachObjectif));
    }

    private String buildCoachObjectifContext(CoachObjectifResultat objectif) {
        if (objectif == null) return "Aucun objectif Coach specifique.";
        return "- Titre: %s\n- Description: %s\n- Indicateur: %s\n- Progression declaree: %s/%s %s\n- Commentaire client: %s"
                .formatted(valueOrDash(objectif.getTitre()), valueOrDash(objectif.getDescription()),
                        valueOrDash(objectif.getIndicateur()), objectif.getQuantiteRealisee(), objectif.getQuantiteCible(),
                        valueOrDash(objectif.getUnite()), valueOrDash(objectif.getCommentaireClient()));
    }

    private CoachObjectifResultat resolveOwnedCoachObjectif(Long objectifId, User user) {
        if (objectifId == null) return null;
        CoachObjectifResultat objectif = coachObjectifResultatRepository.findById(objectifId)
                .orElseThrow(() -> new EntityNotFoundException("Coach objective not found"));
        if (!objectif.getObjectifsHebdo().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Coach objective does not belong to authenticated user");
        }
        return objectif;
    }

    private String buildUserContext(User user) {
        return "- Nom: %s %s\n- Entreprise: %s\n- Secteur: %s\n- Taille: %s"
                .formatted(
                        valueOrDash(user.getPrenom()),
                        valueOrDash(user.getNom()),
                        valueOrDash(user.getEntreprise()),
                        valueOrDash(user.getSecteur()),
                        valueOrDash(user.getTailleEntreprise())
                );
    }

    private String buildDiagnosticContext(User user, Long diagnosticId) {
        Diagnostic diagnostic = diagnosticId == null
                ? findLatestFinishedDiagnostic(user)
                : findOwnedDiagnostic(diagnosticId, user);

        if (diagnostic == null || diagnostic.getStatut() != DiagnosticStatut.TERMINE) {
            return "Aucun diagnostic finalise disponible pour ce client.";
        }

        List<Score> scores = scoreRepository.findByDiagnosticId(diagnostic.getId());
        Score weakestScore = scores.stream()
                .filter(score -> score.getScore() != null)
                .min(Comparator.comparing(Score::getScore))
                .orElse(null);

        StringBuilder builder = new StringBuilder();
        builder.append("- Diagnostic #").append(diagnostic.getId()).append("\n")
                .append("- Date: ").append(diagnostic.getDateFin() == null ? "-" : diagnostic.getDateFin().format(DATE_FORMATTER)).append("\n")
                .append("- Score global: ").append(diagnostic.getScoreGlobal()).append("/100\n")
                .append("- Niveau: ").append(valueOrDash(diagnostic.getNiveauMaturite())).append("\n");

        if (weakestScore == null) {
            builder.append("- Categorie la plus faible: non disponible\n");
        } else {
            builder.append("- Categorie la plus faible: ")
                    .append(weakestScore.getCategorie().getNom())
                    .append(" avec ")
                    .append(weakestScore.getScore())
                    .append("/100\n");
        }

        builder
                .append("- Scores par categorie:\n");

        for (Score score : scores) {
            builder.append("  - ")
                    .append(score.getCategorie().getNom())
                    .append(": ").append(score.getScore()).append("/100\n");
        }

        List<Recommandation> recommandations = recommandationRepository.findByDiagnosticIdOrderByPrioriteAsc(diagnostic.getId());
        if (recommandations.isEmpty()) {
            builder.append("- Recommandations: aucune recommandation prioritaire generee.\n");
        } else {
            builder.append("- Recommandations prioritaires:\n");
            for (Recommandation recommandation : recommandations) {
                builder.append("  - P").append(recommandation.getPriorite())
                        .append(" | ").append(recommandation.getTitre())
                        .append(" | Horizon: ").append(recommandation.getHorizon())
                        .append(" | Impact: ").append(valueOrDash(recommandation.getImpactEstime()))
                        .append("\n");
            }
        }
        return builder.toString();
    }

    private Diagnostic findLatestFinishedDiagnostic(User user) {
        return diagnosticRepository.findByUserIdAndStatutOrderByDateDebutDesc(user.getId(), DiagnosticStatut.TERMINE).stream()
                .max(Comparator.comparing(Diagnostic::getDateDebut))
                .orElse(null);
    }

    private Diagnostic findOwnedDiagnostic(Long diagnosticId, User user) {
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));
        if (!diagnostic.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Diagnostic does not belong to authenticated user");
        }
        return diagnostic;
    }

    private ChatConversation findOwnedConversation(Long conversationId, String email) {
        User user = findUser(email);
        ChatConversation conversation = chatConversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));
        if (!conversation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Conversation does not belong to authenticated user");
        }
        return conversation;
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private ChatConversationDTO toConversationDTO(ChatConversation conversation) {
        return new ChatConversationDTO(
                conversation.getId(),
                conversation.getDiagnosticId(),
                conversation.getTitre(),
                conversation.getDateCreation()
        );
    }

    private ChatMessageDTO toMessageDTO(ChatMessage message) {
        return new ChatMessageDTO(
                message.getId(),
                message.getConversation().getId(),
                message.getRole(),
                message.getContenu(),
                message.getDateEnvoi()
        );
    }

    private String buildConversationTitle(String message) {
        String normalized = message == null ? "Conversation HWC" : message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 60) {
            return normalized;
        }
        return normalized.substring(0, 57) + "...";
    }

    private String fallbackAnswer(Long diagnosticId) {
        if (diagnosticId == null) {
            return "Je peux vous aider a lire votre diagnostic HWC, vos scores et vos recommandations. Pour une analyse plus precise, lancez ou ouvrez un diagnostic finalise.";
        }
        return "Ollama local ne repond pas pour le moment. Votre question est bien enregistree; relancez Ollama puis reessayez pour obtenir une analyse IA basee sur ce diagnostic.";
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
