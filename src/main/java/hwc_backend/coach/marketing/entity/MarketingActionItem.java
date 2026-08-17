package hwc_backend.coach.marketing.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "marketing_coach_action_items")
@Getter @Setter @NoArgsConstructor
public class MarketingActionItem {
    public enum Status { TODO, COMPLETED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private MarketingSession session;
    @Column(nullable = false, length = 500)
    private String title;
    @Column(name = "week_number", nullable = false)
    private int weekNumber;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.TODO;
    @Column(name = "proof_reference", length = 1000)
    private String proofReference;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
