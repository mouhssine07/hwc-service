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
public class CertificationsDTO {

    private Long id;
    @NotBlank
    private String nom;
    private String logoUrl;
    private String type;
}
