package hwc_backend.controller;

import hwc_backend.dto.coach.CoachSemaineDTO;
import hwc_backend.dto.coach.CoachObjectifProgressionDTO;
import hwc_backend.service.CoachIAService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/coach")
@RequiredArgsConstructor
public class CoachIAController {

    private final CoachIAService coachIAService;

    @GetMapping("/current-week")
    public ResponseEntity<CoachSemaineDTO> currentWeek(
            @RequestParam(required = false) Long diagnosticId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(coachIAService.getCurrentWeek(authentication.getName(), diagnosticId));
    }

    @PatchMapping("/objectifs/{id}/complete")
    public ResponseEntity<CoachSemaineDTO> complete(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(coachIAService.completeObjective(id, authentication.getName()));
    }

    @PatchMapping("/objectifs/{id}/progress")
    public ResponseEntity<CoachSemaineDTO> updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody CoachObjectifProgressionDTO progression,
            Authentication authentication
    ) {
        return ResponseEntity.ok(coachIAService.updateObjectiveProgress(id, progression, authentication.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<CoachSemaineDTO>> history(
            @RequestParam(required = false) Long diagnosticId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(coachIAService.getHistory(authentication.getName(), diagnosticId));
    }
}
