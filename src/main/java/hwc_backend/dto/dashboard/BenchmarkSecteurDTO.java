package hwc_backend.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkSecteurDTO {

    private boolean disponible;
    private String message;
    private String secteur;
    private String prenomClient;
    private BigDecimal scoreClient;
    private BigDecimal moyenneSecteur;
    private BigDecimal topDixPourcent;
    private Integer nombreEntreprises;
    private Integer positionClient;
    private List<ConcurrentSecteurDTO> concurrentsDevant;
}
