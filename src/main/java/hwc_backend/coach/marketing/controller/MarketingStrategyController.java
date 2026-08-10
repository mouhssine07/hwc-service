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
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.Set;

@RestController
@RequestMapping("/api/client/coach/marketing/sessions")
@RequiredArgsConstructor
public class MarketingStrategyController {

    private final MarketingStrategyService strategyService;
    private final MarketingDeliverableService deliverableService;
    private final MarketingSessionService sessionService;

    @GetMapping
    public ResponseEntity<List<MarketingSessionSummary>> listSessions(Authentication authentication) {
        return ResponseEntity.ok(sessionService.listSessions(authentication.getName()));
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
        return ResponseEntity.ok(strategyService.processMessage(sessionId, authentication.getName(), request.message()));
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
                        message.getActiveStage(),
                        message.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(messages);
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
