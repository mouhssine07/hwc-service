package hwc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Entity
@Table(name = "service_etiquettes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEtiquettes {

    @EmbeddedId
    private ServiceEtiquettesId id;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private Services service;

    @ManyToOne
    @MapsId("etiquetteId")
    @JoinColumn(name = "etiquette_id")
    private Etiquettes etiquette;
}