package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MarketingTargetAudienceAnswerNormalizer {

    public MarketingSessionState normalize(MarketingSessionState state, String userMessage) {
        if (state.getStage() != MarketingStrategyStage.TARGET_AUDIENCE
                || userMessage == null || userMessage.isBlank()) {
            return state;
        }
        Map<String, Object> persona = state.getTargetAudience().getPrimaryPersona();
        if (persona == null) persona = new LinkedHashMap<>();

        String answer = userMessage.trim();
        boolean hasNeed = hasText(persona, "primaryNeed", "need", "besoin");
        boolean hasObjection = hasText(persona, "mainObjection", "objection", "objectionMajeure");
        String normalized = normalize(answer);

        // A segment identifies who the client is; it must not also be stored as
        // that persona's need. The need is collected by the following question.
        if (!hasNeed && looksLikeSegment(normalized) && !looksLikeNeed(normalized)) {
            return state;
        }

        if (!hasNeed && looksLikeNeed(normalized)) {
            persona.put("primaryNeed", answer);
        } else if (!hasObjection && looksLikeObjection(normalized)) {
            persona.put("mainObjection", answer);
        } else if (!hasNeed) {
            persona.put("primaryNeed", answer);
        } else if (!hasObjection) {
            persona.put("mainObjection", answer);
        }
        state.getTargetAudience().setPrimaryPersona(persona);
        return state;
    }

    static boolean hasText(Map<String, Object> values, String... keys) {
        if (values == null) return false;
        for (String key : keys) {
            Object value = values.get(key);
            if (value != null && !value.toString().isBlank()) return true;
        }
        return false;
    }

    private boolean looksLikeObjection(String value) {
        return value.matches(".*\\b(objection|crainte|peur|hesite|hesitent|frein|cher|prix|cout|mauvaise qualite|peu fiable)\\b.*");
    }

    private boolean looksLikeNeed(String value) {
        return value.matches(".*\\b(besoin|recherche|recherchent|veut|veulent|souhaite|souhaitent|gagner du temps)\\b.*");
    }

    private boolean looksLikeSegment(String value) {
        return value.matches(".*\\b(cibler|cible|proprietaires|vendeurs|acheteurs|entreprises|particuliers|professionnels|clients)\\b.*");
    }

    private String normalize(String value) {
        return java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }
}
