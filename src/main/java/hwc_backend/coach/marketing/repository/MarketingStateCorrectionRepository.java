package hwc_backend.coach.marketing.repository;

import hwc_backend.coach.marketing.entity.MarketingStateCorrection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingStateCorrectionRepository extends JpaRepository<MarketingStateCorrection, Long> {
    List<MarketingStateCorrection> findBySessionIdOrderByCreatedAtDesc(String sessionId);
    void deleteBySessionId(String sessionId);
}
