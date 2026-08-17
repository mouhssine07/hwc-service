package hwc_backend.controller;

import hwc_backend.dto.diagnostic.CategorieDiagnosticDTO;
import hwc_backend.dto.diagnostic.DiagnosticResultatDTO;
import hwc_backend.dto.diagnostic.DiagnosticStartDTO;
import hwc_backend.dto.diagnostic.RepondreQuestionDTO;
import hwc_backend.service.DiagnosticService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/diagnostics")
@RequiredArgsConstructor
public class DiagnosticController {

    private final DiagnosticService diagnosticService;

    @PostMapping("/start")
    public ResponseEntity<DiagnosticStartDTO> start(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diagnosticService.start(authentication.getName()));
    }

    @GetMapping("/questions")
    public ResponseEntity<List<CategorieDiagnosticDTO>> questions() {
        return ResponseEntity.ok(diagnosticService.getQuestions());
    }

    @PostMapping("/{id}/reponses")
    public ResponseEntity<DiagnosticResultatDTO> repondre(
            @PathVariable Long id,
            @Valid @RequestBody RepondreQuestionDTO request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(diagnosticService.repondre(id, request, authentication.getName()));
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<DiagnosticResultatDTO> finalizeDiagnostic(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(diagnosticService.finalizeDiagnostic(id, authentication.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<DiagnosticResultatDTO>> history(Authentication authentication) {
        return ResponseEntity.ok(diagnosticService.history(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticResultatDTO> getById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(diagnosticService.getById(id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        diagnosticService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
