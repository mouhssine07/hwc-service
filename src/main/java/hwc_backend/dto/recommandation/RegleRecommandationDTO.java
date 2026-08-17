package hwc_backend.dto.recommandation;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegleRecommandationDTO {

    private Long id;
    private Long categorieId;
    private String categorieNom;
    private BigDecimal seuilScore;
    private String operateur;
    private Long serviceHwcId;
    private String serviceHwcNom;
    private Long sousServiceHwcId;
    private String sousServiceHwcNom;
    private String titreRecommandation;
    private String descriptionRecommandation;
    private String horizon;
    private String impactEstime;
    private String kpisSuggeres;
    private Integer priorite;
    private boolean actif;
}
