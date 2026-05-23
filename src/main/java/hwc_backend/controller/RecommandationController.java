package hwc_backend.controller;

import hwc_backend.dto.recommandation.PlanActionDTO;
import hwc_backend.dto.recommandation.RecommandationDTO;
import hwc_backend.service.RecommandationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/diagnostics")
@RequiredArgsConstructor
public class RecommandationController {

    private final RecommandationService recommandationService;

    @GetMapping("/{id}/recommandations")
    public ResponseEntity<List<RecommandationDTO>> recommandations(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(recommandationService.getRecommandations(id, authentication.getName()));
    }

    @GetMapping("/{id}/plan-action")
    public ResponseEntity<PlanActionDTO> planAction(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(recommandationService.getPlanAction(id, authentication.getName()));
    }
}
