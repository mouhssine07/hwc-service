package hwc_backend.dto.diagnostic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionReponseDTO {

    private Long id;
    private String texte;
    private Integer poids;
    private Integer ordre;
}
