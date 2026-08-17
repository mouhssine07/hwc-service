package hwc_backend.coach.marketing.model;

import java.util.List;

public record MarketingMessageResponse(
        String sessionId,
        MarketingStrategyStage stage,
        String message,
        List<String> missingInformation,
        MarketingSessionState.Confidence confidence,
        boolean readyToFinalize,
        List<String> suggestedReplies,
        List<MarketingImageAnnotation> imageAnnotations
) {
    public MarketingMessageResponse(
            String sessionId, MarketingStrategyStage stage, String message, List<String> missingInformation,
            MarketingSessionState.Confidence confidence, boolean readyToFinalize, List<String> suggestedReplies) {
        this(sessionId, stage, message, missingInformation, confidence, readyToFinalize, suggestedReplies, List.of());
    }
}
