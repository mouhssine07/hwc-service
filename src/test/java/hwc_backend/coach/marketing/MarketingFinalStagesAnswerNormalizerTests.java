package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingFinalStagesAnswerNormalizer;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import org.junit.jupiter.api.Test;

class MarketingFinalStagesAnswerNormalizerTests {

    private final MarketingFinalStagesAnswerNormalizer normalizer = new MarketingFinalStagesAnswerNormalizer();
    private final MarketingStageEvaluator evaluator = new MarketingStageEvaluator();

    @Test
    void acceptsTheResponsiblePersonFromTheCapturedConversation() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.ACTION_PLAN);

        normalizer.normalize(state, "Une personne de l’équipe sera responsable des premières actions.");
        evaluator.evaluateAfterAnswer(state);

        assertThat(state.getWeeklyActions()).singleElement().satisfies(item ->
                assertThat(item.get("responsible")).isEqualTo(
                        "Une personne de l’équipe sera responsable des premières actions."));
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.KPI_SELECTION);
    }

    @Test
    void acceptsAKpiAndReachesFinalDeliverable() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.KPI_SELECTION);

        normalizer.normalize(state, "Le nombre de réservations mensuelles sera notre indicateur principal.");
        evaluator.evaluateAfterAnswer(state);

        assertThat(state.getKpis()).singleElement().satisfies(item ->
                assertThat(item.get("name")).isEqualTo(
                        "Le nombre de réservations mensuelles sera notre indicateur principal."));
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.FINAL_DELIVERABLE);
    }
}
