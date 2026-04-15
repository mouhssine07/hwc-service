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
public class ChiffresClesDTO {

    private Long id;
    private String valeur;
    private String libelle;
}
