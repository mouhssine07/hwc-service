package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MarketingFinalStagesAnswerNormalizer {

    public MarketingSessionState normalize(MarketingSessionState state, String userMessage) {
        if (userMessage == null || userMessage.isBlank()) return state;
        String answer = userMessage.trim();

        if (state.getStage() == MarketingStrategyStage.ACTION_PLAN && state.getWeeklyActions().isEmpty()) {
            Map<String, Object> planInput = new LinkedHashMap<>();
            planInput.put("planningHorizon", "4 semaines");
            planInput.put("responsible", answer);
            state.setWeeklyActions(List.of(planInput));
        }
        if (state.getStage() == MarketingStrategyStage.KPI_SELECTION && state.getKpis().isEmpty()) {
            Map<String, Object> kpi = new LinkedHashMap<>();
            kpi.put("name", answer);
            state.setKpis(List.of(kpi));
        }
        return state;
    }
}
