package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.MarketingSession;
import hwc_backend.coach.marketing.model.MarketingActionItemView;
import hwc_backend.coach.marketing.model.MarketingKpiDashboard;
import hwc_backend.coach.marketing.model.MarketingProactivePriority;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingProactivePriorityService {
    private final MarketingSessionRepository sessions;
    private final MarketingKpiService kpiService;
    private final MarketingActionPlanService actionPlanService;

    @Transactional(readOnly = true)
    public Optional<MarketingProactivePriority> current(String email) {
        return sessions.findFirstByUserEmailAndCompletedTrueOrderByUpdatedAtDesc(email)
                .map(session -> priority(session, email));
    }

    private MarketingProactivePriority priority(MarketingSession session, String email) {
        LocalDateTime lastActivity = session.getLastClientActivityAt() == null
                ? session.getDeliverableGeneratedAt() : session.getLastClientActivityAt();
        boolean inactive = lastActivity != null && lastActivity.isBefore(LocalDateTime.now().minusDays(7));
        MarketingKpiDashboard kpis = kpiService.dashboard(session.getId(), email);
        String priority = kpis.nextPriority();
        if (inactive) {
            MarketingActionItemView nextAction = actionPlanService.list(session.getId(), email).stream()
                    .filter(action -> !"COMPLETED".equals(action.status())).findFirst().orElse(null);
            priority = nextAction == null
                    ? "Votre suivi est inactif depuis 7 jours. Ajoutez une nouvelle mesure KPI pour choisir la prochaine optimisation."
                    : "Votre suivi est inactif depuis 7 jours. Reprenez par cette action : " + nextAction.title();
        }
        return new MarketingProactivePriority(session.getId(), priority, inactive, lastActivity);
    }
}
