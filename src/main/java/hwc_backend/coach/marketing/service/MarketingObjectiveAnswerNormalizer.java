package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MarketingObjectiveAnswerNormalizer {

    private static final Pattern TARGET = Pattern.compile(
            "(?iu)(\\d+(?:[.,]\\d+)?)\\s*(%|nouveaux?\\s+clients?|clients?|prospects?|leads?|ventes?|commandes?|reservations?|visites?|abonnes?)");
    private static final Pattern DEADLINE = Pattern.compile(
            "(?iu)(?:d['’]ici|dans|sous|en)\\s+(\\d+)\\s+(jour|jours|semaine|semaines|mois|an|ans|annee|annees)");

    public MarketingSessionState normalize(MarketingSessionState state, String userMessage) {
        if (state.getStage() != MarketingStrategyStage.OBJECTIVES || userMessage == null || userMessage.isBlank()) {
            return state;
        }
        MarketingSessionState.Objectives objectives = state.getObjectives();
        Matcher target = TARGET.matcher(userMessage);
        Matcher deadline = DEADLINE.matcher(userMessage);
        if (objectives.getTargetValue() == null && target.find()) objectives.setTargetValue(target.group().trim());
        if ((objectives.getDeadline() == null || objectives.getDeadline().isBlank()) && deadline.find()) {
            objectives.setDeadline(deadline.group().trim());
        }
        if (objectives.getType() == null || objectives.getType().isBlank()) objectives.setType(inferType(userMessage));
        if (objectives.getTargetValue() != null
                && objectives.getDeadline() != null && !objectives.getDeadline().isBlank()
                && (objectives.getSmartStatement() == null || objectives.getSmartStatement().isBlank())) {
            objectives.setSmartStatement(userMessage.trim());
        }
        return state;
    }

    private String inferType(String message) {
        String normalized = Normalizer.normalize(message, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        if (normalized.matches(".*\\b(client|clients|prospect|prospects|lead|leads)\\b.*")) return "ACQUISITION_CLIENTS";
        if (normalized.matches(".*\\b(vente|ventes|commande|commandes|chiffre d affaires|ca)\\b.*")) return "CROISSANCE_VENTES";
        if (normalized.matches(".*\\b(visite|visites|trafic|audience)\\b.*")) return "TRAFIC";
        if (normalized.matches(".*\\b(reservation|reservations|rendez vous)\\b.*")) return "CONVERSIONS";
        return null;
    }
}
