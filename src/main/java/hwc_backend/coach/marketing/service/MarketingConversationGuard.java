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
        String normalized = normalize(message);
        return NON_ANSWERS.contains(normalized)
                || normalized.matches("^(je )?(ne )?sais pas( encore| aussi| vraiment| trop)?$")
                || normalized.matches("^(aucune idee|pas d idee|j ignore)( encore| aussi)?$");
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

    public boolean asksAboutKnownState(String message) {
        String normalized = normalize(message);
        return normalized.matches(".*\\b(c est quoi|quel est|quelle est|rappelle|dit moi)\\b.*\\b(mon|ma|mes|notre|nos)\\b.*")
                || normalized.matches(".*\\b(mon entreprise|ma societe|mon activite|mon objectif|ma cible|mon budget)\\b.*\\?");
    }

    public String reply(MarketingSessionState state, String message) {
        String normalized = normalize(message);
        String question = stageEvaluator.fallbackQuestion(state);
        if (asksAboutKnownState(message)) return knownStateReply(state, normalized, question);
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
        if (normalized.contains("sais pas") || normalized.contains("aucune idee") || normalized.contains("j ignore")) {
            return guidanceFor(state, normalized.contains("aussi"));
        }
        return "Merci. Pour construire une stratégie vraiment adaptée, j'ai encore besoin de cette information : " + question;
    }

    private String knownStateReply(MarketingSessionState state, String normalized, String nextQuestion) {
        if (normalized.contains("entreprise") || normalized.contains("societe")) {
            String name = state.getCompany().getName();
            String sector = state.getCompany().getSector();
            String offer = state.getCompany().getProductsOrServices().stream().findFirst().orElse(null);
            return "Votre entreprise enregistrée est « " + valueOrUnknown(name) + " ». Son activité est « "
                    + valueOrUnknown(sector) + " »" + (offer == null ? "" : " et l'offre principale enregistrée est « " + offer + " »")
                    + ". Pour poursuivre : " + nextQuestion;
        }
        if (normalized.contains("activite")) return "L'activité enregistrée est « "
                + valueOrUnknown(state.getCompany().getSector()) + " ». Pour poursuivre : " + nextQuestion;
        if (normalized.contains("objectif")) return "Votre objectif enregistré est « "
                + valueOrUnknown(state.getObjectives().getSmartStatement()) + " ». Pour poursuivre : " + nextQuestion;
        if (normalized.contains("cible")) return "La cible enregistrée est « "
                + (state.getTargetAudience().getSegments().isEmpty() ? "non définie" : String.join(", ", state.getTargetAudience().getSegments()))
                + " ». Pour poursuivre : " + nextQuestion;
        if (normalized.contains("budget")) return "Le budget mensuel enregistré est « "
                + (state.getBudget().getMonthlyAmount() == null ? "non défini" : state.getBudget().getMonthlyAmount() + " " + valueOrUnknown(state.getBudget().getCurrency()))
                + " ». Pour poursuivre : " + nextQuestion;
        return "Voici ce qui est enregistré dans le panneau de stratégie. Pour poursuivre : " + nextQuestion;
    }

    private String valueOrUnknown(Object value) {
        return value == null || value.toString().isBlank() ? "non défini" : value.toString();
    }

    public java.util.List<String> suggestedReplies(MarketingSessionState state) {
        String missing = state.getMissingInformation().stream().findFirst().orElse("");
        return switch (missing) {
            case "objectives.targetValue" -> java.util.List.of("Environ 10 par mois", "Environ 20 par mois", "Environ 30 par mois");
            case "objectives.deadline" -> java.util.List.of("Dans 1 mois", "Dans 3 mois", "Dans 6 mois");
            case "audit.website" -> java.util.List.of("Non, aucun site", "Oui, il présente nos offres", "Oui, mais il génère peu de contacts");
            case "budget.monthlyAmount" -> java.util.List.of("Moins de 2 000 MAD", "Entre 2 000 et 5 000 MAD", "Plus de 5 000 MAD");
            case "budget.weeklyTimeHours" -> java.util.List.of("Moins de 2 h", "Entre 2 et 5 h", "Plus de 5 h");
            default -> java.util.List.of("Donnez-moi des exemples", "Aidez-moi à l'estimer", "Je préfère y revenir plus tard");
        };
    }

    private String guidanceFor(MarketingSessionState state, boolean repeated) {
        String missing = state.getMissingInformation().stream().findFirst().orElse("");
        String opening = repeated
                ? "Pas de problème. Changeons d'approche avec des choix simples. "
                : "Ce n'est pas grave : nous pouvons l'estimer ensemble. ";
        return opening + switch (missing) {
            case "objectives.targetValue" -> "Pour commencer, viseriez-vous plutôt 10, 20 ou 30 résultats qualifiés par mois ? Nous pourrons ajuster ensuite.";
            case "objectives.deadline" -> "Une échéance de 1, 3 ou 6 mois vous paraît-elle la plus réaliste ?";
            case "audit.website" -> "Dites-moi simplement : aucun site, un site vitrine, ou un site qui reçoit déjà des demandes ?";
            case "audit.trackingAvailable" -> "Répondez simplement : aucune mesure, quelques statistiques, ou un suivi précis des demandes ?";
            case "budget.monthlyAmount" -> "Votre budget serait-il plutôt inférieur à 2 000 MAD, entre 2 000 et 5 000 MAD, ou supérieur à 5 000 MAD ?";
            case "budget.weeklyTimeHours" -> "Votre équipe peut-elle consacrer moins de 2 h, entre 2 et 5 h, ou plus de 5 h par semaine ?";
            default -> "Je peux vous donner des exemples. " + stageEvaluator.fallbackQuestion(state);
        };
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
