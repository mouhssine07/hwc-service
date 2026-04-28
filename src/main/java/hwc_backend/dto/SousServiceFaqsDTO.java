package hwc_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceFaqsDTO {

    private Long id;
    @NotBlank
    private String question;
    @NotBlank
    private String reponse;
    @NotNull
    private Long sousServiceId;
}
