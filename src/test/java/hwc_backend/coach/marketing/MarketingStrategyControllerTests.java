package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hwc_backend.coach.marketing.controller.MarketingStrategyController;
import hwc_backend.coach.marketing.model.MarketingMessageRequest;
import hwc_backend.coach.marketing.model.MarketingMessageResponse;
import hwc_backend.coach.marketing.model.MarketingSessionCreateRequest;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.model.MarketingStateCorrectionRequest;
import hwc_backend.coach.marketing.service.MarketingDeliverableService;
import hwc_backend.coach.marketing.service.MarketingStrategyService;
import hwc_backend.coach.marketing.service.MarketingSessionService;
import hwc_backend.coach.marketing.service.MarketingPublicPresenceService;
import hwc_backend.coach.marketing.service.MarketingKpiService;
import hwc_backend.coach.marketing.service.MarketingCheckInService;
import hwc_backend.coach.marketing.service.MarketingActionPlanService;
import hwc_backend.coach.marketing.service.MarketingFollowUpService;
import hwc_backend.coach.marketing.service.MarketingProactivePriorityService;
import hwc_backend.coach.marketing.service.MarketingInteractionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class MarketingStrategyControllerTests {

    private final MarketingStrategyService strategyService = mock(MarketingStrategyService.class);
    private final MarketingDeliverableService deliverableService = mock(MarketingDeliverableService.class);
    private final MarketingSessionService sessionService = mock(MarketingSessionService.class);
    private final MarketingPublicPresenceService publicPresenceService = mock(MarketingPublicPresenceService.class);
    private final MarketingKpiService kpiService = mock(MarketingKpiService.class);
    private final MarketingCheckInService checkInService = mock(MarketingCheckInService.class);
    private final MarketingActionPlanService actionPlanService = mock(MarketingActionPlanService.class);
    private final MarketingFollowUpService followUpService = mock(MarketingFollowUpService.class);
    private final MarketingProactivePriorityService proactivePriorityService = mock(MarketingProactivePriorityService.class);
    private final MarketingInteractionService interactionService = mock(MarketingInteractionService.class);
    private final MarketingStrategyController controller =
            new MarketingStrategyController(strategyService, deliverableService, sessionService, publicPresenceService,
                    kpiService, checkInService, actionPlanService, followUpService, proactivePriorityService,
                    interactionService);
    private final Authentication authentication = authentication();

    @Test
    void createsOnlyTheMarketingStrategySessionForTheAuthenticatedClient() {
        MarketingMessageResponse expected = response("session-1");
        when(strategyService.startSession("client@hwc.ma")).thenReturn(expected);

        var result = controller.createSession(
                new MarketingSessionCreateRequest("MARKETING_STRATEGY"), authentication);

        assertThat(result.getBody()).isEqualTo(expected);
        verify(strategyService).startSession("client@hwc.ma");
    }

    @Test
    void routesMessagesWithTheAuthenticatedOwnerIdentity() {
        MarketingMessageResponse expected = response("session-1");
        when(strategyService.processMessage("session-1", "client@hwc.ma", "Nous sommes un restaurant"))
                .thenReturn(expected);

        var result = controller.sendMessage("session-1",
                new MarketingMessageRequest("Nous sommes un restaurant"), authentication);

        assertThat(result.getBody()).isEqualTo(expected);
        verify(strategyService).processMessage("session-1", "client@hwc.ma", "Nous sommes un restaurant");
    }

    @Test
    void convertsAPlainHttpCorrectionValueToJsonInsideTheController() {
        MarketingSessionState expected = new MarketingSessionState();
        when(sessionService.correctState(
                org.mockito.ArgumentMatchers.eq("session-1"),
                org.mockito.ArgumentMatchers.eq("client@hwc.ma"),
                org.mockito.ArgumentMatchers.eq("company.name"),
                org.mockito.ArgumentMatchers.argThat(value -> value.isTextual() && "Nouveau nom".equals(value.asText()))))
                .thenReturn(expected);

        var result = controller.correctState("session-1",
                new MarketingStateCorrectionRequest("company.name", "Nouveau nom"), authentication);

        assertThat(result.getBody()).isSameAs(expected);
    }

    @Test
    void createsAnAuthenticatedSseStreamForConversationText() throws Exception {
        MarketingMessageResponse expected = response("session-1");
        when(strategyService.processMessageStreaming(
                org.mockito.ArgumentMatchers.eq("session-1"),
                org.mockito.ArgumentMatchers.eq("client@hwc.ma"),
                org.mockito.ArgumentMatchers.eq("Nous sommes un restaurant"),
                org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> {
                    java.util.function.Consumer<String> consumer = invocation.getArgument(3);
                    consumer.accept(expected.message());
                    return expected;
                });

        SseEmitter emitter = controller.streamMessage("session-1",
                new MarketingMessageRequest("Nous sommes un restaurant"), authentication);

        assertThat(emitter.getTimeout()).isEqualTo(120_000L);
        org.mockito.Mockito.verify(strategyService, org.mockito.Mockito.timeout(2_000))
                .processMessageStreaming(
                        org.mockito.ArgumentMatchers.eq("session-1"),
                        org.mockito.ArgumentMatchers.eq("client@hwc.ma"),
                        org.mockito.ArgumentMatchers.eq("Nous sommes un restaurant"),
                        org.mockito.ArgumentMatchers.any());
    }

    private Authentication authentication() {
        Authentication value = mock(Authentication.class);
        when(value.getName()).thenReturn("client@hwc.ma");
        return value;
    }

    private MarketingMessageResponse response(String sessionId) {
        return new MarketingMessageResponse(sessionId, MarketingStrategyStage.COMPANY_DISCOVERY,
                "Quelle est votre activité ?", List.of("company.sector"),
                MarketingSessionState.Confidence.LOW, false, List.of());
    }
}
