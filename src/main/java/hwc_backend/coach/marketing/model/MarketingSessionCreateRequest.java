package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MarketingSessionCreateRequest(
        @NotBlank
        @Pattern(regexp = "MARKETING_STRATEGY", message = "seul le service MARKETING_STRATEGY est disponible")
        String serviceId
) {
}
