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
import hwc_backend.coach.marketing.service.MarketingDeliverableService;
import hwc_backend.coach.marketing.service.MarketingStrategyService;
import hwc_backend.coach.marketing.service.MarketingSessionService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

class MarketingStrategyControllerTests {

    private final MarketingStrategyService strategyService = mock(MarketingStrategyService.class);
    private final MarketingDeliverableService deliverableService = mock(MarketingDeliverableService.class);
    private final MarketingSessionService sessionService = mock(MarketingSessionService.class);
    private final MarketingStrategyController controller =
            new MarketingStrategyController(strategyService, deliverableService, sessionService);
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
