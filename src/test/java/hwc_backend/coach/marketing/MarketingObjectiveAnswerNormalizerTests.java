package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingObjectiveAnswerNormalizer;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import org.junit.jupiter.api.Test;

class MarketingObjectiveAnswerNormalizerTests {

    private final MarketingObjectiveAnswerNormalizer normalizer = new MarketingObjectiveAnswerNormalizer();
    private final MarketingStageEvaluator evaluator = new MarketingStageEvaluator();

    @Test
    void acceptsTheMeasurableAndDatedObjectiveShownInTheClientConversation() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.OBJECTIVES);
        normalizer.normalize(state, "Mon objectif est d’atteindre 50 nouveaux clients par mois à Casablanca d’ici 3 mois.");
        evaluator.evaluateAfterAnswer(state);
        assertThat(state.getObjectives().getType()).isEqualTo("ACQUISITION_CLIENTS");
        assertThat(state.getObjectives().getTargetValue().toString()).contains("50 nouveaux clients");
        assertThat(state.getObjectives().getDeadline()).isEqualTo("d’ici 3 mois");
        assertThat(state.getObjectives().getSmartStatement()).contains("50 nouveaux clients");
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.CURRENT_AUDIT);
    }

    @Test
    void doesNotAdvanceForAnAnswerWithoutAValueOrDeadline() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.OBJECTIVES);
        normalizer.normalize(state, "Je voudrais avoir plus de clients.");
        evaluator.evaluateAfterAnswer(state);
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.OBJECTIVES);
        assertThat(state.getMissingInformation()).contains("objectives.targetValue", "objectives.deadline");
    }
}
