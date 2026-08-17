package hwc_backend.coach.marketing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "marketing_coach_kpi_measurements")
@Getter @Setter @NoArgsConstructor
public class MarketingKpiMeasurement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "session_id", nullable = false)
    private MarketingSession session;
    @Column(name = "kpi_name", nullable = false, length = 120) private String kpiName;
    @Column(name = "measured_value", nullable = false, precision = 19, scale = 4) private BigDecimal value;
    @Column(length = 40) private String unit;
    @Column(name = "measured_at", nullable = false) private LocalDateTime measuredAt;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void prePersist() { createdAt = createdAt == null ? LocalDateTime.now() : createdAt; measuredAt = measuredAt == null ? createdAt : measuredAt; }
}
