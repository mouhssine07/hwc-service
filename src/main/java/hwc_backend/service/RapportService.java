package hwc_backend.service;

import hwc_backend.dto.rapport.RapportPdfDTO;
import hwc_backend.entity.RapportPdf;
import java.util.List;

public interface RapportService {

    RapportPdf genererRapportDiagnostic(Long diagnosticId, String email);

    List<RapportPdfDTO> getHistorique(String email);

    RapportPdf getRapport(Long rapportId, String email);
}
