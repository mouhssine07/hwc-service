package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlerteCritiqueDTO {

    private Long categorieId;
    private String categorieNom;
    private BigDecimal score;
    private String message;
    private String priorite;
}
