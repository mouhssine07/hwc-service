package hwc_backend.coach.marketing.controller;

import hwc_backend.coach.marketing.model.MarketingMessageRequest;
import hwc_backend.coach.marketing.model.MarketingMessageResponse;
import hwc_backend.coach.marketing.model.MarketingConversationMessage;
import hwc_backend.coach.marketing.model.MarketingSessionCreateRequest;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyResult;
import hwc_backend.coach.marketing.model.MarketingSessionSummary;
import hwc_backend.coach.marketing.model.MarketingImageInput;
import hwc_backend.coach.marketing.service.MarketingSessionService;
import hwc_backend.coach.marketing.service.MarketingDeliverableService;
import hwc_backend.coach.marketing.service.MarketingStrategyService;
import hwc_backend.coach.marketing.service.MarketingPublicPresenceService;
import hwc_backend.coach.marketing.service.MarketingKpiService;
import hwc_backend.coach.marketing.service.MarketingCheckInService;
import hwc_backend.coach.marketing.service.MarketingActionPlanService;
import hwc_backend.coach.marketing.service.MarketingFollowUpService;
import hwc_backend.coach.marketing.service.MarketingProactivePriorityService;
import hwc_backend.coach.marketing.service.MarketingInteractionService;
import hwc_backend.coach.marketing.model.MarketingInputSpec;
import hwc_backend.coach.marketing.model.MarketingReactionRequest;
import hwc_backend.coach.marketing.model.MarketingReactionResponse;
import hwc_backend.coach.marketing.model.MarketingKpiMeasurementRequest;
import hwc_backend.coach.marketing.model.MarketingKpiDashboard;
import hwc_backend.coach.marketing.model.MarketingActionItemView;
import hwc_backend.coach.marketing.model.MarketingActionUpdateRequest;
import hwc_backend.coach.marketing.model.MarketingFollowUpDashboard;
import hwc_backend.coach.marketing.model.MarketingProactivePriority;
import hwc_backend.coach.marketing.model.MarketingPublicFindingDecisionRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import hwc_backend.coach.marketing.model.MarketingStateCorrectionRequest;
import hwc_backend.coach.marketing.model.MarketingStateCorrectionView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.CompletableFuture;
import java.util.Base64;
import java.util.Set;
import hwc_backend.coach.marketing.model.MarketingImageAnnotation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/client/coach/marketing/sessions")
@RequiredArgsConstructor
public class MarketingStrategyController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MarketingStrategyService strategyService;
    private final MarketingDeliverableService deliverableService;
    private final MarketingSessionService sessionService;
    private final MarketingPublicPresenceService publicPresenceService;
    private final MarketingKpiService kpiService;
    private final MarketingCheckInService checkInService;
    private final MarketingActionPlanService actionPlanService;
    private final MarketingFollowUpService followUpService;
    private final MarketingProactivePriorityService proactivePriorityService;
    private final MarketingInteractionService interactionService;

    @GetMapping("/{sessionId}/input-spec")
    public ResponseEntity<MarketingInputSpec> inputSpec(@PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(interactionService.inputSpec(sessionId, authentication.getName()));
    }

    @PostMapping("/{sessionId}/reactions")
    public ResponseEntity<MarketingReactionResponse> react(@PathVariable String sessionId,
            @Valid @RequestBody MarketingReactionRequest request, Authentication authentication) {
        return ResponseEntity.ok(interactionService.react(sessionId, authentication.getName(), request.reaction()));
    }

    @GetMapping
    public ResponseEntity<List<MarketingSessionSummary>> listSessions(Authentication authentication) {
        return ResponseEntity.ok(sessionService.listSessions(authentication.getName()));
    }

    @GetMapping("/proactive-priority")
    public ResponseEntity<MarketingProactivePriority> proactivePriority(Authentication authentication) {
        return proactivePriorityService.current(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId, Authentication authentication) {
        sessionService.deleteSession(sessionId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MarketingMessageResponse> createSession(
            @Valid @RequestBody MarketingSessionCreateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(strategyService.startSession(authentication.getName()));
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<MarketingMessageResponse> sendMessage(
            @PathVariable String sessionId,
            @Valid @RequestBody MarketingMessageRequest request,
            Authentication authentication
    ) {
        String message = request.message();
        if (request.structuredInput() != null && !request.structuredInput().isEmpty()) {
            message += "\n\nValeur structurée confirmée par le client : "
                    + objectMapper.valueToTree(request.structuredInput());
        }
        return ResponseEntity.ok(strategyService.processMessage(sessionId, authentication.getName(), message));
    }

    @PostMapping(value = "/{sessionId}/messages/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessage(
            @PathVariable String sessionId,
            @Valid @RequestBody MarketingMessageRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        SseEmitter emitter = new SseEmitter(120_000L);
        CompletableFuture.runAsync(() -> {
            try {
                MarketingMessageResponse response = strategyService.processMessageStreaming(
                        sessionId, email, request.message(), token -> sendToken(emitter, token));
                emitter.send(SseEmitter.event().name("meta").data(response));
                emitter.send(SseEmitter.event().name("done").data(response));
                emitter.complete();
            } catch (Exception exception) {
                try {
                    emitter.send(SseEmitter.event().name("error")
                            .data(java.util.Map.of("message", "Le Coach n'a pas pu terminer cette réponse.")));
                    emitter.complete();
                } catch (Exception disconnectedClient) {
                    emitter.completeWithError(disconnectedClient);
                }
            }
        });
        return emitter;
    }

    private void sendToken(SseEmitter emitter, String token) {
        try {
            emitter.send(SseEmitter.event().name("token").data(token));
        } catch (java.io.IOException exception) {
            throw new IllegalStateException("Client SSE déconnecté", exception);
        }
    }

    @PostMapping(value = "/{sessionId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MarketingMessageResponse> sendMessageWithImage(
            @PathVariable String sessionId,
            @RequestPart(required = false) String message,
            @RequestPart("image") MultipartFile image,
            Authentication authentication
    ) throws java.io.IOException {
        if (image.isEmpty() || image.getSize() > 4L * 1024 * 1024) {
            throw new IllegalArgumentException("L'image doit être comprise entre 1 octet et 4 Mo");
        }
        String contentType = image.getContentType();
        if (!Set.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
            throw new IllegalArgumentException("Seules les images JPEG, PNG et WebP sont acceptées");
        }
        String effectiveMessage = message == null || message.isBlank()
                ? "Analyse cette image dans le contexte de ma stratégie Marketing."
                : message.trim();
        String dataUrl = "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(image.getBytes());
        return ResponseEntity.ok(strategyService.processMessage(sessionId, authentication.getName(), effectiveMessage,
                new MarketingImageInput(dataUrl, image.getOriginalFilename())));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<MarketingSessionState> getSession(
            @PathVariable String sessionId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(strategyService.getSession(sessionId, authentication.getName()));
    }

    @PatchMapping("/{sessionId}/state")
    public ResponseEntity<MarketingSessionState> correctState(
            @PathVariable String sessionId,
            @Valid @RequestBody MarketingStateCorrectionRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(sessionService.correctState(
                sessionId, authentication.getName(), request.path(), objectMapper.valueToTree(request.value())));
    }

    @GetMapping("/{sessionId}/corrections")
    public ResponseEntity<List<MarketingStateCorrectionView>> getCorrections(
            @PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(sessionService.getCorrections(sessionId, authentication.getName()));
    }

    @PostMapping("/{sessionId}/public-presence")
    public ResponseEntity<MarketingSessionState> researchPublicPresence(
            @PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(publicPresenceService.research(sessionId, authentication.getName()));
    }

    @PostMapping("/{sessionId}/public-presence/decision")
    public ResponseEntity<MarketingSessionState> decidePublicFinding(
            @PathVariable String sessionId,
            @Valid @RequestBody MarketingPublicFindingDecisionRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(publicPresenceService.decide(
                sessionId, authentication.getName(), request.findingIndex(), request.accepted()));
    }

    @GetMapping("/{sessionId}/kpi-dashboard")
    public ResponseEntity<MarketingKpiDashboard> kpiDashboard(@PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(kpiService.dashboard(sessionId, authentication.getName()));
    }

    @PostMapping("/{sessionId}/kpi-measurements")
    public ResponseEntity<MarketingKpiDashboard> addKpi(@PathVariable String sessionId, @Valid @RequestBody MarketingKpiMeasurementRequest request, Authentication authentication) {
        return ResponseEntity.ok(kpiService.add(sessionId, authentication.getName(), request));
    }

    @GetMapping("/{sessionId}/follow-up")
    public ResponseEntity<MarketingFollowUpDashboard> followUp(@PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(followUpService.dashboard(sessionId, authentication.getName()));
    }

    @PatchMapping("/{sessionId}/actions/{actionId}")
    public ResponseEntity<MarketingActionItemView> updateAction(
            @PathVariable String sessionId,
            @PathVariable Long actionId,
            @Valid @RequestBody MarketingActionUpdateRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(actionPlanService.update(sessionId, actionId, authentication.getName(), request));
    }

    @PatchMapping("/{sessionId}/check-ins")
    public ResponseEntity<Boolean> setCheckIns(@PathVariable String sessionId, @RequestBody java.util.Map<String,Boolean> request, Authentication authentication) {
        return ResponseEntity.ok(checkInService.setEnabled(sessionId, authentication.getName(), Boolean.TRUE.equals(request.get("enabled"))));
    }

    @GetMapping("/{sessionId}/check-ins")
    public ResponseEntity<Boolean> getCheckIns(@PathVariable String sessionId, Authentication authentication) {
        return ResponseEntity.ok(checkInService.isEnabled(sessionId, authentication.getName()));
    }

    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<List<MarketingConversationMessage>> getMessages(
            @PathVariable String sessionId,
            Authentication authentication
    ) {
        List<MarketingConversationMessage> messages = strategyService.getMessages(sessionId, authentication.getName())
                .stream()
                .map(message -> new MarketingConversationMessage(
                        message.getRole().name().toLowerCase(),
                        message.getContent(),
                        message.getImageDataUrl(),
                        message.getImageName(),
                        readAnnotations(message.getImageAnnotationsJson()),
                        message.getActiveStage(),
                        message.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{sessionId}/follow-up/messages")
    public ResponseEntity<List<MarketingConversationMessage>> getFollowUpMessages(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication
    ) {
        List<MarketingConversationMessage> messages = sessionService
                .getRecentFollowUpMessages(sessionId, authentication.getName(), limit)
                .stream()
                .map(message -> new MarketingConversationMessage(
                        message.getRole().name().toLowerCase(), message.getContent(),
                        message.getImageDataUrl(), message.getImageName(),
                        readAnnotations(message.getImageAnnotationsJson()),
                        message.getActiveStage(), message.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(messages);
    }

    private List<MarketingImageAnnotation> readAnnotations(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() { });
        } catch (Exception exception) {
            return List.of();
        }
    }

    @PostMapping("/{sessionId}/finalize")
    public ResponseEntity<MarketingStrategyResult> finalizeSession(
            @PathVariable String sessionId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(deliverableService.finalizeStrategy(sessionId, authentication.getName()));
    }

    @GetMapping(value = "/{sessionId}/deliverable", produces = MediaType.TEXT_MARKDOWN_VALUE)
    public ResponseEntity<String> getDeliverable(
            @PathVariable String sessionId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(deliverableService.getDeliverable(sessionId, authentication.getName()));
    }
}
