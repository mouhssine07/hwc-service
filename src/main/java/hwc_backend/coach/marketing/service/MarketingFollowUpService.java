package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.entity.MarketingSession;
import hwc_backend.coach.marketing.model.MarketingFollowUpDashboard;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingFollowUpService {
    private final MarketingSessionRepository sessions;
    private final MarketingActionPlanService actionPlanService;
    private final MarketingKpiService kpiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public MarketingFollowUpDashboard dashboard(String sessionId, String email) {
        MarketingSession session = sessions.findByIdAndUserEmail(sessionId, email)
                .orElseThrow(() -> new EntityNotFoundException("Session Marketing introuvable"));
        if (!session.isCompleted() || session.getDeliverableJson() == null) {
            throw new IllegalStateException("Le suivi est disponible après la finalisation de la stratégie");
        }
        initializeLegacyPlan(session, email);
        return new MarketingFollowUpDashboard(sessionId, immediatePriority(session),
                actionPlanService.list(sessionId, email), kpiService.dashboard(sessionId, email));
    }

    private void initializeLegacyPlan(MarketingSession session, String email) {
        try {
            actionPlanService.initializeFromDeliverable(session.getId(), email,
                    objectMapper.readTree(session.getDeliverableJson()));
        } catch (Exception exception) {
            throw new IllegalStateException("Le plan d’action du livrable est invalide", exception);
        }
    }

    private String immediatePriority(MarketingSession session) {
        try {
            String value = objectMapper.readTree(session.getDeliverableJson()).path("immediatePriority").asText();
            return value.isBlank() ? "Commencez par la première action du plan." : value;
        } catch (Exception exception) {
            return "Commencez par la première action du plan.";
        }
    }
}
