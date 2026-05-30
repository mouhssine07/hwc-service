package hwc_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rapports_pdf")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostic_id", nullable = false)
    private Diagnostic diagnostic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255)
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String analyseForts;

    @Column(columnDefinition = "TEXT")
    private String analyseFaibles;

    @Column(columnDefinition = "TEXT")
    private String planAction;

    @Column(columnDefinition = "TEXT")
    private String conclusion;

    private Integer tokensUsed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateGeneration;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] pdfContent;

    @PrePersist
    void prePersist() {
        if (dateGeneration == null) {
            dateGeneration = LocalDateTime.now();
        }
    }
}
