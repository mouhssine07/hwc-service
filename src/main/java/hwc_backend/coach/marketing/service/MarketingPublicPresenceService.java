package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingPublicPresenceService {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "audit.website", "audit.currentChannels", "audit.currentResults");

    private final MarketingSessionService sessionService;
    private final MarketingLanguageModelService languageModelService;
    private final MarketingPromptService promptService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public MarketingSessionState research(String sessionId, String email) {
        MarketingSessionState state = sessionService.getState(sessionId, email);
        String companyName = state.getCompany().getName();
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'entreprise est nécessaire pour la recherche publique");
        }
        String declaredWebsite = extractWebsite(state.getAudit().getWebsite());
        String prompt = """
                Recherche uniquement la présence publique actuelle de l'entreprise ci-dessous dans le contexte de
                son audit Marketing. Retourne un JSON {"findings":[...]} avec au maximum 5 éléments. Chaque élément
                contient sourceTitle, url, claim, confirmationRequired=true et, seulement si la donnée correspond
                précisément à l'audit, proposedPath et proposedValue. proposedPath doit être exclusivement l'une de
                ces valeurs : audit.website, audit.currentChannels, audit.currentResults. Un claim est une observation
                prudente, courte, sans donnée personnelle et jamais présentée comme acquise. Ignore les homonymes et
                ne retourne rien si l'identité de l'entreprise est ambiguë.

                Entreprise: %s
                Secteur: %s
                Localisation: %s
                Site déclaré par le client (ancre d'identité prioritaire): %s
                Si ce site est renseigné, recherche d'abord ce domaine et utilise-le pour distinguer les homonymes.
                """.formatted(companyName, state.getCompany().getSector(), state.getCompany().getLocation(),
                declaredWebsite == null ? "non renseigné" : declaredWebsite);
        String raw = languageModelService.generateJsonWithWebSearch(promptService.systemPrompt(), prompt);
        List<Map<String, Object>> findings = validateFindings(raw);
        if (findings.isEmpty() && declaredWebsite != null) {
            findings = List.of(declaredWebsiteFinding(declaredWebsite));
        }
        state.setPublicWebFindings(findings);
        findings.forEach(finding -> {
            String assumption = "À confirmer — " + finding.get("claim") + " (source : " + finding.get("sourceTitle") + ")";
            if (!state.getAssumptions().contains(assumption)) state.getAssumptions().add(assumption);
        });
        state.getInformationTypes().put("publicWebFindings", MarketingSessionState.InformationType.HYPOTHESIS);
        return sessionService.saveState(sessionId, email, state);
    }

    @Transactional
    public MarketingSessionState decide(String sessionId, String email, int findingIndex, boolean accepted) {
        MarketingSessionState state = sessionService.getState(sessionId, email);
        if (findingIndex < 0 || findingIndex >= state.getPublicWebFindings().size()) {
            throw new IllegalArgumentException("Proposition publique introuvable");
        }
        Map<String, Object> finding = state.getPublicWebFindings().get(findingIndex);
        if (accepted) applyConfirmedFinding(state, finding);
        List<Map<String, Object>> remaining = new ArrayList<>(state.getPublicWebFindings());
        remaining.remove(findingIndex);
        state.setPublicWebFindings(remaining);
        return sessionService.saveState(sessionId, email, state);
    }

    private void applyConfirmedFinding(MarketingSessionState state, Map<String, Object> finding) {
        String path = String.valueOf(finding.getOrDefault("proposedPath", ""));
        if (!ALLOWED_PATHS.contains(path) || !finding.containsKey("proposedValue")) {
            throw new IllegalArgumentException("Cette observation ne contient pas de pré-remplissage confirmable");
        }
        JsonNode value = objectMapper.valueToTree(finding.get("proposedValue"));
        switch (path) {
            case "audit.website" -> state.getAudit().setWebsite(objectMapper.convertValue(value, Object.class));
            case "audit.currentChannels" -> state.getAudit().setCurrentChannels(
                    objectMapper.convertValue(value, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() { }));
            case "audit.currentResults" -> state.getAudit().setCurrentResults(
                    objectMapper.convertValue(value, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() { }));
            default -> throw new IllegalArgumentException("Chemin de pré-remplissage non autorisé");
        }
        state.getInformationTypes().put(path, MarketingSessionState.InformationType.DATA);
    }

    private Map<String, Object> declaredWebsiteFinding(String url) {
        Map<String, Object> finding = new LinkedHashMap<>();
        finding.put("sourceTitle", "Site déclaré par le client");
        finding.put("url", url);
        finding.put("claim", "Cette adresse constitue l'ancre d'identité fournie par le client ; son contenu public reste à confirmer.");
        finding.put("confirmationRequired", true);
        return Map.copyOf(finding);
    }

    private String extractWebsite(Object website) {
        if (website == null) return null;
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("https?://[^\\s,}\\]\\\"]+", java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(website.toString());
        if (!matcher.find()) return null;
        String candidate = matcher.group().replaceAll("[.)]+$", "");
        return isPublicHttpUrl(candidate) ? candidate : null;
    }

    private List<Map<String, Object>> validateFindings(String raw) {
        try {
            String cleaned = raw.replaceFirst("^```(?:json)?\\s*", "").replaceFirst("\\s*```$", "").trim();
            int start = cleaned.indexOf('{');
            int end = cleaned.lastIndexOf('}');
            if (start >= 0 && end > start) cleaned = cleaned.substring(start, end + 1);
            JsonNode root = objectMapper.readTree(cleaned);
            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode item : root.path("findings")) {
                String title = item.path("sourceTitle").asText("").trim();
                String url = item.path("url").asText("").trim();
                String claim = item.path("claim").asText("").trim();
                if (title.isBlank() || claim.isBlank() || !isPublicHttpUrl(url)) continue;
                Map<String, Object> finding = new LinkedHashMap<>();
                finding.put("sourceTitle", title);
                finding.put("url", url);
                finding.put("claim", claim);
                finding.put("confirmationRequired", true);
                String proposedPath = item.path("proposedPath").asText("").trim();
                if (ALLOWED_PATHS.contains(proposedPath) && item.has("proposedValue") && !item.get("proposedValue").isNull()) {
                    finding.put("proposedPath", proposedPath);
                    finding.put("proposedValue", objectMapper.convertValue(item.get("proposedValue"), Object.class));
                }
                result.add(Map.copyOf(finding));
                if (result.size() == 5) break;
            }
            return List.copyOf(result);
        } catch (Exception exception) {
            throw new IllegalStateException("Résultats de recherche publique invalides", exception);
        }
    }

    private boolean isPublicHttpUrl(String value) {
        try {
            URI uri = URI.create(value);
            return ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) && uri.getHost() != null;
        } catch (Exception exception) {
            return false;
        }
    }
}
