package hwc_backend.dto.rapport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportGenerationResult {

    private String introduction;
    private String analyseForts;
    private String analyseFaibles;
    private String planAction;
    private String conclusion;
    private Integer tokensUsed;
}
