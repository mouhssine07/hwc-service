package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MarketingPublicFindingDecisionRequest(
        @NotNull @Min(0) Integer findingIndex,
        boolean accepted
) { }
