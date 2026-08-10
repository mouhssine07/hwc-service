package hwc_backend.coach.marketing.entity;

import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "marketing_coach_sessions")
@Getter
@Setter
@NoArgsConstructor
public class MarketingSession {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "service_id", nullable = false, length = 40, updatable = false)
    private String serviceId = "MARKETING_STRATEGY";

    @Column(name = "diagnostic_id")
    private Long diagnosticId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private MarketingStrategyStage stage;

    @Column(name = "state_json", nullable = false, columnDefinition = "LONGTEXT")
    private String stateJson;

    @Column(name = "deliverable_json", columnDefinition = "LONGTEXT")
    private String deliverableJson;

    @Column(name = "deliverable_markdown", columnDefinition = "LONGTEXT")
    private String deliverableMarkdown;

    @Column(nullable = false)
    private boolean completed;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = createdAt == null ? now : createdAt;
        updatedAt = now;
        stage = stage == null ? MarketingStrategyStage.COMPANY_DISCOVERY : stage;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
