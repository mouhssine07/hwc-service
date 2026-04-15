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
public class ServiceFonctionnalitesDTO {

    private Long id;
    private String contenu;
    private Long serviceId;
}
