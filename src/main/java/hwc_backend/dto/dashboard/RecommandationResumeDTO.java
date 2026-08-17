package hwc_backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommandationResumeDTO {

    private Long id;
    private String titre;
    private String description;
    private String horizon;
    private String serviceHwcNom;
    private String sousServiceHwcNom;
    private String impactEstime;
    private Integer priorite;
}
