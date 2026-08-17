package hwc_backend.coach.marketing.model;

import java.time.LocalDateTime;
import java.util.List;

public record MarketingConversationMessage(
        String role,
        String content,
        String imageDataUrl,
        String imageName,
        List<MarketingImageAnnotation> imageAnnotations,
        String stage,
        LocalDateTime createdAt
) {
}
