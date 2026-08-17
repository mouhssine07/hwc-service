package hwc_backend.dto.recommandation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommandationDTO {

    private Long id;
    private String titre;
    private String description;
    private String horizon;
    private Long serviceHwcId;
    private String serviceHwcNom;
    private Long sousServiceHwcId;
    private String sousServiceHwcNom;
    private String impactEstime;
    private List<String> kpis;
    private Integer priorite;
}
