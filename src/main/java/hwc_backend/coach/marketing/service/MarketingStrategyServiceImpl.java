package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import hwc_backend.coach.marketing.model.MarketingMessageResponse;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.model.MarketingImageInput;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingStrategyServiceImpl implements MarketingStrategyService {

    private static final Logger log = LoggerFactory.getLogger(MarketingStrategyServiceImpl.class);

    private final MarketingSessionService sessionService;
    private final MarketingRagService ragService;
    private final MarketingPromptService promptService;
    private final MarketingLanguageModelService languageModelService;
    private final MarketingStageEvaluator stageEvaluator;
    private final MarketingObjectiveAnswerNormalizer objectiveAnswerNormalizer;
    private final MarketingTargetAudienceAnswerNormalizer targetAudienceAnswerNormalizer;
    private final MarketingChannelAnswerNormalizer channelAnswerNormalizer;
    private final MarketingFinalStagesAnswerNormalizer finalStagesAnswerNormalizer;
    private final MarketingConversationGuard conversationGuard;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public MarketingMessageResponse startSession(String email) {
        MarketingSessionState state = sessionService.createSession(email);
        state.setMissingInformation(stageEvaluator.missingFor(state.getStage(), state));
        state = sessionService.saveState(state.getSessionId(), email, state);
        String question = stageEvaluator.fallbackQuestion(state);
        List<String> suggestions = List.of();
        try {
            List<MarketingKnowledgeChunk> chunks = ragService.retrieve(state.getStage() + " démarrage accompagnement");
            GeneratedTurn opening = extractGeneratedTurn(state, languageModelService.generateJson(
                    promptService.systemPrompt(),
                    promptService.buildOpeningPrompt(state, write(state), chunks)), true, "");
            if (!opening.reply().isBlank()) question = opening.reply();
            suggestions = safeSuggestions(opening.suggestedReplies());
        } catch (RuntimeException exception) {
            log.warn("Marketing opening generation fallback sessionId={} reason={}",
                    state.getSessionId(), exception.getMessage());
        }
        sessionService.appendMessage(state.getSessionId(), email, MarketingSessionMessage.Role.ASSISTANT, question, null);
        return response(state, question, suggestions);
    }

    @Override
    @Transactional
    public MarketingMessageResponse processMessage(String sessionId, String email, String message) {
        return processMessage(sessionId, email, message, null);
    }

    @Override
    @Transactional
    public MarketingMessageResponse processMessage(String sessionId, String email, String message, MarketingImageInput image) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Le message est obligatoire");
        }
        MarketingSessionState previous = sessionService.getState(sessionId, email);
        if (previous.getStage() == MarketingStrategyStage.COMPLETED) {
            return processCompletedFollowUp(sessionId, email, message, image, previous);
        }
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.USER, message, null,
                image == null ? null : image.dataUrl(), image == null ? null : image.fileName());

        boolean inputProtected = conversationGuard.isNonInformative(message)
                || conversationGuard.looksLikeQuestion(message);
        List<MarketingKnowledgeChunk> chunks;
        GeneratedTurn generated;
        try {
            chunks = ragService.retrieve(previous.getStage() + " " + message);
            String prompt = promptService.buildNextQuestionPrompt(previous, write(previous), message.trim(), chunks);
            if (image != null) prompt += "\n\nUne image client est jointe. Analyse uniquement les éléments visibles utiles à la stratégie, signale toute incertitude et n'invente aucun détail.";
            generated = extractGeneratedTurn(previous, languageModelService.generateJson(
                    promptService.systemPrompt(), prompt, image), inputProtected, message);
        } catch (RuntimeException exception) {
            log.error("Marketing conversation generation fallback sessionId={} stage={}",
                    sessionId, previous.getStage(), exception);
            String fallback = conversationGuard.reply(previous, message);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, fallback, null);
            return response(previous, fallback);
        }
        boolean protectedConversation = inputProtected
                || generated.intent() == MessageIntent.SOCIAL
                || generated.intent() == MessageIntent.OFF_TOPIC
                || generated.intent() == MessageIntent.CLARIFICATION;
        boolean carriesBusinessData = !protectedConversation;
        MarketingSessionState updated = carriesBusinessData ? generated.state() : previous;
        MarketingStrategyStage previousStage = previous.getStage();
        if (carriesBusinessData) {
            updated = objectiveAnswerNormalizer.normalize(updated, message);
            updated = targetAudienceAnswerNormalizer.normalize(updated, message);
            updated = channelAnswerNormalizer.normalize(updated, message);
            updated = finalStagesAnswerNormalizer.normalize(updated, message);
            updated = stageEvaluator.evaluateAfterAnswer(updated);
        }

        String reply = conversationGuard.asksWhetherCoachUnderstands(message)
                ? conversationGuard.reply(previous, message)
                : inputProtected && conversationGuard.isNonInformative(message)
                ? conversationGuard.reply(previous, message)
                : carriesBusinessData
                    ? stageEvaluator.transitionMessage(previousStage, updated)
                    : !generated.reply().isBlank()
                    ? generated.reply()
                    : conversationGuard.reply(previous, message);
        if (carriesBusinessData) {
            updated = sessionService.saveState(sessionId, email, updated);
        }
        List<String> suggestions = generated.suggestedReplies();
        if (carriesBusinessData) {
            suggestions = generateSuggestions(updated, reply);
        }
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT,
                reply, write(chunks));

        log.info("Marketing workflow processed sessionId={} previousStage={} activeStage={} missing={}",
                sessionId, previousStage, updated.getStage(), updated.getMissingInformation());
        return response(updated, reply, safeSuggestions(suggestions));
    }

    private MarketingMessageResponse processCompletedFollowUp(
            String sessionId, String email, String message, MarketingImageInput image, MarketingSessionState state) {
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.USER, message, null,
                image == null ? null : image.dataUrl(), image == null ? null : image.fileName());
        List<MarketingKnowledgeChunk> chunks = ragService.retrieve("SUIVI STRATEGIE FINALISEE " + message);
        String followUpPrompt = """
                La stratégie Marketing de ce client est finalisée. Tu es maintenant son Coach de suivi continu.
                Aide-le à exécuter le plan, analyser ses résultats, ajuster ses actions et choisir la prochaine
                priorité à partir de l'état validé et du contexte RAG. Ne modifie pas l'état et n'invente aucune
                donnée. Si une image est jointe, analyse uniquement les éléments visibles utiles et signale les
                incertitudes. Retourne uniquement un JSON valide :
                {"intent":"ANSWER","updatedState":{},"reply":"réponse utile et prochaine action ou question","suggestedReplies":[]}

                ETAT FINALISE:
                %s

                MESSAGE CLIENT:
                %s

                CONTEXTE RAG:
                %s
                """.formatted(write(state), message, write(chunks));
        GeneratedTurn generated = extractGeneratedTurn(state, languageModelService.generateJson(
                promptService.systemPrompt(), followUpPrompt, image), true, message);
        String reply = generated.reply().isBlank()
                ? "Je poursuis le suivi de votre stratégie. Quelle action ou quel résultat souhaitez-vous analyser ?"
                : generated.reply();
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, reply, write(chunks));
        return response(state, reply, safeSuggestions(generated.suggestedReplies()));
    }

    @Override
    public MarketingSessionState getSession(String sessionId, String email) {
        return sessionService.getState(sessionId, email);
    }

    @Override
    public List<MarketingSessionMessage> getMessages(String sessionId, String email) {
        return sessionService.getMessages(sessionId, email);
    }

    private GeneratedTurn extractGeneratedTurn(
            MarketingSessionState previous,
            String llmJson,
            boolean protectState,
            String userMessage
    ) {
        try {
            JsonNode root = objectMapper.readTree(stripCodeFence(llmJson));
            String reply = root.path("reply").asText("").trim();
            MessageIntent intent;
            try {
                intent = MessageIntent.valueOf(root.path("intent").asText("").trim().toUpperCase());
            } catch (IllegalArgumentException exception) {
                intent = MessageIntent.UNKNOWN;
            }
            List<String> suggestions = new java.util.ArrayList<>();
            root.path("suggestedReplies").forEach(item -> {
                String suggestion = item.asText("").trim();
                if (!suggestion.isBlank() && suggestions.size() < 4) suggestions.add(suggestion);
            });
            MarketingSessionState updated = previous;
            if (!protectState && intent != MessageIntent.SOCIAL && intent != MessageIntent.OFF_TOPIC
                    && intent != MessageIntent.CLARIFICATION) {
                JsonNode patch = root.path("updatedState");
                if (!patch.isObject()) {
                    throw new IllegalArgumentException("updatedState doit être un objet JSON");
                }
                normalizeDottedPaths((ObjectNode) patch);
                removeUnsupportedCategoricalValues((ObjectNode) patch, previous, userMessage);
                ObjectNode merged = objectMapper.valueToTree(previous);
                mergeStatePatch(merged, (ObjectNode) patch, true);
                updated = objectMapper.treeToValue(merged, MarketingSessionState.class);
                updated.setServiceId(previous.getServiceId());
                updated.setSessionId(previous.getSessionId());
                updated.setStage(previous.getStage());
                updated.setCompleted(previous.isCompleted());
                validateGeneratedState(updated);
            }
            return new GeneratedTurn(updated, reply, intent, List.copyOf(suggestions));
        } catch (Exception exception) {
            throw new IllegalStateException("Le Coach n'a pas retourné un état Marketing valide", exception);
        }
    }

    private void normalizeDottedPaths(ObjectNode patch) {
        List<String> dottedKeys = new java.util.ArrayList<>();
        patch.fieldNames().forEachRemaining(key -> {
            if (key.contains(".")) dottedKeys.add(key);
        });
        for (String dottedKey : dottedKeys) {
            JsonNode value = patch.remove(dottedKey);
            String[] parts = dottedKey.split("\\.");
            ObjectNode cursor = patch;
            for (int index = 0; index < parts.length - 1; index++) {
                JsonNode child = cursor.get(parts[index]);
                if (!(child instanceof ObjectNode)) {
                    child = cursor.putObject(parts[index]);
                }
                cursor = (ObjectNode) child;
            }
            cursor.set(parts[parts.length - 1], value);
        }
    }

    private void removeUnsupportedCategoricalValues(
            ObjectNode patch,
            MarketingSessionState previous,
            String userMessage
    ) {
        JsonNode companyNode = patch.path("company");
        if (!(companyNode instanceof ObjectNode company)) return;
        removeUnlessMentioned(company, "size", previous.getCompany().getSize(), userMessage);
        removeBusinessModelUnlessSupported(company, previous.getCompany().getBusinessModel(), userMessage);
        removeUnlessMentioned(company, "location", previous.getCompany().getLocation(), userMessage);
    }

    private void removeBusinessModelUnlessSupported(ObjectNode node, String previousValue, String userMessage) {
        JsonNode candidate = node.get("businessModel");
        if (previousValue != null || candidate == null || candidate.isNull()) return;
        String value = candidate.asText("").trim().toUpperCase();
        String message = normalizeEvidence(userMessage);
        boolean supported = switch (value) {
            case "B2B" -> message.matches(".*\\b(entreprise|entreprises|professionnel|professionnels|societe|societes|b2b)\\b.*");
            case "B2C" -> message.matches(".*\\b(particulier|particuliers|consommateur|consommateurs|grand public|b2c)\\b.*");
            case "B2B2C", "B2B ET B2C", "B2B/B2C" -> message.matches(".*\\b(les deux|entreprises et particuliers|b2b2c|b2b et b2c)\\b.*");
            default -> message.contains(normalizeEvidence(value));
        };
        if (!supported) node.remove("businessModel");
    }

    private void removeUnlessMentioned(ObjectNode node, String field, String previousValue, String userMessage) {
        JsonNode candidate = node.get(field);
        if (previousValue != null || candidate == null || candidate.isNull()) return;
        String value = candidate.asText("").trim();
        String message = normalizeEvidence(userMessage);
        String normalizedValue = normalizeEvidence(value);
        if (normalizedValue.isBlank() || !message.contains(normalizedValue)) node.remove(field);
    }

    private String normalizeEvidence(String value) {
        return java.text.Normalizer.normalize(value == null ? "" : value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

    private void validateGeneratedState(MarketingSessionState state) {
        if (state.getCompany() == null || state.getObjectives() == null || state.getAudit() == null
                || state.getTargetAudience() == null || state.getPositioning() == null
                || state.getBudget() == null || state.getRecommendedChannels() == null
                || state.getWeeklyActions() == null || state.getKpis() == null
                || state.getAssumptions() == null) {
            throw new IllegalArgumentException("Structure d'état incomplète");
        }
    }

    private List<String> safeSuggestions(List<String> values) {
        List<String> safe = values.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank() && value.length() <= 240)
                .filter(value -> !value.toLowerCase().matches(
                        ".*(remplacez|complétez|indiquez|exemple|vos propres|placeholder|\\[.*]|<.*>).*"))
                .distinct()
                .limit(4)
                .toList();
        return safe.size() >= 2 ? safe : List.of();
    }

    private List<String> generateSuggestions(MarketingSessionState state, String question) {
        try {
            String json = languageModelService.generateJson(
                    promptService.systemPrompt(),
                    promptService.buildSuggestedRepliesPrompt(state, question));
            JsonNode root = objectMapper.readTree(stripCodeFence(json));
            List<String> suggestions = new java.util.ArrayList<>();
            root.path("suggestedReplies").forEach(item -> suggestions.add(item.asText("")));
            return safeSuggestions(suggestions);
        } catch (Exception exception) {
            log.warn("Marketing suggestions generation skipped sessionId={} reason={}",
                    state.getSessionId(), exception.getMessage());
            return List.of();
        }
    }

    private record GeneratedTurn(
            MarketingSessionState state,
            String reply,
            MessageIntent intent,
            List<String> suggestedReplies
    ) {
    }

    private enum MessageIntent { ANSWER, QUESTION, CLARIFICATION, SOCIAL, OFF_TOPIC, UNKNOWN }

    private void mergeStatePatch(ObjectNode target, ObjectNode patch, boolean root) {
        patch.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (root && (key.equals("serviceId") || key.equals("sessionId")
                    || key.equals("stage") || key.equals("completed"))) {
                return;
            }
            JsonNode current = target.get(key);
            if (value.isObject() && current != null && current.isObject()) {
                mergeStatePatch((ObjectNode) current, (ObjectNode) value, false);
            } else if (!value.isNull()) {
                target.set(key, value);
            }
        });
    }

    private MarketingMessageResponse response(MarketingSessionState state, String message) {
        return response(state, message, List.of());
    }

    private MarketingMessageResponse response(MarketingSessionState state, String message, List<String> suggestions) {
        return new MarketingMessageResponse(
                state.getSessionId(), state.getStage(), message,
                List.copyOf(state.getMissingInformation()), state.getConfidence(),
                stageEvaluator.canFinalize(state), suggestions
        );
    }

    private String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible de sérialiser le contexte Marketing", exception);
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
