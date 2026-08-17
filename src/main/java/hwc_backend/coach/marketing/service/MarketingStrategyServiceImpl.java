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
import hwc_backend.coach.marketing.model.MarketingImageAnnotation;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class MarketingStrategyServiceImpl implements MarketingStrategyService {

    private static final Logger log = LoggerFactory.getLogger(MarketingStrategyServiceImpl.class);
    private static final int FOLLOW_UP_MEMORY_LIMIT = 12;

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
    private final MarketingAnswerDepthAnalyzer answerDepthAnalyzer;
    private final MarketingRichAnswerNormalizer richAnswerNormalizer;
    private final MarketingFollowUpService followUpService;
    private final MarketingPublicPresenceService publicPresenceService;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

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
        return processMessageInternal(sessionId, email, message, image, null);
    }

    @Override
    @Transactional
    public MarketingMessageResponse processMessageStreaming(
            String sessionId, String email, String message, Consumer<String> tokenConsumer) {
        return processMessageInternal(sessionId, email, message, null, tokenConsumer);
    }

    private MarketingMessageResponse processMessageInternal(
            String sessionId, String email, String message, MarketingImageInput image, Consumer<String> tokenConsumer) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Le message est obligatoire");
        }
        MarketingSessionState previous = sessionService.getState(sessionId, email);
        String previousCompanyName = previous.getCompany().getName();
        if (previous.getStage() == MarketingStrategyStage.COMPLETED) {
            return processCompletedFollowUp(sessionId, email, message, image, previous, tokenConsumer);
        }
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.USER, message, null,
                image == null ? null : image.dataUrl(), image == null ? null : image.fileName());

        if (image != null) {
            return analyzeMarketingImage(sessionId, email, message, image, previous);
        }

        if (conversationGuard.isNonInformative(message)) {
            String reply = conversationGuard.reply(previous, message);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, reply, null);
            return response(previous, reply, conversationGuard.suggestedReplies(previous));
        }

        if (conversationGuard.asksAboutKnownState(message)) {
            String reply = conversationGuard.reply(previous, message);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, reply, null);
            return response(previous, reply, conversationGuard.suggestedReplies(previous));
        }

        MarketingAnswerDepthAnalyzer.Depth answerDepth = answerDepthAnalyzer.analyze(message);
        if (answerDepth == MarketingAnswerDepthAnalyzer.Depth.DETAILED) {
            // Extract explicit facts before calling the provider. They must survive
            // an unavailable model or an invalid structured response.
            previous = richAnswerNormalizer.normalize(previous, message);
        }

        boolean inputProtected = conversationGuard.isNonInformative(message)
                || conversationGuard.looksLikeQuestion(message);
        List<MarketingKnowledgeChunk> chunks;
        GeneratedTurn generated;
        try {
            chunks = ragService.retrieve(previous.getStage() + " " + message);
            String prompt = promptService.buildNextQuestionPrompt(previous, write(previous), message.trim(), chunks,
                    answerDepth);
            if (image != null) prompt += "\n\nUne image client est jointe. Analyse uniquement les éléments visibles utiles à la stratégie, signale toute incertitude et n'invente aucun détail.";
            generated = extractGeneratedTurn(previous, languageModelService.generateJson(
                    promptService.systemPrompt(), prompt, image), inputProtected, message);
        } catch (RuntimeException exception) {
            log.error("Marketing conversation generation fallback sessionId={} stage={}",
                    sessionId, previous.getStage(), exception);
            if (answerDepth == MarketingAnswerDepthAnalyzer.Depth.DETAILED) {
                MarketingStrategyStage initialStage = previous.getStage();
                previous = stageEvaluator.evaluateAfterDetailedAnswer(previous);
                previous = sessionService.saveState(sessionId, email, previous);
                String fallback = companyChangeNotice(previousCompanyName, previous.getCompany().getName())
                        + stageEvaluator.transitionMessage(initialStage, previous);
                sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, fallback, null);
                return response(previous, fallback, generateSuggestions(previous, fallback));
            }
            String fallback = conversationGuard.reply(previous, message);
            String finalReply = renderConversationalReply(previous, message, fallback, tokenConsumer);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, finalReply, null);
            return response(previous, finalReply);
        }
        boolean protectedConversation = inputProtected
                || generated.intent() == MessageIntent.SOCIAL
                || generated.intent() == MessageIntent.OFF_TOPIC
                || generated.intent() == MessageIntent.CLARIFICATION;
        boolean carriesBusinessData = !protectedConversation;
        MarketingSessionState updated = carriesBusinessData ? generated.state() : previous;
        MarketingStrategyStage previousStage = previous.getStage();
        if (carriesBusinessData) {
            if (answerDepth == MarketingAnswerDepthAnalyzer.Depth.DETAILED) {
                updated = richAnswerNormalizer.normalize(updated, message);
            }
            updated = objectiveAnswerNormalizer.normalize(updated, message);
            updated = targetAudienceAnswerNormalizer.normalize(updated, message);
            updated = channelAnswerNormalizer.normalize(updated, message);
            updated = finalStagesAnswerNormalizer.normalize(updated, message);
            updated = answerDepth == MarketingAnswerDepthAnalyzer.Depth.DETAILED
                    ? stageEvaluator.evaluateAfterDetailedAnswer(updated)
                    : stageEvaluator.evaluateAfterAnswer(updated);
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
        reply = companyChangeNotice(previousCompanyName, updated.getCompany().getName()) + reply;
        if (carriesBusinessData) {
            updated = sessionService.saveState(sessionId, email, updated);
            if (previousStage != MarketingStrategyStage.CURRENT_AUDIT
                    && updated.getStage() == MarketingStrategyStage.CURRENT_AUDIT
                    && updated.getPublicWebFindings().isEmpty()) {
                try {
                    updated = publicPresenceService.research(sessionId, email);
                    Map<String, Object> proposed = updated.getPublicWebFindings().stream()
                            .filter(finding -> finding.containsKey("proposedPath"))
                            .findFirst().orElse(null);
                    if (proposed != null) {
                        reply = "Avant de poursuivre l’audit, j’ai trouvé cette information publique à confirmer : "
                                + proposed.get("claim") + " Est-ce bien exact ?";
                    }
                } catch (RuntimeException exception) {
                    log.info("Automatic public presence lookup skipped sessionId={} reason={}",
                            sessionId, exception.getMessage());
                }
            }
        }
        List<String> suggestions = generated.suggestedReplies();
        if (conversationGuard.isNonInformative(message)) {
            suggestions = conversationGuard.suggestedReplies(previous);
        }
        if (carriesBusinessData) {
            suggestions = generateSuggestions(updated, reply);
        }
        reply = renderConversationalReply(updated, message, reply, tokenConsumer);
        String annotationsJson = image != null && !generated.imageAnnotations().isEmpty()
                ? write(generated.imageAnnotations()) : null;
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT,
                reply, write(chunks), null, null, annotationsJson);

        log.info("Marketing workflow processed sessionId={} previousStage={} activeStage={} missing={}",
                sessionId, previousStage, updated.getStage(), updated.getMissingInformation());
        return response(updated, reply, safeSuggestions(suggestions), generated.imageAnnotations());
    }

    private MarketingMessageResponse analyzeMarketingImage(
            String sessionId, String email, String message, MarketingImageInput image,
            MarketingSessionState state) {
        String prompt = """
                Tu réalises un audit visuel Marketing de l'image jointe. N'utilise que ce qui est réellement visible.
                Analyse successivement : 1) type de support et objectif apparent, 2) hiérarchie et lisibilité,
                3) clarté de l'offre et du bénéfice, 4) preuves et éléments de confiance, 5) appel à l'action,
                6) cohérence avec la stratégie validée ci-dessous. Cite des éléments précis visibles. Signale clairement
                ce qui est illisible ou incertain. Donne 2 à 4 améliorations concrètes et prioritaires.

                Retourne uniquement ce JSON :
                {"intent":"ANSWER","updatedState":{},"reply":"audit détaillé en français",
                 "suggestedReplies":["action utile 1","action utile 2"],
                 "imageAnnotations":[{"x":0.0,"y":0.0,"width":0.2,"height":0.1,
                 "label":"constat court","confidence":0.8}]}
                Les coordonnées sont normalisées entre 0 et 1. Ajoute une annotation seulement si la zone est
                localisable avec confiance. Ne modifie jamais l'état ni l'étape du workflow.

                DEMANDE CLIENT:
                %s

                ETAT MARKETING VALIDÉ:
                %s
                """.formatted(message, write(state));
        try {
            GeneratedTurn generated = extractGeneratedTurn(state,
                    languageModelService.generateJson(promptService.systemPrompt(), prompt, image), true, message);
            String reply = generated.reply().isBlank()
                    ? "Je vois bien l'image, mais je ne peux pas en tirer un constat visuel suffisamment fiable."
                    : generated.reply();
            if (claimsImageIsMissing(reply)) {
                throw new IllegalStateException("Le modèle multimodal n'a pas reçu l'image jointe");
            }
            reply += "\n\nPour reprendre la stratégie : " + stageEvaluator.fallbackQuestion(state);
            String annotationsJson = generated.imageAnnotations().isEmpty() ? null : write(generated.imageAnnotations());
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT,
                    reply, null, null, null, annotationsJson);
            return response(state, reply, safeSuggestions(generated.suggestedReplies()), generated.imageAnnotations());
        } catch (RuntimeException exception) {
            log.error("Marketing visual audit failed sessionId={}", sessionId, exception);
            String reply = "L'image a bien été reçue, mais son analyse visuelle n'a pas pu être terminée. "
                    + "Aucune information de votre stratégie n'a été modifiée. Pour reprendre : "
                    + stageEvaluator.fallbackQuestion(state);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, reply, null);
            return response(state, reply);
        }
    }

    private boolean claimsImageIsMissing(String reply) {
        String normalized = java.text.Normalizer.normalize(reply, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("aucune image") || normalized.contains("pas d image")
                || normalized.contains("image non jointe") || normalized.contains("ne vois pas l image")
                || normalized.contains("cannot see the image") || normalized.contains("no image attached");
    }

    private MarketingMessageResponse processCompletedFollowUp(
            String sessionId, String email, String message, MarketingImageInput image, MarketingSessionState state,
            Consumer<String> tokenConsumer) {
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.USER, message, null,
                image == null ? null : image.dataUrl(), image == null ? null : image.fileName());
        List<MarketingKnowledgeChunk> chunks;
        try {
            chunks = ragService.retrieve("SUIVI STRATEGIE FINALISEE " + message);
        } catch (RuntimeException exception) {
            log.warn("Marketing follow-up RAG fallback sessionId={} reason={}", sessionId, exception.getMessage());
            chunks = List.of();
        }
        var dashboard = followUpService.dashboard(sessionId, email);
        String followUpContext = write(dashboard);
        var recentMessages = new java.util.ArrayList<>(sessionService
                .getRecentFollowUpMessages(sessionId, email, FOLLOW_UP_MEMORY_LIMIT + 1));
        if (!recentMessages.isEmpty()) {
            var newest = recentMessages.getLast();
            if (newest.getRole() == MarketingSessionMessage.Role.USER
                    && newest.getContent().equals(message.trim())) {
                recentMessages.removeLast();
            }
        }
        if (recentMessages.size() > FOLLOW_UP_MEMORY_LIMIT) {
            recentMessages = new java.util.ArrayList<>(recentMessages
                    .subList(recentMessages.size() - FOLLOW_UP_MEMORY_LIMIT, recentMessages.size()));
        }
        String conversationMemory = write(recentMessages
                .stream()
                .map(item -> Map.of(
                        "role", item.getRole().name().toLowerCase(),
                        "content", item.getContent()))
                .toList());
        String followUpPrompt = """
                La stratégie Marketing de ce client est finalisée. Tu es maintenant son Coach de suivi continu.
                Aide-le à exécuter le plan, analyser ses résultats, ajuster ses actions et choisir la prochaine
                priorité à partir de l'état validé et du contexte RAG. Ne modifie pas l'état et n'invente aucune
                donnée. Si une image est jointe, analyse uniquement les éléments visibles utiles et signale les
                incertitudes. Retourne uniquement un JSON valide :
                {"intent":"ANSWER","updatedState":{},"reply":"réponse utile et prochaine action ou question","suggestedReplies":[]}

                ETAT FINALISE:
                %s

                ETAT REEL DU SUIVI (actions, echeances, statuts, KPI et priorite):
                %s

                MEMOIRE RECENTE DE LA CONVERSATION (maximum %d messages, du plus ancien au plus recent):
                %s

                MESSAGE CLIENT:
                %s

                CONTEXTE RAG:
                %s
                """.formatted(write(state), followUpContext, FOLLOW_UP_MEMORY_LIMIT,
                conversationMemory, message, write(chunks));
        GeneratedTurn generated;
        try {
            generated = extractGeneratedTurn(state, languageModelService.generateJson(
                    promptService.systemPrompt(), followUpPrompt, image), true, message);
        } catch (RuntimeException exception) {
            log.warn("Marketing follow-up provider fallback sessionId={} reason={}", sessionId, exception.getMessage());
            String fallback = localFollowUpReply(message, dashboard);
            sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, fallback, write(chunks));
            return response(state, fallback, List.of(
                    "Quelle action dois-je faire en premier ?",
                    "Comment évoluent mes KPI ?"));
        }
        String reply = generated.reply().isBlank()
                ? "Je poursuis le suivi de votre stratégie. Quelle action ou quel résultat souhaitez-vous analyser ?"
                : generated.reply();
        reply = renderConversationalReply(state, message, reply, tokenConsumer);
        String annotationsJson = image != null && !generated.imageAnnotations().isEmpty()
                ? write(generated.imageAnnotations()) : null;
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT,
                reply, write(chunks), null, null, annotationsJson);
        return response(state, reply, safeSuggestions(generated.suggestedReplies()), generated.imageAnnotations());
    }

    private String localFollowUpReply(String message,
            hwc_backend.coach.marketing.model.MarketingFollowUpDashboard dashboard) {
        String normalized = java.text.Normalizer.normalize(message, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(java.util.Locale.ROOT);
        if (normalized.contains("kpi") || normalized.contains("resultat") || normalized.contains("evolution")) {
            var kpis = dashboard.kpis();
            if (kpis == null || kpis.series() == null || kpis.series().isEmpty()) {
                return "Vous n’avez pas encore de mesure KPI. Ajoutez une première valeur dans le tableau de suivi afin que je puisse analyser une tendance réelle.";
            }
            String trends = kpis.series().stream()
                    .map(series -> series.name() + " : tendance " + series.trend())
                    .collect(java.util.stream.Collectors.joining(" ; "));
            return "D’après vos mesures enregistrées, " + trends + ". "
                    + (kpis.comment() == null ? kpis.nextPriority() : kpis.comment());
        }
        var nextAction = dashboard.actions() == null ? java.util.Optional.<hwc_backend.coach.marketing.model.MarketingActionItemView>empty()
                : dashboard.actions().stream().filter(action -> !"COMPLETED".equalsIgnoreCase(action.status())).findFirst();
        if (nextAction.isPresent()) {
            var action = nextAction.get();
            return "Votre prochaine action concrète est : « " + action.title() + " »"
                    + (action.dueDate() == null ? "." : ", à réaliser avant le " + action.dueDate() + ".")
                    + " Une fois réalisée, marquez-la terminée et ajoutez une preuve si possible.";
        }
        return dashboard.immediatePriority() == null || dashboard.immediatePriority().isBlank()
                ? "Votre plan est à jour. Ajoutez une nouvelle mesure KPI pour identifier la prochaine optimisation."
                : dashboard.immediatePriority();
    }

    private String renderConversationalReply(
            MarketingSessionState state, String userMessage, String approvedDraft, Consumer<String> tokenConsumer) {
        if (tokenConsumer == null) return approvedDraft;
        String prompt = """
                Rédige uniquement le message conversationnel final du Coach, sans JSON ni préambule.
                Le backend a déjà validé les faits, l'étape et la prochaine question dans le brouillon ci-dessous.
                Préserve strictement son sens, ses faits et son unique question principale. N'ajoute aucune donnée,
                recommandation ou question. Améliore seulement la fluidité et adapte le registre au toneProfile.

                toneProfile: %s
                message client: %s
                brouillon validé: %s
                """.formatted(state.getToneProfile(), userMessage, approvedDraft);
        java.util.concurrent.atomic.AtomicBoolean emitted = new java.util.concurrent.atomic.AtomicBoolean(false);
        Consumer<String> trackedConsumer = token -> {
            emitted.set(true);
            tokenConsumer.accept(token);
        };
        try {
            return languageModelService.streamText(promptService.systemPrompt(), prompt, trackedConsumer).trim();
        } catch (RuntimeException exception) {
            log.warn("Native Marketing text streaming fallback sessionId={} reason={}",
                    state.getSessionId(), exception.getMessage());
            // A provider can close its stream after a few tokens. Keep the already
            // validated draft and let the final SSE event replace the partial text.
            if (!emitted.get()) emitFallback(approvedDraft, tokenConsumer);
            return approvedDraft;
        }
    }

    private void emitFallback(String text, Consumer<String> tokenConsumer) {
        for (String token : text.split("(?<=\\s)|(?=\\s)")) tokenConsumer.accept(token);
    }

    private String companyChangeNotice(String oldName, String newName) {
        if (oldName == null || oldName.isBlank() || newName == null || newName.isBlank()
                || canonicalCompanyName(oldName).equals(canonicalCompanyName(newName))) return "";
        return "Vous avez présenté « " + newName + " », alors que cette session mentionnait auparavant « "
                + oldName + " ». J'utilise désormais « " + newName
                + " » pour cette stratégie ; vous pouvez corriger ce choix dans le panneau. ";
    }

    private String canonicalCompanyName(String value) {
        return java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
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
            List<MarketingImageAnnotation> annotations = new java.util.ArrayList<>();
            root.path("imageAnnotations").forEach(item -> {
                MarketingImageAnnotation annotation = new MarketingImageAnnotation(
                        item.path("x").asDouble(-1D), item.path("y").asDouble(-1D),
                        item.path("width").asDouble(-1D), item.path("height").asDouble(-1D),
                        item.path("label").asText(""), item.path("confidence").asDouble(0D));
                if (annotation.isValid() && annotations.size() < 5) annotations.add(annotation);
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
                if (updated.getToneProfile() == null) {
                    updated.setToneProfile(previous.getToneProfile() == null
                            ? MarketingSessionState.ToneProfile.PEDAGOGICAL
                            : previous.getToneProfile());
                }
                tagChangedInformation(previous, updated);
                validateGeneratedState(updated);
            }
            return new GeneratedTurn(updated, reply, intent, List.copyOf(suggestions), List.copyOf(annotations));
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
                || state.getAssumptions() == null || state.getInformationTypes() == null) {
            throw new IllegalArgumentException("Structure d'état incomplète");
        }
    }

    private void tagChangedInformation(MarketingSessionState previous, MarketingSessionState updated) {
        if (updated.getInformationTypes() == null) updated.setInformationTypes(new java.util.LinkedHashMap<>());
        JsonNode before = objectMapper.valueToTree(previous);
        JsonNode after = objectMapper.valueToTree(updated);
        collectChangedPaths("", before, after, updated);
        if (!updated.getAssumptions().isEmpty()) {
            updated.getInformationTypes().put("assumptions", MarketingSessionState.InformationType.HYPOTHESIS);
        }
    }

    private void collectChangedPaths(
            String path, JsonNode before, JsonNode after, MarketingSessionState state) {
        if (after == null || after.isNull()) return;
        if (after.isObject()) {
            after.fields().forEachRemaining(entry -> {
                String childPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
                if (!childPath.equals("informationTypes") && !childPath.startsWith("informationTypes.")) {
                    collectChangedPaths(childPath, before == null ? null : before.get(entry.getKey()),
                            entry.getValue(), state);
                }
            });
            return;
        }
        if (java.util.Objects.equals(before, after) || isProtectedStatePath(path)) return;
        MarketingSessionState.InformationType fallback = path.equals("assumptions")
                ? MarketingSessionState.InformationType.HYPOTHESIS
                : path.equals("recommendedChannels") || path.equals("weeklyActions") || path.equals("kpis")
                ? MarketingSessionState.InformationType.RECOMMENDATION
                : MarketingSessionState.InformationType.DATA;
        state.getInformationTypes().putIfAbsent(path, fallback);
    }

    private boolean isProtectedStatePath(String path) {
        return java.util.Set.of("serviceId", "sessionId", "stage", "completed", "confidence",
                "missingInformation", "toneProfile").contains(path);
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
            String sector = state.getCompany().getSector() == null ? "secteur non défini" : state.getCompany().getSector();
            List<MarketingKnowledgeChunk> suggestionChunks = ragService.retrieve(
                    state.getStage() + " cas pratiques " + sector + " " + question);
            String json = languageModelService.generateJson(
                    promptService.systemPrompt(),
                    promptService.buildSuggestedRepliesPrompt(state, question, suggestionChunks));
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
            List<String> suggestedReplies,
            List<MarketingImageAnnotation> imageAnnotations
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
        return response(state, message, suggestions, List.of());
    }

    private MarketingMessageResponse response(
            MarketingSessionState state, String message, List<String> suggestions,
            List<MarketingImageAnnotation> annotations) {
        return new MarketingMessageResponse(
                state.getSessionId(), state.getStage(), message,
                List.copyOf(state.getMissingInformation()), state.getConfidence(),
                stageEvaluator.canFinalize(state), suggestions, annotations
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
