package hwc_backend.coach.marketing.model;

import java.util.List;

public record MarketingMessageResponse(
        String sessionId,
        MarketingStrategyStage stage,
        String message,
        List<String> missingInformation,
        MarketingSessionState.Confidence confidence,
        boolean readyToFinalize,
        List<String> suggestedReplies
) {
}
