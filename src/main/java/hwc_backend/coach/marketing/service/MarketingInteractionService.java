package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import hwc_backend.coach.marketing.model.MarketingInputSpec;
import hwc_backend.coach.marketing.model.MarketingReactionRequest;
import hwc_backend.coach.marketing.model.MarketingReactionResponse;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketingInteractionService {
    private final MarketingSessionService sessionService;

    public MarketingInputSpec inputSpec(String sessionId, String email) {
        MarketingSessionState state = sessionService.getState(sessionId, email);
        String missing = state.getMissingInformation().stream().findFirst().orElse("");
        return switch (state.getStage()) {
            case TARGET_AUDIENCE -> new MarketingInputSpec("persona_builder", "targetAudience.primaryPersona",
                    "Décrivez votre client prioritaire", Map.of("ageMin", 18, "ageMax", 75),
                    List.of("Prix", "Qualité", "Rapidité", "Proximité", "Innovation"));
            case CHANNEL_SELECTION -> new MarketingInputSpec("channel_picker", "recommendedChannels",
                    "Sélectionnez les canaux réellement exploitables", Map.of("multiple", true),
                    List.of("Google Business Profile", "SEO", "Google Ads", "Instagram", "Facebook", "LinkedIn", "Email", "Partenariats"));
            case POSITIONING -> new MarketingInputSpec("matrix_2x2", "positioning.valueProposition",
                    "Situez votre positionnement", Map.of("xLabel", "Prix", "yLabel", "Différenciation"), List.of());
            case BUDGET_AND_RESOURCES -> new MarketingInputSpec("slider", missing, "Précisez vos ressources",
                    Map.of("min", missing.contains("weeklyTimeHours") ? 1 : 0,
                            "max", missing.contains("weeklyTimeHours") ? 80 : 100000,
                            "step", missing.contains("weeklyTimeHours") ? 1 : 500,
                            "unit", missing.contains("weeklyTimeHours") ? "h/semaine" : "MAD/mois"), List.of());
            default -> new MarketingInputSpec("free_text", missing, "Votre réponse", Map.of(), List.of());
        };
    }

    @Transactional
    public MarketingReactionResponse react(String sessionId, String email, MarketingReactionRequest.Reaction reaction) {
        MarketingSessionState state = sessionService.getState(sessionId, email);
        String message = switch (reaction) {
            case CORRECT -> "Parfait, cette proposition est confirmée. Continuons avec la question active.";
            case CLARIFY -> "Bien sûr. Indiquez ce qui est imprécis et je le reformulerai sans modifier vos données.";
            case REVISE -> "Proposition marquée à revoir. Donnez votre correction dans le champ ci-dessous.";
        };
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.USER,
                "Réaction rapide : " + reaction.name(), null);
        sessionService.appendMessage(sessionId, email, MarketingSessionMessage.Role.ASSISTANT, message, null);
        return new MarketingReactionResponse(message, state);
    }
}
