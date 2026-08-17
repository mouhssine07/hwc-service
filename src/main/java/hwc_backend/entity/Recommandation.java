package hwc_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommandations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommandation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diagnostic_id", nullable = false)
    private Diagnostic diagnostic;

    @ManyToOne
    @JoinColumn(name = "regle_id")
    private RegleRecommandation regle;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String horizon;

    @ManyToOne
    @JoinColumn(name = "service_hwc_id")
    private Services serviceHwc;

    @ManyToOne
    @JoinColumn(name = "sous_service_hwc_id")
    private SousServices sousServiceHwc;

    @Column(length = 100)
    private String impactEstime;

    @Column(columnDefinition = "TEXT")
    private String kpis;

    @Column(nullable = false)
    private Integer priorite;
}
