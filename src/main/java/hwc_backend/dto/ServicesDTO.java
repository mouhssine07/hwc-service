package hwc_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDTO {

    private Long id;
    private String accroche;
    @NotBlank
    private String titre;
    @NotBlank
    private String description;
    private String icone;
    private List<EtiquettesDTO> etiquettes;
}
