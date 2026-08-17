package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record MarketingActionUpdateRequest(
        Boolean completed,
        LocalDate dueDate,
        @Size(max = 1000) String proofReference
) { }
