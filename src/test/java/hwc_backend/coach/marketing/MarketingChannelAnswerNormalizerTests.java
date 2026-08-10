package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.service.MarketingChannelAnswerNormalizer;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import org.junit.jupiter.api.Test;

class MarketingChannelAnswerNormalizerTests {

    private final MarketingChannelAnswerNormalizer normalizer = new MarketingChannelAnswerNormalizer();
    private final MarketingStageEvaluator evaluator = new MarketingStageEvaluator();

    @Test
    void acceptsInstagramAndFacebookFromTheCapturedConversation() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.CHANNEL_SELECTION);

        normalizer.normalize(state, "Je peux utiliser Instagram et Facebook pour toucher des clients à Casablanca.");
        evaluator.evaluateAfterAnswer(state);

        assertThat(state.getRecommendedChannels())
                .extracting(channel -> channel.get("name"))
                .containsExactly("Instagram", "Facebook");
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.BUDGET_AND_RESOURCES);
    }

    @Test
    void keepsPreviouslySelectedChannelsAndAddsWhatsAppWithoutDuplicates() {
        MarketingSessionState state = new MarketingSessionState();
        state.setStage(MarketingStrategyStage.CHANNEL_SELECTION);
        normalizer.normalize(state, "Je peux surtout utiliser Instagram et WhatsApp.");
        normalizer.normalize(state, "Instagram et Facebook sont également possibles.");

        assertThat(state.getRecommendedChannels())
                .extracting(channel -> channel.get("name"))
                .containsExactly("Instagram", "WhatsApp", "Facebook");
    }
}
