package hwc_backend.coach.marketing.model;

import java.util.List;

public record MarketingFollowUpDashboard(
        String sessionId,
        String immediatePriority,
        List<MarketingActionItemView> actions,
        MarketingKpiDashboard kpis
) { }
