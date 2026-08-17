package hwc_backend.coach.marketing.model;

import java.util.Map;

public record MarketingKnowledgeChunk(
        String id,
        String content,
        double score,
        Map<String, Object> metadata
) {
}
