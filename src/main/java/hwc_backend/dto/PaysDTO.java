package hwc_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaysDTO {

    private Long id;
    @NotBlank
    private String nom;
    private String codePays;
    private String ville;
}
