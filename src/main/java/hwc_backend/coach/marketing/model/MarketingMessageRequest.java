package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

public record MarketingMessageRequest(
        @NotBlank(message = "le message est obligatoire")
        @Size(max = 4000, message = "le message ne doit pas dépasser 4000 caractères")
        String message,
        Map<String, Object> structuredInput
) {
    public MarketingMessageRequest(String message) { this(message, null); }
}
