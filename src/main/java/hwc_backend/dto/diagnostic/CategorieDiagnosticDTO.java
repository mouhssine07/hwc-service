package hwc_backend.dto.diagnostic;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDiagnosticDTO {

    private Long id;
    private String nom;
    private String description;
    private String icone;
    private BigDecimal poids;
    private Integer ordre;
    private List<QuestionDTO> questions;
}
