package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/** Extracts only explicit, high-confidence facts from unusually rich client answers. */
@Component
public class MarketingRichAnswerNormalizer {
    private static final Pattern COMPANY = Pattern.compile("(?iu)(?:je g[eè]re|notre (?:entreprise|soci[eé]t[eé]) est|nous sommes)\\s+[\"“]?([^\"”,;]+)");
    private static final Pattern LOCATION = Pattern.compile("(?iu)(?:à|a|situ[eé]e? à|bas[eé]e? à)\\s+(Casablanca|Rabat|Marrakech|Tanger|F[eè]s|Agadir)\\b");
    private static final Pattern BUDGET = Pattern.compile("(?iu)budget[^.]{0,45}?(\\d[\\d .]*)\\s*(MAD|DH|EUR|€)");
    private static final Pattern HOURS = Pattern.compile("(?iu)(\\d+(?:[-–]\\d+)?)\\s*h(?:eures?)?\\s+par\\s+semaine");

    public MarketingSessionState normalize(MarketingSessionState state, String message) {
        if (message == null || message.length() < 120) return state;
        String normalized = normalize(message);
        Matcher company = COMPANY.matcher(message);
        if (company.find()) state.getCompany().setName(company.group(1).trim());
        Matcher location = LOCATION.matcher(message);
        if (location.find()) state.getCompany().setLocation(location.group(1));

        if (describesFoodAndDrinkVenue(normalized)) {
            state.getCompany().setSector("Restauration");
            if (state.getCompany().getProductsOrServices().isEmpty()) {
                String offer = normalized.contains("cuisine mediterraneenne")
                        ? "Restauration de cuisine méditerranéenne"
                        : describesCoffeeAndBrunchOffer(normalized) ? "Café-brunch, boissons et petite restauration"
                        : "Restauration sur place";
                state.getCompany().setProductsOrServices(List.of(offer));
            }
            if (state.getCompany().getBusinessModel() == null) state.getCompany().setBusinessModel("B2C");
        }
        if (state.getCompany().getBusinessModel() == null
                && normalized.matches(".*\\b(particuliers|etudiants|freelances|jeunes actifs|consommateurs)\\b.*")) {
            state.getCompany().setBusinessModel("B2C");
        }
        Matcher capacity = Pattern.compile("(?iu)(?:environ\\s+)?(\\d+)\\s+couverts?").matcher(message);
        if (capacity.find()) state.getCompany().setSize(capacity.group(1) + " couverts par service en moyenne");

        var originalStage = state.getStage();
        state.setStage(hwc_backend.coach.marketing.model.MarketingStrategyStage.OBJECTIVES);
        new MarketingObjectiveAnswerNormalizer().normalize(state, message);
        state.setStage(originalStage);
        if (normalized.contains("pas de site web") || normalized.contains("aucun site web")) state.getAudit().setWebsite(false);
        else extractUrl(message).ifPresent(url -> state.getAudit().setWebsite(Map.of("exists", true, "url", url)));
        if (normalized.contains("pas de suivi") || normalized.contains("sans suivre")
                || normalized.contains("ne mesurons pas") || normalized.contains("sans mesure")) state.getAudit().setTrackingAvailable(false);
        List<String> channels = new ArrayList<>(state.getAudit().getCurrentChannels());
        for (String channel : List.of("Instagram", "Facebook", "LinkedIn", "TikTok", "Google Business")) {
            if (normalized.contains(normalize(channel)) && !channels.contains(channel)) channels.add(channel);
        }
        state.getAudit().setCurrentChannels(channels);

        Matcher target = Pattern.compile("(?iu)(?:cible principale|ciblant surtout|cibler en priorit[eé])\\s*:?\\s*([^.;]+)").matcher(message);
        if (target.find()) state.getTargetAudience().setSegments(List.of(target.group(1).trim()));
        Matcher positioning = Pattern.compile("(?iu)(?:positionnement[^.:]*[:;,]?|me diff[eé]rencier par)\\s*([^.;]+)").matcher(message);
        if (positioning.find()) state.getPositioning().setValueProposition(positioning.group(1).trim());

        Matcher budget = BUDGET.matcher(message);
        if (budget.find()) {
            state.getBudget().setMonthlyAmount(new BigDecimal(budget.group(1).replace(" ", "")));
            state.getBudget().setCurrency(budget.group(2).equalsIgnoreCase("DH") ? "MAD" : budget.group(2));
        }
        Matcher hours = HOURS.matcher(message);
        if (hours.find()) {
            String range = hours.group(1);
            String upper = range.contains("-") || range.contains("–") ? range.split("[-–]")[1] : range;
            state.getBudget().setWeeklyTimeHours(new BigDecimal(upper));
        }
        if (normalized.contains("sans employe dedie")) state.getBudget().setTeamResources(List.of("Gestion par le dirigeant, sans employé dédié"));

        Matcher kpis = Pattern.compile("(?iu)(?:comme KPI|suivre)\\s*[:,]?\\s*([^.;]+)").matcher(message);
        if (kpis.find() && state.getKpis().isEmpty()) state.setKpis(List.of(Map.of("name", kpis.group(1).trim())));
        return state;
    }

    private java.util.Optional<String> extractUrl(String message) {
        Matcher matcher = Pattern.compile("https?://[^\\s,]+", Pattern.CASE_INSENSITIVE).matcher(message);
        return matcher.find() ? java.util.Optional.of(matcher.group().replaceAll("[.)]+$", "")) : java.util.Optional.empty();
    }

    private boolean describesFoodAndDrinkVenue(String value) {
        if (containsAny(value, "restaurant", "cafe", "brunch", "patisserie", "snack", "salon de the")) return true;
        boolean venue = containsAny(value, "endroit", "lieu", "place", "espace", "local", "etablissement");
        boolean consumption = containsAny(value, "prendre une boisson", "boire", "manger", "dejeuner", "diner",
                "petit dejeuner", "repas", "plats", "boissons", "gateaux", "viennoiseries");
        boolean hospitality = containsAny(value, "clients", "servir", "commander", "sur place", "table", "menu");
        return consumption && (venue || hospitality);
    }

    private boolean describesCoffeeAndBrunchOffer(String value) {
        return containsAny(value, "brunch", "cafe", "boisson", "petit dejeuner", "patisserie",
                "gateau", "viennoiserie", "the", "jus");
    }

    private boolean containsAny(String value, String... signals) {
        for (String signal : signals) if (value.contains(signal)) return true;
        return false;
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }
}
