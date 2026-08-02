package hwc_backend.dto.coach;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CoachObjectifProgressionDTO(
        @Min(0) @Max(1000000) Integer quantiteRealisee,
        @Size(max = 1000) String commentaire
) {
}
