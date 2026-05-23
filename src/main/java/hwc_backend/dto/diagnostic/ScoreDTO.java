package hwc_backend.dto.diagnostic;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {

    private Long categorieId;
    private String categorieNom;
    private BigDecimal score;
    private String niveau;
    private Integer pointsObtenus;
    private Integer pointsMax;
}
