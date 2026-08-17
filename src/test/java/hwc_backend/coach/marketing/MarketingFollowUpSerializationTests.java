package hwc_backend.coach.marketing;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.model.MarketingActionItemView;
import hwc_backend.coach.marketing.model.MarketingFollowUpDashboard;
import hwc_backend.coach.marketing.model.MarketingKpiDashboard;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarketingFollowUpSerializationTests {

    @Test
    void serializesDatesUsedByTheFollowUpCoachContext() throws Exception {
        LocalDateTime measuredAt = LocalDateTime.of(2026, 8, 17, 19, 30);
        MarketingFollowUpDashboard dashboard = new MarketingFollowUpDashboard(
                "session-id",
                "Mesurer le KPI prioritaire",
                List.of(new MarketingActionItemView(
                        1L, "Publier la campagne", 1, LocalDate.of(2026, 8, 20),
                        "TODO", null, null)),
                new MarketingKpiDashboard(
                        List.of(new MarketingKpiDashboard.Series(
                                "Conversions", "%",
                                List.of(new MarketingKpiDashboard.Point(new BigDecimal("12.5"), measuredAt)),
                                "UP")),
                        "Progression", "Ajouter une nouvelle mesure"));

        String json = new ObjectMapper().findAndRegisterModules().writeValueAsString(dashboard);

        assertThat(json)
                .contains("\"dueDate\":[2026,8,20]")
                .contains("\"measuredAt\":[2026,8,17,19,30]");
    }
}
