package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import hwc_backend.coach.marketing.entity.MarketingActionItem;
import hwc_backend.coach.marketing.entity.MarketingSession;
import hwc_backend.coach.marketing.model.MarketingActionItemView;
import hwc_backend.coach.marketing.model.MarketingActionUpdateRequest;
import hwc_backend.coach.marketing.repository.MarketingActionItemRepository;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingActionPlanService {
    private final MarketingSessionRepository sessions;
    private final MarketingActionItemRepository actions;

    @Transactional
    public void initializeFromDeliverable(String sessionId, String email, JsonNode deliverable) {
        MarketingSession session = owned(sessionId, email);
        if (actions.existsBySessionId(sessionId)) return;
        LocalDate start = LocalDate.now();
        int fallbackWeek = 1;
        for (JsonNode weekNode : deliverable.path("fourWeekPlan")) {
            int week = parseWeek(weekNode.path("week").asText(), fallbackWeek++);
            for (String title : actionTitles(weekNode.path("actions"))) {
                MarketingActionItem item = new MarketingActionItem();
                item.setSession(session);
                item.setTitle(title);
                item.setWeekNumber(week);
                item.setDueDate(start.plusWeeks(week).minusDays(1));
                actions.save(item);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MarketingActionItemView> list(String sessionId, String email) {
        owned(sessionId, email);
        return actions.findBySessionIdOrderByWeekNumberAscIdAsc(sessionId).stream().map(this::view).toList();
    }

    @Transactional
    public MarketingActionItemView update(String sessionId, Long actionId, String email, MarketingActionUpdateRequest request) {
        owned(sessionId, email);
        MarketingActionItem item = actions.findById(actionId)
                .filter(action -> action.getSession().getId().equals(sessionId))
                .orElseThrow(() -> new EntityNotFoundException("Action Marketing introuvable"));
        if (request.completed() != null) {
            item.setStatus(request.completed() ? MarketingActionItem.Status.COMPLETED : MarketingActionItem.Status.TODO);
            item.setCompletedAt(request.completed() ? LocalDateTime.now() : null);
        }
        if (request.dueDate() != null) item.setDueDate(request.dueDate());
        if (request.proofReference() != null) item.setProofReference(request.proofReference().trim());
        item.getSession().setLastClientActivityAt(LocalDateTime.now());
        sessions.save(item.getSession());
        return view(actions.save(item));
    }

    private List<String> actionTitles(JsonNode node) {
        List<String> titles = new ArrayList<>();
        if (node.isArray()) node.forEach(item -> addTitle(titles, item));
        else addTitle(titles, node);
        return titles;
    }

    private void addTitle(List<String> titles, JsonNode node) {
        String title = node.isTextual() ? node.asText() : node.path("title").asText();
        if (title.isBlank() && node.isObject()) title = node.path("action").asText();
        if (!title.isBlank()) titles.add(title.trim());
    }

    private int parseWeek(String value, int fallback) {
        String digits = value == null ? "" : value.replaceAll("\\D+", "");
        return digits.isBlank() ? fallback : Math.max(1, Integer.parseInt(digits));
    }

    private MarketingActionItemView view(MarketingActionItem item) {
        return new MarketingActionItemView(item.getId(), item.getTitle(), item.getWeekNumber(), item.getDueDate(),
                item.getStatus().name(), item.getProofReference(), item.getCompletedAt());
    }

    private MarketingSession owned(String sessionId, String email) {
        return sessions.findByIdAndUserEmail(sessionId, email)
                .orElseThrow(() -> new EntityNotFoundException("Session Marketing introuvable"));
    }
}
