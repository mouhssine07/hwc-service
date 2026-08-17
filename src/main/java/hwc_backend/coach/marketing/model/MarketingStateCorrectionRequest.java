package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarketingStateCorrectionRequest(
        @NotBlank String path,
        @NotNull Object value
) {
}
