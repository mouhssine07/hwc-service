package hwc_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coach_objectifs_hebdo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachObjectifsHebdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "diagnostic_id", nullable = false)
    private Diagnostic diagnostic;

    @Column(name = "semaine_debut", nullable = false)
    private LocalDate semaineDebut;

    @Column(name = "semaine_fin", nullable = false)
    private LocalDate semaineFin;

    @Column(columnDefinition = "TEXT")
    private String conseilSemaine;

    @Column(nullable = false)
    private Integer numeroSemaine = 1;

    @Column(nullable = false)
    private Integer dureeProgrammeSemaines = 12;

    @Column(columnDefinition = "TEXT")
    private String bilanSemaine;

    @Column(columnDefinition = "TEXT")
    private String insightIa;

    private LocalDateTime dateBilan;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateGeneration;

    @PrePersist
    void prePersist() {
        if (dateGeneration == null) {
            dateGeneration = LocalDateTime.now();
        }
    }
}
