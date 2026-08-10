package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.service.MarketingPromptService;
import org.junit.jupiter.api.Test;

class MarketingPromptServiceTests {

    private final MarketingPromptService service = new MarketingPromptService();

    @Test
    void loadsSpecializedPromptsAndFinalTemplate() {
        assertThat(service.systemPrompt())
                .contains("stratégie Marketing Digital", "une seule question principale", "n'invente");
        assertThat(service.deliverableTemplate())
                .contains("# Mini-stratégie Marketing Digital", "{{companySummary}}", "{{fourWeekPlan}}");
    }
}
