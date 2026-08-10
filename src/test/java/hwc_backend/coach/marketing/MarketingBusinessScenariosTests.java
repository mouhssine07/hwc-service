package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MarketingBusinessScenariosTests {

    @Test
    void containsTheTenRequiredIsolatedMarketingScenarios() throws Exception {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(
                "coach-ia/services/marketing-strategy/tests/scenarios.json")) {
            assertThat(input).isNotNull();
            JsonNode root = new ObjectMapper().readTree(input);
            assertThat(root.path("serviceId").asText()).isEqualTo("MARKETING_STRATEGY");
            assertThat(root.path("scenarios")).hasSize(10);

            Set<String> ids = new HashSet<>();
            root.path("scenarios").forEach(scenario -> {
                assertThat(ids.add(scenario.path("id").asText())).isTrue();
                assertThat(scenario.path("company").path("sector").asText()).isNotBlank();
                assertThat(scenario.path("objective").asText()).isNotBlank();
                assertThat(scenario.path("expectedPriorities").isEmpty()).isFalse();
                assertThat(scenario.path("expectedKpis").isEmpty()).isFalse();
                assertThat(scenario.path("forbiddenBehaviors").isEmpty()).isFalse();
            });
        }
    }
}
