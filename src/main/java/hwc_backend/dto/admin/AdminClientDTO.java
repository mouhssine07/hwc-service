package hwc_backend.dto.admin;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminClientDTO {

    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String entreprise;
    private String secteur;
    private String tailleEntreprise;
    private String telephone;
    private boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereConnexion;
}
