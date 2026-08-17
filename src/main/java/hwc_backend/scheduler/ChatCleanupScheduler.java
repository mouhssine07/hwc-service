package hwc_backend.scheduler;

import hwc_backend.entity.ChatConversation;
import hwc_backend.repository.ChatConversationRepository;
import hwc_backend.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatCleanupScheduler {

    private final ChatConversationRepository chatConversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Value("${chat.retention-days:30}")
    private int retentionDays;

    @Scheduled(cron = "${chat.cleanup-cron:0 30 3 * * *}")
    @Transactional
    public void cleanupOldConversations() {
        if (retentionDays <= 0) {
            return;
        }

        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        List<ChatConversation> oldConversations = chatConversationRepository.findByDateCreationBefore(cutoff);
        if (oldConversations.isEmpty()) {
            return;
        }

        List<Long> conversationIds = oldConversations.stream()
                .map(ChatConversation::getId)
                .toList();

        chatMessageRepository.deleteByConversationIdIn(conversationIds);
        chatConversationRepository.deleteAll(oldConversations);
    }
}
