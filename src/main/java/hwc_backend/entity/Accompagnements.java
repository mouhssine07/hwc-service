package hwc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SETUP GAME
 **/
@Entity
@Table(name = "accompagnements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accompagnements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private String accroche;

    @Column(columnDefinition = "TEXT")
    private String description1;

    @Column(columnDefinition = "TEXT")
    private String description2;

    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}