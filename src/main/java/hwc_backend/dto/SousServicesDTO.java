package hwc_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServicesDTO {

    private Long id;
    private String titre;
    private String accroche;
    private String description;
    private String descriptionComplete;
    private String icone;
    private Long serviceId;
    private String serviceTitre;
}
