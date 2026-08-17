package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardClientDTO {

    private boolean disponible;
    private String message;
    private Long dernierDiagnosticId;
    private BigDecimal scoreGlobal;
    private String niveauGlobal;
    private List<ScoreCategorieDTO> scoresParCategorie;
    private List<HistoriqueDiagnosticDTO> historiqueScores;
    private List<ScoreCategorieDTO> categoriesFaibles;
    private List<AlerteCritiqueDTO> alertesCritiques;
    private List<RecommandationResumeDTO> recommandationsPrincipales;
    private List<RecommandationResumeDTO> planActionResume;
    private List<RecommandationResumeDTO> servicesHwcRecommandes;
    private Integer nombreDiagnostics;
    private LocalDateTime dateDernierDiagnostic;
    private BigDecimal progressionDepuisDernier;
    private BenchmarkSecteurDTO benchmarkSecteur;
}
