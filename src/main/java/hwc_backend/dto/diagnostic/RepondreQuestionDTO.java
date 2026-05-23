package hwc_backend.dto.diagnostic;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepondreQuestionDTO {

    @NotNull
    private Long questionId;

    @NotNull
    private Long optionReponseId;
}
