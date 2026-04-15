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
public class AccompagnementsDTO {

    private Long id;
    private String titre;
    private String accroche;
    private String description1;
    private String description2;
    private Long sousServiceId;
}
