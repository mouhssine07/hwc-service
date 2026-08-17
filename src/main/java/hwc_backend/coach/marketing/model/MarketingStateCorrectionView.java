package hwc_backend.coach.marketing.model;

import java.time.LocalDateTime;

public record MarketingStateCorrectionView(
        String path,
        Object oldValue,
        Object newValue,
        String actorEmail,
        LocalDateTime createdAt
) {
}
