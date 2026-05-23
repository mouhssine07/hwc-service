package hwc_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regles_recommandation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegleRecommandation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategorieDiagnostic categorie;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal seuilScore;

    @Column(nullable = false, length = 5)
    private String operateur;

    @ManyToOne
    @JoinColumn(name = "service_hwc_id")
    private Services serviceHwc;

    @ManyToOne
    @JoinColumn(name = "sous_service_hwc_id")
    private SousServices sousServiceHwc;

    @Column(nullable = false, length = 255)
    private String titreRecommandation;

    @Column(columnDefinition = "TEXT")
    private String descriptionRecommandation;

    @Column(nullable = false, length = 20)
    private String horizon;

    @Column(length = 100)
    private String impactEstime;

    @Column(columnDefinition = "TEXT")
    private String kpisSuggeres;

    @Column(nullable = false)
    private Integer priorite;

    @Column(nullable = false)
    private boolean actif = true;
}
