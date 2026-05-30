package hwc_backend.service;

import hwc_backend.dto.rapport.RapportGenerationResult;

public interface OpenAIService {

    RapportGenerationResult genererRapportDiagnostic(String contexte);
}
