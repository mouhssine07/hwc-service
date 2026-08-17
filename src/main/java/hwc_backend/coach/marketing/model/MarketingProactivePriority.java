package hwc_backend.coach.marketing.model;

import java.time.LocalDateTime;

public record MarketingProactivePriority(
        String sessionId,
        String priority,
        boolean inactive,
        LocalDateTime lastClientActivityAt
) { }
