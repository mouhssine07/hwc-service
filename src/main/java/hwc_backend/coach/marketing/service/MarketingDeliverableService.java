package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingStrategyResult;

public interface MarketingDeliverableService {

    MarketingStrategyResult finalizeStrategy(String sessionId, String email);

    String getDeliverable(String sessionId, String email);
}
