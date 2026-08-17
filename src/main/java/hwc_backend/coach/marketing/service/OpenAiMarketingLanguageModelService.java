package hwc_backend.coach.marketing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;
import hwc_backend.coach.marketing.model.MarketingImageInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAiMarketingLanguageModelService implements MarketingLanguageModelService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.builder().build();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

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
                        Map.of("type", "input_image", "image_url", image.dataUrl(), "detail", "high")
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

    @Override
    public String streamText(String systemPrompt, String userPrompt, Consumer<String> tokenConsumer) {
        requireApiKey();
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "input", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "reasoning", Map.of("effort", "low"),
                    "text", Map.of("verbosity", "low"),
                    "max_output_tokens", 1200,
                    "stream", true
            );
            HttpRequest request = HttpRequest.newBuilder(URI.create(responsesUrl))
                    .timeout(Duration.ofSeconds(120))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();
            HttpResponse<java.util.stream.Stream<String>> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                try (var lines = response.body()) {
                    throw new IllegalStateException("OpenAI Marketing streaming failed with HTTP "
                            + response.statusCode() + ": " + lines.limit(5).reduce("", String::concat));
                }
            }
            StringBuilder completeText = new StringBuilder();
            try (var lines = response.body()) {
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring(5).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> consumeTextDelta(data, completeText, tokenConsumer));
            }
            if (completeText.isEmpty()) {
                throw new IllegalStateException("OpenAI Marketing streaming response is empty");
            }
            return completeText.toString();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("OpenAI Marketing streaming was interrupted", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("OpenAI Marketing streaming response is invalid", exception);
        }
    }

    @Override
    public String generateJsonWithWebSearch(String systemPrompt, String userPrompt) {
        requireApiKey();
        Map<String, Object> request = Map.of(
                "model", model,
                "input", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "tools", List.of(Map.of("type", "web_search")),
                "reasoning", Map.of("effort", "low"),
                // Web Search and JSON mode cannot be combined by the Responses API.
                // The caller still requests JSON in its prompt and validates every field.
                "text", Map.of("verbosity", "low"),
                "max_output_tokens", 2400
        );
        String response = restClient.post().uri(responsesUrl)
                .headers(headers -> headers.setBearerAuth(apiKey))
                .contentType(MediaType.APPLICATION_JSON).body(request).retrieve().body(String.class);
        return extractOutputText(response);
    }

    private String extractOutputText(String response) {
        if (response == null || response.isBlank()) throw new IllegalStateException("OpenAI response is empty");
        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.path("output_text").isTextual()) return root.path("output_text").asText();
            for (JsonNode output : root.path("output")) {
                for (JsonNode content : output.path("content")) {
                    if (content.path("text").isTextual() && !content.path("text").asText().isBlank()) {
                        return content.path("text").asText();
                    }
                }
            }
            throw new IllegalStateException("OpenAI response text not found");
        } catch (Exception exception) {
            throw new IllegalStateException("OpenAI response is invalid", exception);
        }
    }

    private void consumeTextDelta(String data, StringBuilder completeText, Consumer<String> tokenConsumer) {
        try {
            JsonNode event = objectMapper.readTree(data);
            if (!"response.output_text.delta".equals(event.path("type").asText())) return;
            String delta = event.path("delta").asText("");
            if (delta.isEmpty()) return;
            completeText.append(delta);
            tokenConsumer.accept(delta);
        } catch (Exception exception) {
            throw new IllegalStateException("Invalid OpenAI streaming event", exception);
        }
    }

    private void requireApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is required for the Marketing Coach");
        }
    }
}
