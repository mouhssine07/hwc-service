package hwc_backend.dto.clientauth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterClientDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String entreprise;

    @NotBlank
    private String secteur;

    private String tailleEntreprise;

    private String telephone;
}
