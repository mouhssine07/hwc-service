package hwc_backend.coach.marketing.repository;
import hwc_backend.coach.marketing.entity.MarketingKpiMeasurement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketingKpiMeasurementRepository extends JpaRepository<MarketingKpiMeasurement, Long> {
    List<MarketingKpiMeasurement> findBySessionIdOrderByMeasuredAtAsc(String sessionId);
    void deleteBySessionId(String sessionId);
}
