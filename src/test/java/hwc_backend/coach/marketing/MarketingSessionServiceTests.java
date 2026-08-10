package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.entity.MarketingSession;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import hwc_backend.coach.marketing.repository.MarketingSessionMessageRepository;
import hwc_backend.coach.marketing.repository.MarketingSessionRepository;
import hwc_backend.coach.marketing.service.MarketingSessionServiceImpl;
import hwc_backend.entity.User;
import hwc_backend.repository.UserRepository;
import hwc_backend.repository.DiagnosticRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketingSessionServiceTests {

    @Mock private MarketingSessionRepository sessionRepository;
    @Mock private MarketingSessionMessageRepository messageRepository;
    @Mock private UserRepository userRepository;
    @Mock private DiagnosticRepository diagnosticRepository;

    private MarketingSessionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MarketingSessionServiceImpl(
                sessionRepository,
                messageRepository,
                userRepository,
                diagnosticRepository
        );
    }

    @Test
    void createsAnIsolatedMarketingSessionAndPrefillsKnownCompanyData() {
        User user = client();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(MarketingSession.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MarketingSessionState state = service.createSession(user.getEmail());

        assertThat(state.getServiceId()).isEqualTo("MARKETING_STRATEGY");
        assertThat(state.getStage()).isEqualTo(MarketingStrategyStage.COMPANY_DISCOVERY);
        assertThat(state.getCompany().getSector()).isEqualTo("Immobilier");
        assertThat(state.getMissingInformation()).doesNotContain("company.sector", "company.size");
        assertThat(state.isCompleted()).isFalse();

        ArgumentCaptor<MarketingSession> captor = ArgumentCaptor.forClass(MarketingSession.class);
        verify(sessionRepository).save(captor.capture());
        assertThat(captor.getValue().getStateJson()).contains("MARKETING_STRATEGY", "COMPANY_DISCOVERY");
    }

    @Test
    void reloadsThePersistedStateForItsOwner() throws Exception {
        User user = client();
        MarketingSessionState expected = new MarketingSessionState();
        expected.setSessionId("session-1");
        MarketingSession session = new MarketingSession();
        session.setId("session-1");
        session.setUser(user);
        session.setStage(MarketingStrategyStage.COMPANY_DISCOVERY);
        session.setStateJson(new ObjectMapper().writeValueAsString(expected));
        when(sessionRepository.findByIdAndUserEmail("session-1", user.getEmail()))
                .thenReturn(Optional.of(session));

        MarketingSessionState actual = service.getState("session-1", user.getEmail());

        assertThat(actual.getSessionId()).isEqualTo("session-1");
        assertThat(actual.getServiceId()).isEqualTo("MARKETING_STRATEGY");
    }

    @Test
    void refusesARequestWhenTheSessionDoesNotBelongToTheAuthenticatedClient() {
        when(sessionRepository.findByIdAndUserEmail("foreign-session", "client@hwc.ma"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getState("foreign-session", "client@hwc.ma"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Session Marketing introuvable");
    }

    private User client() {
        User user = new User();
        user.setId(42L);
        user.setEmail("client@hwc.ma");
        user.setNom("Client");
        user.setPrenom("Test");
        user.setSecteur("Immobilier");
        user.setTailleEntreprise("TPE");
        return user;
    }
}
