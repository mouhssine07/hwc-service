package hwc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Entity
@Table(name = "service_fonctionnalites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFonctionnalites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;
}