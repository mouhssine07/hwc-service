package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.service.MarketingLanguageModelService;
import hwc_backend.coach.marketing.service.MarketingPromptService;
import hwc_backend.coach.marketing.service.MarketingPublicPresenceService;
import hwc_backend.coach.marketing.service.MarketingSessionService;
import org.junit.jupiter.api.Test;

class MarketingPublicPresenceServiceTests {
    @Test
    void storesOnlyPublicFindingsAsHypothesesToConfirm() {
        MarketingSessionService sessions = mock(MarketingSessionService.class);
        MarketingLanguageModelService model = mock(MarketingLanguageModelService.class);
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("session-1");
        state.getCompany().setName("HWC Casablanca");
        when(sessions.getState("session-1", "client@hwc.ma")).thenReturn(state);
        when(sessions.saveState("session-1", "client@hwc.ma", state)).thenReturn(state);
        when(model.generateJsonWithWebSearch(anyString(), anyString())).thenReturn("""
                {"findings":[
                  {"sourceTitle":"Site officiel","url":"https://example.com","claim":"Un site public semble actif","confirmationRequired":false},
                  {"sourceTitle":"URL invalide","url":"file:///secret","claim":"À ignorer"}
                ]}
                """);
        MarketingPublicPresenceService service = new MarketingPublicPresenceService(
                sessions, model, new MarketingPromptService());

        MarketingSessionState result = service.research("session-1", "client@hwc.ma");

        assertThat(result.getPublicWebFindings()).hasSize(1);
        assertThat(result.getPublicWebFindings().getFirst().get("confirmationRequired")).isEqualTo(true);
        assertThat(result.getAssumptions()).allMatch(value -> value.startsWith("À confirmer"));
        verify(sessions).saveState("session-1", "client@hwc.ma", state);
    }

    @Test
    void usesDeclaredWebsiteAsIdentityAnchorWhenSearchHasNoReliableFinding() {
        MarketingSessionService sessions = mock(MarketingSessionService.class);
        MarketingLanguageModelService model = mock(MarketingLanguageModelService.class);
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("session-2");
        state.getCompany().setName("EMSI");
        state.getAudit().setWebsite(java.util.Map.of("url", "https://emsi.ma/", "exists", true));
        when(sessions.getState("session-2", "client@hwc.ma")).thenReturn(state);
        when(sessions.saveState("session-2", "client@hwc.ma", state)).thenReturn(state);
        when(model.generateJsonWithWebSearch(anyString(), anyString())).thenReturn("{\"findings\":[]}");
        MarketingPublicPresenceService service = new MarketingPublicPresenceService(
                sessions, model, new MarketingPromptService());

        MarketingSessionState result = service.research("session-2", "client@hwc.ma");

        assertThat(result.getPublicWebFindings()).singleElement()
                .satisfies(finding -> {
                    assertThat(finding.get("url")).isEqualTo("https://emsi.ma/");
                    assertThat(finding.get("confirmationRequired")).isEqualTo(true);
                });
    }
}
