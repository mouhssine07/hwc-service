package hwc_backend.dto.recommandation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanActionDTO {

    private List<RecommandationDTO> courtTerme;
    private List<RecommandationDTO> moyenTerme;
    private List<RecommandationDTO> longTerme;
    private String impactTotal;
}
