package hwc_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.dto.rapport.RapportGenerationResult;
import hwc_backend.service.OpenAIService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api-key:}")
    private String apiKey;

    @Value("${openai.model:gpt-5.4-mini}")
    private String model;

    @Value("${openai.responses-url:https://api.openai.com/v1/responses}")
    private String responsesUrl;

    @Override
    public RapportGenerationResult genererRapportDiagnostic(String contexte) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackRapport(contexte);
        }

        try {
            String responseText = callResponsesApi(contexte);
            return parseRapport(responseText);
        } catch (RuntimeException exception) {
            return fallbackRapport(contexte);
        }
    }

    @Override
    public String generateText(List<Map<String, String>> messages, int maxOutputTokens) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is required");
        }
        return callResponsesApi(messages, maxOutputTokens);
    }

    private String callResponsesApi(String contexte) {
        return callResponsesApi(
                List.of(
                        Map.of(
                                "role", "system",
                                "content", "Tu es un consultant senior de Harmony Works Consulting. Redige en francais un rapport executif clair, professionnel et actionnable. Retourne uniquement un JSON valide avec les cles introduction, analyseForts, analyseFaibles, planAction, conclusion."
                        ),
                        Map.of(
                                "role", "user",
                                "content", contexte
                        )
                ),
                1400
        );
    }

    private String callResponsesApi(List<Map<String, String>> messages, int maxOutputTokens) {
        RestClient restClient = RestClient.builder().build();
        Map<String, Object> request = Map.of(
                "model", model,
                "input", messages,
                "max_output_tokens", maxOutputTokens
        );

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(request);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to serialize OpenAI request", exception);
        }

        String responseBody = restClient.post()
                .uri(responsesUrl)
                .headers(headers -> headers.setBearerAuth(apiKey))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("OpenAI response is empty");
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(responseBody);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse OpenAI response", exception);
        }

        JsonNode outputText = root.get("output_text");
        if (outputText != null && outputText.isTextual()) {
            return outputText.asText();
        }

        JsonNode output = root.path("output");
        if (output.isArray()) {
            for (JsonNode item : output) {
                JsonNode content = item.path("content");
                if (content.isArray()) {
                    for (JsonNode contentItem : content) {
                        JsonNode text = contentItem.get("text");
                        if (text != null && text.isTextual()) {
                            return text.asText();
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("OpenAI response text not found");
    }

    private RapportGenerationResult parseRapport(String responseText) {
        try {
            JsonNode json = objectMapper.readTree(responseText);
            return new RapportGenerationResult(
                    json.path("introduction").asText(),
                    json.path("analyseForts").asText(),
                    json.path("analyseFaibles").asText(),
                    json.path("planAction").asText(),
                    json.path("conclusion").asText(),
                    null
            );
        } catch (Exception exception) {
            return new RapportGenerationResult(
                    responseText,
                    "Les points forts sont detailles dans l'analyse globale du diagnostic.",
                    "Les axes faibles doivent etre traites en priorite selon les recommandations generees.",
                    "Le plan d'action reprend les recommandations HWC classees par priorite.",
                    "Ce rapport doit servir de base a une discussion avec un consultant HWC.",
                    null
            );
        }
    }

    private RapportGenerationResult fallbackRapport(String contexte) {
        return new RapportGenerationResult(
                "Ce rapport synthetise le diagnostic HWC 360 et transforme les scores en priorites de decision.",
                "Les categories avec les meilleurs scores constituent les points d'appui pour accelerer la transformation.",
                "Les categories faibles ou critiques demandent un traitement prioritaire afin de reduire les risques operationnels et commerciaux.",
                "Prioriser les recommandations de court terme, structurer les chantiers de moyen terme, puis planifier les transformations de fond.",
                "La prochaine etape consiste a valider ce plan avec les equipes et a suivre l'evolution lors d'un nouveau diagnostic.",
                null
        );
    }
}
