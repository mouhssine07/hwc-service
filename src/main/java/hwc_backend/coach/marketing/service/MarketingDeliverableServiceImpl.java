package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyResult;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingDeliverableServiceImpl implements MarketingDeliverableService {

    private static final Logger log = LoggerFactory.getLogger(MarketingDeliverableServiceImpl.class);
    private static final List<String> REQUIRED_FIELDS = List.of(
            "sessionId", "companySummary", "currentAudit", "smartObjective", "targetAudience",
            "positioning", "priorityChannels", "fourWeekPlan", "budgetAndResources", "kpis",
            "assumptions", "immediatePriority"
    );

    private final MarketingSessionService sessionService;
    private final MarketingStageEvaluator stageEvaluator;
    private final MarketingPromptService promptService;
    private final MarketingLanguageModelService languageModelService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public MarketingStrategyResult finalizeStrategy(String sessionId, String email) {
        MarketingSessionState state = sessionService.getState(sessionId, email);
        if (!stageEvaluator.canFinalize(state)) {
            throw new IllegalStateException("Les informations minimales de la stratégie Marketing sont incomplètes");
        }

        String rawJson = languageModelService.generateJson(
                promptService.systemPrompt(),
                promptService.buildFinalDeliverablePrompt(state, write(state))
        );
        JsonNode result = validateResult(sessionId, rawJson);
        String normalizedJson = pretty(result);
        String markdown = renderMarkdown(result);
        sessionService.saveDeliverable(sessionId, email, normalizedJson, markdown);

        state.setStage(MarketingStrategyStage.COMPLETED);
        state.setCompleted(true);
        state.setMissingInformation(List.of());
        state.setConfidence(MarketingSessionState.Confidence.HIGH);
        sessionService.saveState(sessionId, email, state);

        log.info("Marketing deliverable generated sessionId={} channels={} weeks={} kpis={}",
                sessionId, result.path("priorityChannels").size(), result.path("fourWeekPlan").size(),
                result.path("kpis").size());
        return new MarketingStrategyResult(sessionId, normalizedJson, markdown);
    }

    @Override
    public String getDeliverable(String sessionId, String email) {
        return sessionService.getDeliverableMarkdown(sessionId, email);
    }

    private JsonNode validateResult(String sessionId, String rawJson) {
        try {
            JsonNode result = objectMapper.readTree(stripCodeFence(rawJson));
            if (!result.isObject()) {
                throw new IllegalArgumentException("Le livrable doit être un objet JSON");
            }
            for (String field : REQUIRED_FIELDS) {
                if (!result.has(field) || result.get(field).isNull()) {
                    throw new IllegalArgumentException("Champ du livrable manquant: " + field);
                }
            }
            if (!sessionId.equals(result.path("sessionId").asText())) {
                throw new IllegalArgumentException("Le livrable ne correspond pas à la session");
            }
            if (!result.path("priorityChannels").isArray()
                    || result.path("priorityChannels").isEmpty()
                    || result.path("priorityChannels").size() > 3) {
                throw new IllegalArgumentException("Le livrable doit contenir entre un et trois canaux");
            }
            if (!result.path("fourWeekPlan").isArray() || result.path("fourWeekPlan").size() != 4) {
                throw new IllegalArgumentException("Le plan doit contenir exactement quatre semaines");
            }
            if (!result.path("kpis").isArray() || result.path("kpis").isEmpty()) {
                throw new IllegalArgumentException("Le livrable doit contenir au moins un KPI");
            }
            return result;
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalStateException("Livrable Marketing JSON invalide", exception);
        }
    }

    private String renderMarkdown(JsonNode result) {
        return promptService.deliverableTemplate()
                .replace("{{companySummary}}", text(result, "companySummary"))
                .replace("{{currentAudit}}", text(result, "currentAudit"))
                .replace("{{smartObjective}}", text(result, "smartObjective"))
                .replace("{{targetAudience}}", markdownValue(result.path("targetAudience")))
                .replace("{{positioning}}", markdownValue(result.path("positioning")))
                .replace("{{priorityChannels}}", markdownValue(result.path("priorityChannels")))
                .replace("{{fourWeekPlan}}", markdownValue(result.path("fourWeekPlan")))
                .replace("{{budgetAndResources}}", markdownValue(result.path("budgetAndResources")))
                .replace("{{kpis}}", markdownValue(result.path("kpis")))
                .replace("{{assumptionsAndChecks}}", markdownValue(result.path("assumptions"))
                        + "\n\n" + markdownValue(result.path("pointsToVerify")))
                .replace("{{immediatePriority}}", text(result, "immediatePriority"));
    }

    private String markdownValue(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return "Non renseigné.";
        if (node.isTextual()) return node.asText();
        if (node.isArray()) {
            StringBuilder markdown = new StringBuilder();
            for (JsonNode item : node) {
                markdown.append("- ").append(item.isTextual() ? item.asText() : compact(item)).append("\n");
            }
            return markdown.toString().trim();
        }
        StringBuilder markdown = new StringBuilder();
        node.fields().forEachRemaining(entry -> markdown.append("- **")
                .append(humanize(entry.getKey())).append("** : ")
                .append(entry.getValue().isTextual() ? entry.getValue().asText() : compact(entry.getValue()))
                .append("\n"));
        return markdown.toString().trim();
    }

    private String humanize(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1 $2").replace('_', ' ');
    }

    private String text(JsonNode node, String field) {
        String value = node.path(field).asText();
        return value.isBlank() ? "Non renseigné." : value;
    }

    private String compact(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de rendre le livrable", exception);
        }
    }

    private String pretty(JsonNode node) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de normaliser le livrable", exception);
        }
    }

    private String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de sérialiser l'état Marketing", exception);
        }
    }

    private String stripCodeFence(String value) {
        String cleaned = value == null ? "" : value.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceFirst("^```(?:json)?\\s*", "").replaceFirst("\\s*```$", "");
        }
        return cleaned;
    }
}
