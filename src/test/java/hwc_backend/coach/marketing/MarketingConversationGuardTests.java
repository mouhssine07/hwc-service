package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.service.MarketingConversationGuard;
import hwc_backend.coach.marketing.service.MarketingStageEvaluator;
import org.junit.jupiter.api.Test;

class MarketingConversationGuardTests {

    private final MarketingConversationGuard guard =
            new MarketingConversationGuard(new MarketingStageEvaluator());

    @Test
    void greetingDoesNotCountAsAMarketingAnswer() {
        MarketingSessionState state = new MarketingSessionState();
        state.setMissingInformation(java.util.List.of("company.productsOrServices"));

        assertThat(guard.isNonInformative("Salut !")).isTrue();
        assertThat(guard.reply(state, "Salut !"))
                .contains("Bonjour", "Quels produits ou services");
    }

    @Test
    void greetingWithRealBusinessInformationIsStillProcessed() {
        assertThat(guard.isNonInformative("Salut, je vends des repas à Casablanca"))
                .isFalse();
    }

    @Test
    void clientQuestionsAreNotTreatedAsBusinessAnswers() {
        assertThat(guard.looksLikeQuestion("Est-ce que vous comprenez ce que je dis ?")).isTrue();
        assertThat(guard.looksLikeQuestion("Comment pouvez-vous m'aider")) .isTrue();
        assertThat(guard.looksLikeQuestion("Je vends des repas sur place")) .isFalse();
    }

    @Test
    void understandingQuestionReceivesAConcreteSummary() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setSector("Vente immobilière");
        state.getCompany().setBusinessModel("B2B");
        state.setMissingInformation(java.util.List.of("company.productsOrServices"));

        assertThat(guard.reply(state, "Est-ce que vous comprenez ce que je dis ?"))
                .contains("Oui, je vous comprends", "Vente immobilière", "B2B");
    }

    @Test
    void uncertaintyVariantsDoNotCountAsAnswersAndReceiveConcreteChoices() {
        MarketingSessionState state = new MarketingSessionState();
        state.setMissingInformation(java.util.List.of("objectives.targetValue"));

        assertThat(guard.isNonInformative("je sais pas aussi")).isTrue();
        assertThat(guard.reply(state, "je sais pas aussi"))
                .contains("Changeons d'approche", "10, 20 ou 30");
        assertThat(guard.suggestedReplies(state))
                .containsExactly("Environ 10 par mois", "Environ 20 par mois", "Environ 30 par mois");
    }

    @Test
    void answersQuestionsAboutCorrectedCompanyWithoutAdvancing() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setName("zoubir immo");
        state.getCompany().setSector("Immobilier");
        state.getCompany().setProductsOrServices(java.util.List.of("Services d'agence immobilière"));
        state.setMissingInformation(java.util.List.of("company.businessModel"));

        assertThat(guard.asksAboutKnownState("Alors maintenant c'est quoi mon entreprise ?")).isTrue();
        assertThat(guard.reply(state, "Alors maintenant c'est quoi mon entreprise ?"))
                .contains("zoubir immo", "Immobilier", "Services d'agence immobilière", "particuliers");
    }
}
