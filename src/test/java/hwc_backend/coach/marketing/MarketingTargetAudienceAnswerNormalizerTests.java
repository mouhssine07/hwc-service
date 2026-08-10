package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import hwc_backend.coach.marketing.service.MarketingTargetAudienceAnswerNormalizer;
import java.util.List;
import org.junit.jupiter.api.Test;

class MarketingTargetAudienceAnswerNormalizerTests {

    private final MarketingTargetAudienceAnswerNormalizer normalizer = new MarketingTargetAudienceAnswerNormalizer();
    private final MarketingStageEvaluator evaluator = new MarketingStageEvaluator();

    @Test
    void storesNeedThenAsksOnlyForObjection() {
        MarketingSessionState state = targetAudienceState();
        normalizer.normalize(state, "Le besoin principal est de gagner du temps avec un lavage fiable et pratique.");
        evaluator.evaluateAfterAnswer(state);
        assertThat(state.getTargetAudience().getPrimaryPersona()).containsKey("primaryNeed");
        assertThat(state.getMissingInformation())
                .containsExactly("targetAudience.primaryPersona.mainObjection");
        assertThat(evaluator.fallbackQuestion(state)).containsIgnoringCase("objection");
    }

    @Test
    void advancesAfterTheObjectionIsProvided() {
        MarketingSessionState state = targetAudienceState();
        normalizer.normalize(state, "Le besoin principal est de gagner du temps avec un lavage fiable et pratique.");
        normalizer.normalize(state, "Le frein principal est la crainte d'un service peu fiable ou trop cher.");
        evaluator.evaluateAfterAnswer(state);
        assertThat(state.getTargetAudience().getPrimaryPersona()).containsKeys("primaryNeed", "mainObjection");
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.POSITIONING);
    }

    private MarketingSessionState targetAudienceState() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.TARGET_AUDIENCE);
        state.getTargetAudience().setSegments(List.of("Clients recherchant un lavage pratique et régulier"));
        return state;
    }
}
