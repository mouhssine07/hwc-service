package hwc_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coach_objectif_resultat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachObjectifResultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "objectif_hebdo_id", nullable = false)
    private CoachObjectifsHebdo objectifsHebdo;

    @Column(nullable = false)
    private Integer ordre;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String indicateur;

    private Integer quantiteCible;

    @Column(nullable = false)
    private Integer quantiteRealisee = 0;

    @Column(length = 60)
    private String unite;

    @Column(length = 1000)
    private String commentaireClient;

    private LocalDateTime dateMiseAJour;

    @Column(nullable = false)
    private boolean termine = false;

    private LocalDateTime dateCompletion;
}
