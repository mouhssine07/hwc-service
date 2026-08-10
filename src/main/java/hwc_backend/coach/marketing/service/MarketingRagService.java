package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import java.util.List;

public interface MarketingRagService {

    List<MarketingKnowledgeChunk> retrieve(String question);
}
