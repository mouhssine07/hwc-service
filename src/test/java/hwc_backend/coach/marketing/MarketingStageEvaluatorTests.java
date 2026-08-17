package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import java.util.List;
import org.junit.jupiter.api.Test;

class MarketingStageEvaluatorTests {

    private final MarketingStageEvaluator evaluator = new MarketingStageEvaluator();

    @Test
    void doesNotAdvanceBeforeCompanyDiscoveryIsComplete() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setSector("Restauration");

        evaluator.evaluateAfterAnswer(state);

        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.COMPANY_DISCOVERY);
        assertThat(state.getMissingInformation()).contains(
                "company.productsOrServices", "company.businessModel", "company.location", "company.size");
    }

    @Test
    void advancesOnlyOneStageWhenCurrentStageIsComplete() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setSector("Restauration");
        state.getCompany().setProductsOrServices(List.of("Restauration sur place"));
        state.getCompany().setBusinessModel("B2C");
        state.getCompany().setLocation("Casablanca");
        state.getCompany().setSize("TPE");

        evaluator.evaluateAfterAnswer(state);

        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.OBJECTIVES);
        assertThat(state.getMissingInformation()).contains("objectives.type");
    }

    @Test
    void refusesFinalizationWhenAnEarlierSectionIsIncomplete() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.FINAL_DELIVERABLE);

        assertThat(evaluator.canFinalize(state)).isFalse();
    }

    @Test
    void summarizesSkippedStagesAndContextualizesRestaurantObjection() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.TARGET_AUDIENCE);
        state.getCompany().setName("Le Petit Comptoir");
        state.getCompany().setSector("Restauration");
        state.setMissingInformation(List.of("targetAudience.primaryPersona.mainObjection"));

        String message = evaluator.transitionMessage(MarketingStrategyStage.COMPANY_DISCOVERY, state);

        assertThat(message).contains("entreprise, objectif et audit", "Le Petit Comptoir", "temps d'attente");
    }
}
