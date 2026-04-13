package hwc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Entity
@Table(name = "sous_service_faqs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceFaqs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String reponse;

    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}