package hwc_backend.coach.marketing.model;

import java.time.LocalDateTime;

public record MarketingSessionSummary(
        String sessionId,
        String title,
        String serviceId,
        Long diagnosticId,
        MarketingStrategyStage stage,
        boolean completed,
        LocalDateTime updatedAt
) {
}
