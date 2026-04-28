package hwc_backend.dto.auth;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private boolean actif;
    private Set<String> roles;
}
