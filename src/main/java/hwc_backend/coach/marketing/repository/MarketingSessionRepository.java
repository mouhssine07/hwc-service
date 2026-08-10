package hwc_backend.coach.marketing.repository;

import hwc_backend.coach.marketing.entity.MarketingSession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingSessionRepository extends JpaRepository<MarketingSession, String> {

    Optional<MarketingSession> findByIdAndUserEmail(String id, String email);

    List<MarketingSession> findByUserEmailOrderByUpdatedAtDesc(String email);
}
