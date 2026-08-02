package hwc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author SETUP GAME
 **/
@Entity
@Table(name = "demandes_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandesContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nom;

    private String telephone;

    private String serviceDemande;

    @Column(length = 1500)
    private String message;

    private LocalDateTime dateCreation;
}
