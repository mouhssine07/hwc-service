package hwc_backend.coach.marketing.repository;

import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingSessionMessageRepository extends JpaRepository<MarketingSessionMessage, Long> {

    List<MarketingSessionMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    void deleteBySessionId(String sessionId);
}
