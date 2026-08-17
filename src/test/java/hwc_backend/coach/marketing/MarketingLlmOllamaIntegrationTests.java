package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.service.MarketingPromptService;
import hwc_backend.coach.marketing.service.OpenAiMarketingLanguageModelService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.test.util.ReflectionTestUtils;

@EnabledIfEnvironmentVariable(named = "RUN_MARKETING_OPENAI_IT", matches = "true")
class MarketingLlmOllamaIntegrationTests {

    @Test
    void extractsExplicitCompanyDataIntoTheRequiredJsonContract() throws Exception {
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("SESSION-OLLAMA-TEST");
        MarketingPromptService prompts = new MarketingPromptService();
        String stateJson = new ObjectMapper().writeValueAsString(state);
        String prompt = prompts.buildNextQuestionPrompt(
                state,
                stateJson,
                "Nous sommes un restaurant marocain B2C à Casablanca, une TPE qui vend des repas sur place et à emporter.",
                List.of()
        );
        OpenAiMarketingLanguageModelService llm = openAiService();

        JsonNode response = new ObjectMapper().readTree(llm.generateJson(prompts.systemPrompt(), prompt));

        assertThat(response.has("updatedState")).isTrue();
        assertThat(response.path("intent").asText()).isEqualTo("ANSWER");
        assertThat(response.path("reply").asText()).isNotBlank();
        assertThat(response.path("suggestedReplies").isArray()).isTrue();
        JsonNode updated = response.path("updatedState");
        assertThat(updated.path("company").path("sector").asText())
                .as("Réponse Ollama: %s", response.toPrettyString())
                .containsIgnoringCase("restaur");
        assertThat(updated.path("company").path("businessModel").asText()).isEqualTo("B2C");
        assertThat(updated.path("company").path("location").asText()).containsIgnoringCase("Casablanca");
    }

    @Test
    void answersAClientQuestionAndProvidesContextualChoices() throws Exception {
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("SESSION-QUESTION-TEST");
        state.setMissingInformation(new java.util.ArrayList<>(List.of("company.productsOrServices")));
        MarketingPromptService prompts = new MarketingPromptService();
        String prompt = prompts.buildNextQuestionPrompt(
                state,
                new ObjectMapper().writeValueAsString(state),
                "Comment pouvez-vous m'aider ?",
                List.of()
        );
        OpenAiMarketingLanguageModelService llm = openAiService();

        JsonNode response = new ObjectMapper().readTree(llm.generateJson(prompts.systemPrompt(), prompt));

        assertThat(response.path("intent").asText()).isIn("QUESTION", "CLARIFICATION");
        assertThat(response.path("reply").asText())
                .as("Réponse Ollama: %s", response.toPrettyString())
                .isNotBlank();
        assertThat(response.path("suggestedReplies").size()).isBetween(2, 4);
    }

    @Test
    void extractsTheExactSoftwareAndConsultingAnswerFromTheClientConversation() throws Exception {
        MarketingSessionState state = new MarketingSessionState();
        state.setSessionId("SESSION-SOFTWARE-TEST");
        state.getCompany().setSector("Services aux entreprises");
        state.setMissingInformation(new java.util.ArrayList<>(List.of("company.productsOrServices")));
        MarketingPromptService prompts = new MarketingPromptService();
        String prompt = prompts.buildNextQuestionPrompt(
                state,
                new ObjectMapper().writeValueAsString(state),
                "Nous commercialisons principalement des solutions logicielles de gestion et des services d'accompagnement sur mesure pour les entreprises. Notre offre se divise en deux grands pôles d'activité.",
                List.of()
        );
        OpenAiMarketingLanguageModelService llm = openAiService();

        JsonNode response = new ObjectMapper().readTree(llm.generateJson(prompts.systemPrompt(), prompt));

        assertThat(response.path("intent").asText()).isEqualTo("ANSWER");
        JsonNode products = response.path("updatedState").path("company").path("productsOrServices");
        if (products.isMissingNode()) {
            products = response.path("updatedState").path("company.productsOrServices");
        }
        assertThat(products)
                .as("Réponse Ollama: %s", response.toPrettyString())
                .isNotEmpty();
        assertThat(response.path("reply").asText())
                .doesNotContain("Quels produits ou services commercialisez-vous");
    }

    private OpenAiMarketingLanguageModelService openAiService() {
        OpenAiMarketingLanguageModelService service = new OpenAiMarketingLanguageModelService();
        ReflectionTestUtils.setField(service, "apiKey", System.getenv("OPENAI_API_KEY"));
        ReflectionTestUtils.setField(service, "model", System.getenv().getOrDefault("OPENAI_MODEL", "gpt-5.6-terra"));
        ReflectionTestUtils.setField(service, "responsesUrl", "https://api.openai.com/v1/responses");
        return service;
    }
}
