package hwc_backend.coach.marketing.repository;

import hwc_backend.coach.marketing.entity.MarketingActionItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketingActionItemRepository extends JpaRepository<MarketingActionItem, Long> {
    List<MarketingActionItem> findBySessionIdOrderByWeekNumberAscIdAsc(String sessionId);
    boolean existsBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
}
