package hwc_backend.dto;

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
    private String titre;
    private String description;
    private String icone;
    private List<EtiquettesDTO> etiquettes;
}
