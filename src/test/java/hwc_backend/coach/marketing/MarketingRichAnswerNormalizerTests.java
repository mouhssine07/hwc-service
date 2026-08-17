package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.service.MarketingRichAnswerNormalizer;
import org.junit.jupiter.api.Test;

class MarketingRichAnswerNormalizerTests {
    private final MarketingRichAnswerNormalizer normalizer = new MarketingRichAnswerNormalizer();

    @Test
    void extractsExplicitFactsFromOneRichRestaurantAnswer() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setName("EMSI");
        String answer = """
                Bonjour, je gère "Le Petit Comptoir", un restaurant de cuisine méditerranéenne à Casablanca,
                avec environ 15 couverts par service. Mon objectif est d'augmenter les réservations de 30% d'ici
                3 mois. J'ai Instagram, pas de site web et pas de suivi des résultats. Ma cible principale : des
                jeunes actifs de 25-40 ans. Je veux me différencier par l'authenticité des recettes familiales.
                Mon budget marketing mensuel est de 3000 MAD et je peux consacrer 4-5h par semaine, sans employé
                dédié. Comme KPI, suivre le nombre de réservations via Instagram.
                """;

        normalizer.normalize(state, answer);

        assertThat(state.getCompany().getName()).isEqualTo("Le Petit Comptoir");
        assertThat(state.getCompany().getProductsOrServices()).containsExactly("Restauration de cuisine méditerranéenne");
        assertThat(state.getCompany().getLocation()).isEqualTo("Casablanca");
        assertThat(state.getObjectives().getTargetValue().toString()).contains("30%");
        assertThat(state.getObjectives().getDeadline()).contains("3 mois");
        assertThat(state.getAudit().getWebsite()).isEqualTo(false);
        assertThat(state.getAudit().getTrackingAvailable()).isEqualTo(false);
        assertThat(state.getTargetAudience().getSegments()).isNotEmpty();
        assertThat(state.getPositioning().getValueProposition()).contains("authenticité");
        assertThat(state.getBudget().getMonthlyAmount()).isEqualByComparingTo("3000");
        assertThat(state.getKpis()).isNotEmpty();
    }

    @Test
    void recognizesCafeOfferAndIndividualAudienceAsB2c() {
        MarketingSessionState state = new MarketingSessionState();
        state.getCompany().setName("cafe zoubir");
        normalizer.normalize(state, """
                Bonjour, je gère Café Zoubir, un café-brunch situé à Casablanca. On propose des brunchs,
                pâtisseries maison et cafés de spécialité. Notre cible est composée de freelances et étudiants
                de 20 à 35 ans. Mon objectif est +40% de fréquentation d'ici 2 mois avec un budget de 2000 MAD.
                Je peux y consacrer 3h par semaine et je souhaite suivre le taux d'occupation comme KPI.
                """);

        assertThat(state.getCompany().getProductsOrServices()).contains("Café-brunch, boissons et petite restauration");
        assertThat(state.getCompany().getBusinessModel()).isEqualTo("B2C");
    }

    @Test
    void understandsFoodVenueFromMeaningWithoutCafeOrRestaurantLabel() {
        MarketingSessionState state = new MarketingSessionState();
        normalizer.normalize(state, """
                Nous proposons un endroit calme à Casablanca où les clients peuvent prendre une boisson chaude,
                manger des pâtisseries maison ou un petit déjeuner et travailler avec le wifi. Nous accueillons
                surtout des étudiants et des freelances, avec 25 places assises. Notre objectif est d'augmenter
                la fréquentation de 20% dans 3 mois avec un budget mensuel de 1500 MAD et 3h par semaine.
                """);

        assertThat(state.getCompany().getSector()).isEqualTo("Restauration");
        assertThat(state.getCompany().getProductsOrServices())
                .contains("Café-brunch, boissons et petite restauration");
        assertThat(state.getCompany().getBusinessModel()).isEqualTo("B2C");
    }
}
