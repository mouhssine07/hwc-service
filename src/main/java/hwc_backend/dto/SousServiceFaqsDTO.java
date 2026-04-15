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
public class SousServiceFaqsDTO {

    private Long id;
    private String question;
    private String reponse;
    private Long sousServiceId;
}
