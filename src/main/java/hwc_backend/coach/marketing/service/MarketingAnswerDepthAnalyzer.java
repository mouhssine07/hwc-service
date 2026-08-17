package hwc_backend.coach.marketing.service;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MarketingAnswerDepthAnalyzer {

    private static final Pattern INFORMATION_SIGNAL = Pattern.compile(
            "(?i)(\\d|%|€|dh|mad|casablanca|rabat|b2b|b2c|instagram|facebook|linkedin|site|seo|client|budget|semaine|mois)");

    public Depth analyze(String message) {
        String value = message == null ? "" : message.trim();
        long signals = INFORMATION_SIGNAL.matcher(value).results().count();
        int clauses = value.split("[,;.]|\\R|\\bet\\b").length;
        if (value.length() >= 160 || signals >= 3 || clauses >= 4) return Depth.DETAILED;
        if (value.length() < 45 && signals == 0 && clauses <= 1) return Depth.VAGUE;
        return Depth.STANDARD;
    }

    public enum Depth { VAGUE, STANDARD, DETAILED }
}
