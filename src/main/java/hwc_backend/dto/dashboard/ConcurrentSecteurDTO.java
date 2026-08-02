package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcurrentSecteurDTO {

    private String libelle;
    private BigDecimal score;
    private BigDecimal ecartAvecClient;
}
