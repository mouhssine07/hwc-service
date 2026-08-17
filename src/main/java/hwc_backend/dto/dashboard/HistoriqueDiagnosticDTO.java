package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueDiagnosticDTO {

    private Long diagnosticId;
    private BigDecimal scoreGlobal;
    private String niveauGlobal;
    private LocalDateTime dateFin;
}
