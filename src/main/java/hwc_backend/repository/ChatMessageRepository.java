package hwc_backend.repository;

import hwc_backend.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationIdOrderByDateEnvoiAsc(Long conversationId);

    void deleteByConversationIdIn(List<Long> conversationIds);
}
