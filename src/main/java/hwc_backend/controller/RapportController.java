package hwc_backend.controller;

import hwc_backend.dto.rapport.RapportPdfDTO;
import hwc_backend.entity.RapportPdf;
import hwc_backend.service.RapportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class RapportController {

    private final RapportService rapportService;

    @GetMapping("/diagnostics/{id}/pdf")
    public ResponseEntity<byte[]> genererRapport(
            @PathVariable Long id,
            Authentication authentication
    ) {
        RapportPdf rapport = rapportService.genererRapportDiagnostic(id, authentication.getName());
        return pdfResponse(rapport);
    }

    @GetMapping("/rapports")
    public ResponseEntity<List<RapportPdfDTO>> historique(Authentication authentication) {
        return ResponseEntity.ok(rapportService.getHistorique(authentication.getName()));
    }

    @GetMapping("/rapports/{id}/download")
    public ResponseEntity<byte[]> telechargerRapport(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return pdfResponse(rapportService.getRapport(id, authentication.getName()));
    }

    private ResponseEntity<byte[]> pdfResponse(RapportPdf rapport) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(rapport.getFileName())
                        .build()
                        .toString())
                .body(rapport.getPdfContent());
    }
}
