package hwc_backend.dto.diagnostic;

import hwc_backend.entity.DiagnosticStatut;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticResultatDTO {

    private Long diagnosticId;
    private BigDecimal scoreGlobal;
    private String niveauMaturite;
    private DiagnosticStatut statut;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer progression;
    private Integer totalQuestions;
    private List<ScoreDTO> scores;
}
