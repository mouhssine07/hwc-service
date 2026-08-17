package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.*;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MarketingCheckInService {
    private final MarketingSessionRepository sessions;
    private final MarketingSessionService sessionService;

    @Scheduled(cron = "${coach.marketing.check-in-cron:0 15 9 * * *}", zone = "${coach.time-zone:Africa/Casablanca}")
    @Transactional
    public void sendDueCheckIns() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        for (MarketingSession session : sessions.findByCompletedTrueAndCheckInsEnabledTrueAndUpdatedAtBefore(threshold)) {
            sessionService.appendMessage(session.getId(), session.getUser().getEmail(), MarketingSessionMessage.Role.ASSISTANT,
                    "Cela fait environ une semaine depuis votre stratégie. Où en êtes-vous dans la première action prévue, et quel résultat pouvez-vous déjà mesurer ?", null);
            session.setLastCheckInAt(LocalDateTime.now()); sessions.save(session);
        }
    }

    @Transactional
    public boolean setEnabled(String id, String email, boolean enabled) {
        MarketingSession session = sessions.findByIdAndUserEmail(id,email).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Session Marketing introuvable"));
        session.setCheckInsEnabled(enabled); sessions.save(session); return enabled;
    }

    @Transactional(readOnly = true)
    public boolean isEnabled(String id, String email) {
        return sessions.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Session Marketing introuvable"))
                .isCheckInsEnabled();
    }
}
