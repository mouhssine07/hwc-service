package hwc_backend.coach.marketing.model;

import java.time.LocalDateTime;

public record MarketingConversationMessage(
        String role,
        String content,
        String imageDataUrl,
        String imageName,
        String stage,
        LocalDateTime createdAt
) {
}
