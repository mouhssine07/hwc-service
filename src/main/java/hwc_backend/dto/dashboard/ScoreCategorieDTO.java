package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreCategorieDTO {

    private Long categorieId;
    private String categorieNom;
    private String niveau;
    private BigDecimal score;
    private Integer pointsObtenus;
    private Integer pointsMax;
    private Integer ordre;
}
