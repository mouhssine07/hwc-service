package hwc_backend.service;

import hwc_backend.dto.diagnostic.CategorieDiagnosticDTO;
import hwc_backend.dto.diagnostic.DiagnosticResultatDTO;
import hwc_backend.dto.diagnostic.DiagnosticStartDTO;
import hwc_backend.dto.diagnostic.RepondreQuestionDTO;
import java.util.List;

public interface DiagnosticService {

    DiagnosticStartDTO start(String email);

    List<CategorieDiagnosticDTO> getQuestions();

    DiagnosticResultatDTO repondre(Long diagnosticId, RepondreQuestionDTO request, String email);

    DiagnosticResultatDTO finalizeDiagnostic(Long diagnosticId, String email);

    List<DiagnosticResultatDTO> history(String email);

    DiagnosticResultatDTO getById(Long diagnosticId, String email);
}
