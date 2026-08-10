package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import hwc_backend.coach.marketing.model.MarketingImageInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAiMarketingLanguageModelService implements MarketingLanguageModelService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.builder().build();

    @Value("${openai.api-key:}")
    private String apiKey;

    @Value("${openai.model:gpt-5.6-terra}")
    private String model;

    @Value("${openai.responses-url:https://api.openai.com/v1/responses}")
    private String responsesUrl;

    @Override
    public String generateJson(String systemPrompt, String userPrompt) {
        return generateJson(systemPrompt, userPrompt, null);
    }

    @Override
    public String generateJson(String systemPrompt, String userPrompt, MarketingImageInput image) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is required for the Marketing Coach");
        }

        Object userContent = image == null
                ? userPrompt
                : List.of(
                        Map.of("type", "input_text", "text", userPrompt),
                        Map.of("type", "input_image", "image_url", image.dataUrl(), "detail", "low")
                );
        Map<String, Object> request = Map.of(
                "model", model,
                "input", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userContent)
                ),
                "reasoning", Map.of("effort", "low"),
                "text", Map.of("format", Map.of("type", "json_object"), "verbosity", "low"),
                "max_output_tokens", 4000
        );

        String response = restClient.post()
                .uri(responsesUrl)
                .headers(headers -> headers.setBearerAuth(apiKey))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);

        if (response == null || response.isBlank()) {
            throw new IllegalStateException("OpenAI Marketing response is empty");
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode outputText = root.get("output_text");
            if (outputText != null && outputText.isTextual() && !outputText.asText().isBlank()) {
                return outputText.asText();
            }
            for (JsonNode output : root.path("output")) {
                for (JsonNode content : output.path("content")) {
                    JsonNode text = content.get("text");
                    if (text != null && text.isTextual() && !text.asText().isBlank()) {
                        return text.asText();
                    }
                }
            }
            throw new IllegalStateException("OpenAI Marketing response text not found");
        } catch (Exception exception) {
            throw new IllegalStateException("OpenAI Marketing response is invalid", exception);
        }
    }
}
