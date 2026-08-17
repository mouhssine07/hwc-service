package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MarketingChannelAnswerNormalizer {

    private static final Map<String, String> CHANNELS = new LinkedHashMap<>();

    static {
        CHANNELS.put("instagram", "Instagram");
        CHANNELS.put("facebook", "Facebook");
        CHANNELS.put("whatsapp", "WhatsApp");
        CHANNELS.put("linkedin", "LinkedIn");
        CHANNELS.put("tiktok", "TikTok");
        CHANNELS.put("google ads", "Google Ads");
        CHANNELS.put("google business", "Google Business Profile");
        CHANNELS.put("seo", "SEO");
        CHANNELS.put("email", "Email marketing");
        CHANNELS.put("newsletter", "Email marketing");
        CHANNELS.put("site web", "Site web");
        CHANNELS.put("partenariat", "Partenariats");
    }

    public MarketingSessionState normalize(MarketingSessionState state, String userMessage) {
        if (state.getStage() != MarketingStrategyStage.CHANNEL_SELECTION
                || userMessage == null || userMessage.isBlank()) {
            return state;
        }
        String normalized = normalizeText(userMessage);
        List<Map<String, Object>> selected = new ArrayList<>(state.getRecommendedChannels());
        List<String> existingNames = selected.stream()
                .map(channel -> normalizeText(String.valueOf(channel.getOrDefault("name", ""))))
                .toList();

        CHANNELS.forEach((token, displayName) -> {
            if (normalized.contains(token) && !existingNames.contains(normalizeText(displayName))) {
                Map<String, Object> channel = new LinkedHashMap<>();
                channel.put("name", displayName);
                channel.put("justification", "Canal que le client indique pouvoir exploiter");
                selected.add(channel);
            }
        });
        if (!selected.isEmpty()) state.setRecommendedChannels(selected);
        return state;
    }

    private String normalizeText(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
