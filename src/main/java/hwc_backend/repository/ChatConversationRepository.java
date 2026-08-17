package hwc_backend.repository;

import hwc_backend.entity.ChatConversation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

    List<ChatConversation> findByUserIdOrderByDateCreationDesc(Long userId);

    List<ChatConversation> findByDateCreationBefore(LocalDateTime cutoff);
}
