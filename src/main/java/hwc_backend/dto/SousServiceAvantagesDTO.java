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
public class SousServiceAvantagesDTO {

    private Long id;
    private String titre;
    private String description;
    private Long sousServiceId;
}
