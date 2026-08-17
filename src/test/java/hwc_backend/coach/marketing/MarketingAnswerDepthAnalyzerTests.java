package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.service.MarketingAnswerDepthAnalyzer;
import org.junit.jupiter.api.Test;

class MarketingAnswerDepthAnalyzerTests {

    private final MarketingAnswerDepthAnalyzer analyzer = new MarketingAnswerDepthAnalyzer();

    @Test
    void distinguishesVagueStandardAndDetailedAnswers() {
        assertThat(analyzer.analyze("Je ne sais pas"))
                .isEqualTo(MarketingAnswerDepthAnalyzer.Depth.VAGUE);
        assertThat(analyzer.analyze("Nous ciblons surtout les propriétaires à Casablanca"))
                .isEqualTo(MarketingAnswerDepthAnalyzer.Depth.STANDARD);
        assertThat(analyzer.analyze("Nous sommes une agence B2B à Casablanca, avec un budget de 5000 MAD par mois, "
                + "une équipe de 3 personnes et nous utilisons déjà LinkedIn et notre site web."))
                .isEqualTo(MarketingAnswerDepthAnalyzer.Depth.DETAILED);
    }
}
