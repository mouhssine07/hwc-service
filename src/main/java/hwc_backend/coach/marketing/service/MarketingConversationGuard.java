package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MarketingConversationGuard {

    private static final Set<String> NON_ANSWERS = Set.of(
            "salut", "bonjour", "bonsoir", "hello", "coucou",
            "merci", "merci beaucoup", "ok", "d accord", "je ne sais pas",
            "je ne comprends pas", "pouvez vous expliquer"
    );

    private final MarketingStageEvaluator stageEvaluator;

    public MarketingConversationGuard(MarketingStageEvaluator stageEvaluator) {
        this.stageEvaluator = stageEvaluator;
    }

    public boolean isNonInformative(String message) {
        return NON_ANSWERS.contains(normalize(message));
    }

    public boolean looksLikeQuestion(String message) {
        String normalized = normalize(message);
        return (message != null && message.contains("?"))
                || normalized.matches("^(est ce|qu est ce|que|quel|quelle|quels|quelles|comment|pourquoi|qui|ou|pouvez vous|comprenez vous|savez vous)\\b.*");
    }

    public boolean asksWhetherCoachUnderstands(String message) {
        String normalized = normalize(message);
        return normalized.contains("comprenez ce que je dis")
                || normalized.contains("comprends ce que je dis")
                || normalized.contains("vous me comprenez");
    }

    public String reply(MarketingSessionState state, String message) {
        String normalized = normalize(message);
        String question = stageEvaluator.fallbackQuestion(state);
        if (Set.of("salut", "bonjour", "bonsoir", "hello", "coucou").contains(normalized)) {
            return "Bonjour ! Ravi de vous accompagner. " + question;
        }
        if (asksWhetherCoachUnderstands(message)) {
            String sector = state.getCompany().getSector();
            String model = state.getCompany().getBusinessModel();
            String understood = sector == null || sector.isBlank()
                    ? "les informations que vous m'avez données"
                    : "que votre activité concerne " + sector
                        + (model == null || model.isBlank() ? "" : " et que vous vous adressez principalement en " + model);
            return "Oui, je vous comprends. J'ai bien retenu " + understood
                    + ". Si un élément est inexact, vous pouvez me corriger. Pour poursuivre : " + question;
        }
        if (normalized.contains("ne comprends") || normalized.contains("expliquer")) {
            return "Bien sûr. Cette information me permet d'adapter la stratégie à votre situation réelle. " + question;
        }
        if (normalized.contains("ne sais pas")) {
            return "Ce n'est pas grave : nous pouvons l'estimer ensemble. Donnez-moi simplement votre meilleure approximation. " + question;
        }
        return "Merci. Pour construire une stratégie vraiment adaptée, j'ai encore besoin de cette information : " + question;
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return normalized;
    }
}
