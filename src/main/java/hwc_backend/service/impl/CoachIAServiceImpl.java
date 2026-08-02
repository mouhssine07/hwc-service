package hwc_backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.dto.coach.CoachObjectifDTO;
import hwc_backend.dto.coach.CoachObjectifProgressionDTO;
import hwc_backend.dto.coach.CoachSemaineDTO;
import hwc_backend.entity.CoachEmailEnvoye;
import hwc_backend.entity.CoachObjectifResultat;
import hwc_backend.entity.CoachObjectifsHebdo;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import hwc_backend.entity.Recommandation;
import hwc_backend.entity.User;
import hwc_backend.repository.CoachEmailEnvoyeRepository;
import hwc_backend.repository.CoachObjectifResultatRepository;
import hwc_backend.repository.CoachObjectifsHebdoRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.RecommandationRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.CoachIAService;
import hwc_backend.service.CoachNotificationService;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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
public class CoachIAServiceImpl implements CoachIAService {

    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final RecommandationRepository recommandationRepository;
    private final CoachObjectifsHebdoRepository objectifsHebdoRepository;
    private final CoachObjectifResultatRepository objectifResultatRepository;
    private final CoachEmailEnvoyeRepository emailEnvoyeRepository;
    private final CoachNotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model:llama3.2:latest}")
    private String ollamaModel;

    @Override
    @Transactional
    public CoachSemaineDTO getCurrentWeek(String email, Long diagnosticId) {
        User user = findUser(email);
        Diagnostic diagnostic = diagnosticId == null ? latestFinishedDiagnostic(user) : findOwnedFinishedDiagnostic(user, diagnosticId);
        if (diagnostic == null) {
            return noDiagnosticResponse();
        }
        LocalDate monday = currentMonday();
        CoachObjectifsHebdo week = objectifsHebdoRepository.findFirstByUserIdAndDiagnosticIdAndSemaineDebutOrderByIdAsc(user.getId(), diagnostic.getId(), monday)
                .orElseGet(() -> createWeek(user, diagnostic, monday));
        return toDto(week);
    }

    @Override
    @Transactional
    public CoachSemaineDTO completeObjective(Long objectiveId, String email) {
        User user = findUser(email);
        CoachObjectifResultat objective = findOwnedObjective(objectiveId, user);
        CoachObjectifsHebdo week = objective.getObjectifsHebdo();
        if (!objective.isTermine()) {
            objective.setTermine(true);
            objective.setQuantiteRealisee(objective.getQuantiteCible() == null ? 1 : objective.getQuantiteCible());
            objective.setDateCompletion(LocalDateTime.now());
            objective.setDateMiseAJour(LocalDateTime.now());
            objectifResultatRepository.save(objective);
        }
        return toDto(week);
    }

    @Override
    @Transactional
    public CoachSemaineDTO updateObjectiveProgress(Long objectiveId, CoachObjectifProgressionDTO progression, String email) {
        User user = findUser(email);
        CoachObjectifResultat objective = findOwnedObjective(objectiveId, user);
        int quantity = progression.quantiteRealisee() == null ? 0 : progression.quantiteRealisee();
        objective.setQuantiteRealisee(quantity);
        objective.setCommentaireClient(progression.commentaire() == null ? null : progression.commentaire().trim());
        objective.setDateMiseAJour(LocalDateTime.now());
        boolean completed = objective.getQuantiteCible() != null && quantity >= objective.getQuantiteCible();
        objective.setTermine(completed);
        objective.setDateCompletion(completed ? LocalDateTime.now() : null);
        objectifResultatRepository.save(objective);
        return toDto(objective.getObjectifsHebdo());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoachSemaineDTO> getHistory(String email, Long diagnosticId) {
        User user = findUser(email);
        List<CoachObjectifsHebdo> weeks = diagnosticId == null
                ? objectifsHebdoRepository.findByUserIdOrderBySemaineDebutDesc(user.getId())
                : objectifsHebdoRepository.findByUserIdAndDiagnosticIdOrderBySemaineDebutDesc(user.getId(), diagnosticId);
        return weeks.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void generateWeeklyObjectivesForActiveClients() {
        LocalDate monday = currentMonday();
        userRepository.findByActifTrue().forEach(user -> {
            Diagnostic diagnostic = latestFinishedDiagnostic(user);
            if (diagnostic != null && objectifsHebdoRepository.findFirstByUserIdAndDiagnosticIdAndSemaineDebutOrderByIdAsc(user.getId(), diagnostic.getId(), monday).isEmpty()) {
                CoachObjectifsHebdo week = createWeek(user, diagnostic, monday);
                if (week != null) {
                    sendMondayEmail(user, week);
                }
            }
        });
    }

    @Override
    @Transactional
    public void prepareWeeklySummaries() {
        LocalDate monday = currentMonday();
        objectifsHebdoRepository.findAll().stream()
                .filter(week -> monday.equals(week.getSemaineDebut()))
                .forEach(this::prepareAndSendFridaySummary);
    }

    private CoachObjectifsHebdo createWeek(User user, Diagnostic diagnostic, LocalDate monday) {
        List<CoachObjectifsHebdo> existingWeeks =
                objectifsHebdoRepository.findByUserIdAndDiagnosticIdOrderBySemaineDebutDesc(user.getId(), diagnostic.getId());
        if (existingWeeks.size() >= 12) {
            return existingWeeks.get(0);
        }
        CoachObjectifsHebdo week = new CoachObjectifsHebdo();
        week.setUser(user);
        week.setDiagnostic(diagnostic);
        week.setSemaineDebut(monday);
        week.setSemaineFin(monday.plusDays(6));
        int weekNumber = existingWeeks.size() + 1;
        week.setNumeroSemaine(Math.min(weekNumber, 12));
        week.setDureeProgrammeSemaines(12);

        List<ObjectiveDraft> drafts = generateObjectives(user, diagnostic);
        week.setConseilSemaine("Concentrez-vous sur ces trois actions avant d'ajouter de nouveaux chantiers.");
        CoachObjectifsHebdo savedWeek = objectifsHebdoRepository.save(week);
        for (int index = 0; index < drafts.size(); index++) {
            ObjectiveDraft draft = drafts.get(index);
            CoachObjectifResultat result = new CoachObjectifResultat();
            result.setObjectifsHebdo(savedWeek);
            result.setOrdre(index + 1);
            result.setTitre(draft.titre());
            result.setDescription(draft.description());
            result.setIndicateur(draft.indicateur());
            result.setQuantiteCible(draft.quantiteCible() == null || draft.quantiteCible() < 1 ? 1 : draft.quantiteCible());
            result.setQuantiteRealisee(0);
            result.setUnite(safeUnit(draft.unite()));
            objectifResultatRepository.save(result);
        }
        return savedWeek;
    }

    private List<ObjectiveDraft> generateObjectives(User user, Diagnostic diagnostic) {
        List<Recommandation> recommendations = recommandationRepository.findByDiagnosticIdOrderByPrioriteAsc(diagnostic.getId());
        List<ObjectiveDraft> carryOver = previousIncompleteObjectives(user, diagnostic);
        try {
            List<ObjectiveDraft> generated = callOllama(user, diagnostic, recommendations);
            List<ObjectiveDraft> combined = combineObjectives(carryOver, generated);
            if (combined.size() >= 3) {
                return combined.subList(0, 3);
            }
        } catch (RuntimeException ignored) {
            // The deterministic plan below keeps the Coach available without Ollama.
        }
        return combineObjectives(carryOver, fallbackObjectives(recommendations)).stream().limit(3).toList();
    }

    private List<ObjectiveDraft> previousIncompleteObjectives(User user, Diagnostic diagnostic) {
        return objectifsHebdoRepository.findByUserIdAndDiagnosticIdOrderBySemaineDebutDesc(user.getId(), diagnostic.getId())
                .stream()
                .findFirst()
                .map(previous -> objectifResultatRepository.findByObjectifsHebdoIdOrderByOrdreAsc(previous.getId()).stream()
                        .filter(objective -> !objective.isTermine())
                        .map(objective -> new ObjectiveDraft(
                                objective.getTitre(),
                                "Objectif reporte : " + safe(objective.getDescription()),
                                objective.getIndicateur(),
                                objective.getQuantiteCible(),
                                objective.getUnite()
                        ))
                        .toList())
                .orElse(List.of());
    }

    private List<ObjectiveDraft> combineObjectives(List<ObjectiveDraft> priority, List<ObjectiveDraft> generated) {
        List<ObjectiveDraft> combined = new ArrayList<>(priority);
        generated.stream()
                .filter(candidate -> combined.stream().noneMatch(existing -> existing.titre().equalsIgnoreCase(candidate.titre())))
                .forEach(combined::add);
        return combined;
    }

    private List<ObjectiveDraft> callOllama(User user, Diagnostic diagnostic, List<Recommandation> recommendations) {
        String recommendationsText = recommendations.stream()
                .limit(5)
                .map(recommendation -> "- " + recommendation.getTitre() + ": " + safe(recommendation.getDescription()))
                .reduce("", (left, right) -> left + "\n" + right);
        String prompt = """
                Tu es le Coach IA de Harmony Works Consulting. Cree exactement 3 objectifs hebdomadaires concrets pour ce client.
                Reponds uniquement avec un tableau JSON valide. Chaque objet doit contenir titre, description, indicateur,
                quantiteCible (nombre entier positif) et unite (par exemple action, publication, rendez-vous).
                Les actions doivent etre mesurables, realisables en une semaine et fondees uniquement sur les recommandations fournies.
                Client: %s %s, entreprise: %s. Score global: %s/100.
                Recommandations:%s
                """.formatted(user.getPrenom(), user.getNom(), safe(user.getEntreprise()), diagnostic.getScoreGlobal(), recommendationsText);

        RestClient restClient = RestClient.builder().baseUrl(ollamaBaseUrl).build();
        Map<String, Object> request = Map.of(
                "model", ollamaModel,
                "stream", false,
                "format", "json",
                "messages", List.of(
                        Map.of("role", "system", "content", "Tu reponds uniquement en JSON valide."),
                        Map.of("role", "user", "content", prompt)
                ),
                "options", Map.of("temperature", 0.2, "num_predict", 550)
        );
        String response = restClient.post().uri("/api/chat").contentType(MediaType.APPLICATION_JSON).body(request)
                .retrieve().body(String.class);
        try {
            String content = objectMapper.readTree(response).path("message").path("content").asText();
            if (content.isBlank()) {
                throw new IllegalStateException("Reponse Coach IA vide");
            }
            return objectMapper.readValue(content, new TypeReference<List<ObjectiveDraft>>() { });
        } catch (Exception exception) {
            throw new IllegalStateException("Reponse Coach IA invalide", exception);
        }
    }

    private List<ObjectiveDraft> fallbackObjectives(List<Recommandation> recommendations) {
        List<ObjectiveDraft> drafts = new ArrayList<>();
        for (Recommandation recommendation : recommendations.stream().limit(3).toList()) {
            drafts.add(new ObjectiveDraft(
                    recommendation.getTitre(),
                    safe(recommendation.getDescription()),
                    "Valider une action concrete liee a cette recommandation",
                    1,
                    "action"
            ));
        }
        while (drafts.size() < 3) {
            int number = drafts.size() + 1;
            drafts.add(new ObjectiveDraft(
                    "Priorite de la semaine " + number,
                    "Choisissez une action mesurable issue de votre diagnostic et planifiez-la avec votre equipe.",
                    "Action planifiee et responsable identifie",
                    1,
                    "action"
            ));
        }
        return drafts;
    }

    private CoachSemaineDTO toDto(CoachObjectifsHebdo week) {
        List<CoachObjectifDTO> objectives = objectifResultatRepository.findByObjectifsHebdoIdOrderByOrdreAsc(week.getId()).stream()
                .map(item -> new CoachObjectifDTO(
                        item.getId(),
                        item.getOrdre(),
                        item.getTitre(),
                        item.getDescription(),
                        item.getIndicateur(),
                        item.getQuantiteCible(),
                        item.getQuantiteRealisee(),
                        item.getUnite(),
                        item.getCommentaireClient(),
                        item.isTermine(),
                        item.getDateCompletion(),
                        item.getDateMiseAJour()
                ))
                .toList();
        int completed = (int) objectives.stream().filter(CoachObjectifDTO::termine).count();
        int weekNumber = week.getNumeroSemaine() == null || week.getNumeroSemaine() < 1 ? 1 : week.getNumeroSemaine();
        int programmeDuration = week.getDureeProgrammeSemaines() == null || week.getDureeProgrammeSemaines() < 1
                ? 12
                : week.getDureeProgrammeSemaines();
        int programmeProgress = Math.min(100, Math.round(
                ((weekNumber - 1) + (objectives.isEmpty() ? 0f : (float) completed / objectives.size()))
                        / programmeDuration * 100
        ));
        return new CoachSemaineDTO(
                true,
                null,
                week.getId(),
                week.getDiagnostic().getId(),
                week.getSemaineDebut(),
                week.getSemaineFin(),
                week.getConseilSemaine(),
                weekNumber,
                programmeDuration,
                programmeProgress,
                week.getBilanSemaine(),
                week.getInsightIa(),
                completed,
                objectives.size(),
                objectives
        );
    }

    private CoachSemaineDTO noDiagnosticResponse() {
        return new CoachSemaineDTO(false, "Finalisez un diagnostic pour obtenir vos objectifs personnalises.", null, null, null, null, null, 0, 12, 0, null, null, 0, 0, List.of());
    }

    private Diagnostic latestFinishedDiagnostic(User user) {
        return diagnosticRepository.findByUserIdAndStatutOrderByDateDebutDesc(user.getId(), DiagnosticStatut.TERMINE).stream()
                .max(Comparator.comparing(Diagnostic::getDateDebut))
                .orElse(null);
    }

    private Diagnostic findOwnedFinishedDiagnostic(User user, Long diagnosticId) {
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic introuvable"));
        if (!diagnostic.getUser().getId().equals(user.getId()) || diagnostic.getStatut() != DiagnosticStatut.TERMINE) {
            throw new IllegalArgumentException("Ce diagnostic finalise n'appartient pas au client connecte");
        }
        return diagnostic;
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Client introuvable"));
    }

    private CoachObjectifResultat findOwnedObjective(Long objectiveId, User user) {
        CoachObjectifResultat objective = objectifResultatRepository.findById(objectiveId)
                .orElseThrow(() -> new EntityNotFoundException("Objectif Coach introuvable"));
        if (!objective.getObjectifsHebdo().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cet objectif n'appartient pas au client connecte");
        }
        return objective;
    }

    private void saveEmailEvent(User user, CoachObjectifsHebdo week, String type, String status) {
        CoachEmailEnvoye event = new CoachEmailEnvoye();
        event.setUser(user);
        event.setObjectifsHebdo(week);
        event.setType(type);
        event.setStatut(status);
        emailEnvoyeRepository.save(event);
    }

    private void sendMondayEmail(User user, CoachObjectifsHebdo week) {
        if (emailEnvoyeRepository.existsByObjectifsHebdoIdAndTypeAndStatut(week.getId(), "OBJECTIFS", "ENVOYE")) {
            return;
        }
        if (!notificationService.isEnabled()) {
            saveEmailEvent(user, week, "OBJECTIFS", "PREPARE");
            return;
        }
        try {
            notificationService.sendMondayObjectives(
                    user,
                    week,
                    objectifResultatRepository.findByObjectifsHebdoIdOrderByOrdreAsc(week.getId())
            );
            saveEmailEvent(user, week, "OBJECTIFS", "ENVOYE");
        } catch (RuntimeException exception) {
            saveFailedEmailEvent(user, week, "OBJECTIFS", exception);
        }
    }

    private void prepareAndSendFridaySummary(CoachObjectifsHebdo week) {
        List<CoachObjectifResultat> objectives =
                objectifResultatRepository.findByObjectifsHebdoIdOrderByOrdreAsc(week.getId());
        int completed = (int) objectives.stream().filter(CoachObjectifResultat::isTermine).count();
        week.setBilanSemaine(completed + " objectif(s) termine(s) sur " + objectives.size() + ".");
        week.setInsightIa(buildTruthfulInsight(objectives));
        week.setDateBilan(LocalDateTime.now());
        objectifsHebdoRepository.save(week);

        if (emailEnvoyeRepository.existsByObjectifsHebdoIdAndTypeAndStatut(week.getId(), "BILAN", "ENVOYE")) {
            return;
        }
        if (!notificationService.isEnabled()) {
            saveEmailEvent(week.getUser(), week, "BILAN", "PREPARE");
            return;
        }
        try {
            notificationService.sendFridaySummary(week.getUser(), week, objectives);
            saveEmailEvent(week.getUser(), week, "BILAN", "ENVOYE");
        } catch (RuntimeException exception) {
            saveFailedEmailEvent(week.getUser(), week, "BILAN", exception);
        }
    }

    private String buildTruthfulInsight(List<CoachObjectifResultat> objectives) {
        long completed = objectives.stream().filter(CoachObjectifResultat::isTermine).count();
        long started = objectives.stream()
                .filter(objective -> !objective.isTermine() && objective.getQuantiteRealisee() != null && objective.getQuantiteRealisee() > 0)
                .count();
        if (completed == objectives.size() && !objectives.isEmpty()) {
            return "Tous les objectifs declares ont ete realises. La semaine suivante pourra augmenter progressivement le niveau d'ambition.";
        }
        if (completed > 0 || started > 0) {
            return "Des progres reels ont ete enregistres. Les actions inachevees seront conservees comme priorites de la semaine suivante.";
        }
        return "Aucun resultat n'a encore ete renseigne. Le prochain plan reprendra ces priorites sans inventer de progression.";
    }

    private void saveFailedEmailEvent(User user, CoachObjectifsHebdo week, String type, RuntimeException exception) {
        CoachEmailEnvoye event = new CoachEmailEnvoye();
        event.setUser(user);
        event.setObjectifsHebdo(week);
        event.setType(type);
        event.setStatut("ECHEC");
        String detail = exception.getMessage();
        event.setDetailErreur(detail == null ? "Erreur SMTP" : detail.substring(0, Math.min(detail.length(), 500)));
        emailEnvoyeRepository.save(event);
    }

    private LocalDate currentMonday() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Action a definir avec votre equipe." : value.trim();
    }

    private String safeUnit(String value) {
        return value == null || value.isBlank() ? "action" : value.trim();
    }

    private record ObjectiveDraft(String titre, String description, String indicateur, Integer quantiteCible, String unite) {
    }
}
