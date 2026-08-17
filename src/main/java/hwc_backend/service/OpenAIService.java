package hwc_backend.service;

import hwc_backend.dto.rapport.RapportGenerationResult;
import java.util.List;
import java.util.Map;

public interface OpenAIService {

    RapportGenerationResult genererRapportDiagnostic(String contexte);

    String generateText(List<Map<String, String>> messages, int maxOutputTokens);
}
