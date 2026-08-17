package hwc_backend.dto.diagnostic;

import hwc_backend.entity.DiagnosticStatut;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticStartDTO {

    private Long diagnosticId;
    private DiagnosticStatut statut;
    private LocalDateTime dateDebut;
}
