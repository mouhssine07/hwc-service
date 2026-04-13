package hwc_backend.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author SETUP GAME
 **/
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEtiquettesId implements Serializable {
    private Long serviceId;
    private Long etiquetteId;
}