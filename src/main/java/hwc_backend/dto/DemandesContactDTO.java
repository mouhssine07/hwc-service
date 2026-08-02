package hwc_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandesContactDTO {

    private Long id;
    @Email
    @NotBlank
    private String email;

    @Size(max = 150)
    private String nom;

    @Size(max = 40)
    private String telephone;

    @Size(max = 255)
    private String serviceDemande;

    @Size(max = 1500)
    private String message;

    private java.time.LocalDateTime dateCreation;
}
