package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyResult;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingDeliverableServiceImpl;
import hwc_backend.coach.marketing.service.MarketingLanguageModelService;
import hwc_backend.coach.marketing.service.MarketingPromptService;
import hwc_backend.coach.marketing.service.MarketingSessionService;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MarketingDeliverableServiceTests {

    @Test
    void generatesValidatesRendersAndPersistsACompleteStrategy() {
        MarketingSessionService sessionService = mock(MarketingSessionService.class);
        MarketingLanguageModelService languageModel = mock(MarketingLanguageModelService.class);
        MarketingSessionState state = completeState();
        when(sessionService.getState("session-demo", "client@hwc.ma")).thenReturn(state);
        when(languageModel.generateJson(contains("Marketing Digital"), contains("ÉTAT VALIDÉ")))
                .thenReturn(validDeliverableJson());

        MarketingDeliverableServiceImpl service = new MarketingDeliverableServiceImpl(
                sessionService, new MarketingStageEvaluator(), new MarketingPromptService(), languageModel);

        MarketingStrategyResult result = service.finalizeStrategy("session-demo", "client@hwc.ma");

        assertThat(result.markdown()).contains(
                "# Mini-stratégie Marketing Digital",
                "Augmenter de 30 % les réservations directes",
                "Google Business Profile",
                "Semaine 4");
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.COMPLETED);
        assertThat(state.isCompleted()).isTrue();
        verify(sessionService).saveDeliverable(eq("session-demo"), eq("client@hwc.ma"),
                contains("priorityChannels"), contains("# Mini-stratégie Marketing Digital"));
        verify(sessionService).saveState("session-demo", "client@hwc.ma", state);
    }

    private MarketingSessionState completeState() {
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("session-demo");
        state.setStage(MarketingStrategyStage.FINAL_DELIVERABLE);
        state.getCompany().setSector("Restauration");
        state.getCompany().setProductsOrServices(List.of("Restaurant marocain"));
        state.getCompany().setBusinessModel("B2C");
        state.getCompany().setLocation("Casablanca");
        state.getCompany().setSize("TPE");
        state.getObjectives().setType("Réservations directes");
        state.getObjectives().setTargetValue("+30 %");
        state.getObjectives().setDeadline("3 mois");
        state.getObjectives().setSmartStatement("Augmenter de 30 % les réservations directes en trois mois");
        state.getAudit().setWebsite(true);
        state.getAudit().setTrackingAvailable(false);
        state.getTargetAudience().setSegments(List.of("Actifs de Casablanca"));
        state.getTargetAudience().setPrimaryPersona(Map.of(
                "primaryNeed", "réserver facilement",
                "mainObjection", "crainte d'un service peu fiable"));
        state.getPositioning().setValueProposition("Cuisine marocaine locale avec réservation directe simple");
        state.setRecommendedChannels(List.of(Map.of("name", "Google Business Profile")));
        state.getBudget().setMonthlyAmount(BigDecimal.valueOf(3000));
        state.getBudget().setWeeklyTimeHours(BigDecimal.valueOf(5));
        state.getBudget().setLeadHandlingCapacity("20 demandes par semaine");
        state.setWeeklyActions(List.of(Map.of("week", 1, "action", "Optimiser la fiche locale")));
        state.setKpis(List.of(Map.of("name", "Réservations directes")));
        return state;
    }

    private String validDeliverableJson() {
        return """
                {
                  "sessionId": "session-demo",
                  "companySummary": "Restaurant marocain B2C à Casablanca.",
                  "currentAudit": "Site présent, suivi des conversions absent.",
                  "smartObjective": "Augmenter de 30 % les réservations directes en trois mois.",
                  "targetAudience": {"segment": "Actifs de Casablanca"},
                  "positioning": {"valueProposition": "Cuisine locale et réservation simple"},
                  "priorityChannels": [
                    {"name": "Google Business Profile", "justification": "Recherche locale", "objective": "Réservations", "requiredResources": "Photos et avis"}
                  ],
                  "fourWeekPlan": [
                    {"week": "Semaine 1", "actions": ["Installer le suivi"]},
                    {"week": "Semaine 2", "actions": ["Optimiser les supports"]},
                    {"week": "Semaine 3", "actions": ["Lancer le test"]},
                    {"week": "Semaine 4", "actions": ["Mesurer et optimiser"]}
                  ],
                  "budgetAndResources": {"monthlyAmount": 3000, "currency": "MAD"},
                  "kpis": [{"name": "Réservations directes"}],
                  "assumptions": [],
                  "pointsToVerify": ["Valeur actuelle des réservations"],
                  "immediatePriority": "Installer le suivi des réservations."
                }
                """;
    }
}
