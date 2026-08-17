package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.*;
import hwc_backend.coach.marketing.model.*;
import hwc_backend.coach.marketing.repository.*;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MarketingKpiService {
    private final MarketingSessionRepository sessions;
    private final MarketingKpiMeasurementRepository measurements;

    @Transactional
    public MarketingKpiDashboard add(String sessionId, String email, MarketingKpiMeasurementRequest request) {
        MarketingSession session = owned(sessionId, email);
        MarketingKpiMeasurement item = new MarketingKpiMeasurement();
        item.setSession(session); item.setKpiName(request.name().trim()); item.setValue(request.value());
        item.setUnit(request.unit()); item.setMeasuredAt(request.measuredAt()); measurements.save(item);
        session.setLastClientActivityAt(java.time.LocalDateTime.now());
        sessions.save(session);
        return dashboard(sessionId, email);
    }

    @Transactional(readOnly = true)
    public MarketingKpiDashboard dashboard(String sessionId, String email) {
        owned(sessionId, email);
        Map<String,List<MarketingKpiMeasurement>> grouped = new LinkedHashMap<>();
        measurements.findBySessionIdOrderByMeasuredAtAsc(sessionId).forEach(item -> grouped.computeIfAbsent(item.getKpiName(), ignored -> new ArrayList<>()).add(item));
        List<MarketingKpiDashboard.Series> series = grouped.entrySet().stream().map(entry -> {
            List<MarketingKpiMeasurement> values = entry.getValue();
            String trend = trend(values);
            return new MarketingKpiDashboard.Series(entry.getKey(), values.getLast().getUnit(), values.stream().map(value -> new MarketingKpiDashboard.Point(value.getValue(), value.getMeasuredAt())).toList(), trend);
        }).toList();
        String comment = series.isEmpty() ? "Ajoutez une première mesure pour démarrer le suivi."
                : series.stream().allMatch(item -> item.points().size() < 2) ? "Une seconde mesure est nécessaire pour commenter une tendance réelle."
                : "Tendances calculées uniquement à partir des valeurs enregistrées : " + series.stream().map(item -> item.name()+" "+item.trend().toLowerCase()).reduce((a,b)->a+", "+b).orElse("") + ".";
        String priority = priority(series);
        return new MarketingKpiDashboard(series, comment, priority);
    }

    private String trend(List<MarketingKpiMeasurement> values) {
        if (values.size() < 2) return "INSUFFICIENT_DATA";
        int comparison = values.getLast().getValue().compareTo(values.get(values.size()-2).getValue());
        return comparison > 0 ? "UP" : comparison < 0 ? "DOWN" : "STABLE";
    }
    private String priority(List<MarketingKpiDashboard.Series> series) {
        return series.stream().filter(item -> "DOWN".equals(item.trend())).findFirst()
                .map(item -> "Priorité proposée : analyser la baisse de « "+item.name()+" » et vérifier l'action ou le canal associé avant d'augmenter le budget.")
                .orElseGet(() -> series.isEmpty() ? "Priorité proposée : enregistrer votre première mesure KPI."
                        : series.stream().anyMatch(item -> "INSUFFICIENT_DATA".equals(item.trend())) ? "Priorité proposée : ajouter une nouvelle mesure comparable pour établir une tendance."
                        : "Priorité proposée : consolider le KPI le plus proche de votre objectif et tester une seule optimisation mesurable.");
    }
    private MarketingSession owned(String id, String email) { return sessions.findByIdAndUserEmail(id,email).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Session Marketing introuvable")); }
}
