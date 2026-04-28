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
public class ServiceFonctionnalitesDTO {

    private Long id;
    @NotBlank
    private String contenu;
    @NotNull
    private Long serviceId;
}
