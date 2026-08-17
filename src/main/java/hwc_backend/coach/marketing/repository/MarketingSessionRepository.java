package hwc_backend.coach.marketing.repository;

import hwc_backend.coach.marketing.entity.MarketingSession;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingSessionRepository extends JpaRepository<MarketingSession, String> {

    Optional<MarketingSession> findByIdAndUserEmail(String id, String email);

    List<MarketingSession> findByUserEmailOrderByUpdatedAtDesc(String email);
    Optional<MarketingSession> findFirstByUserEmailAndCompletedTrueOrderByUpdatedAtDesc(String email);
    List<MarketingSession> findByCompletedTrueAndCheckInsEnabledTrueAndUpdatedAtBefore(LocalDateTime threshold);
}
