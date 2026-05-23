package hwc_backend.dto.diagnostic;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long id;
    private String texte;
    private Integer ordre;
    private List<OptionReponseDTO> options;
}
