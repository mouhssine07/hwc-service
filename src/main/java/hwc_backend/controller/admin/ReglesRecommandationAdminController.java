package hwc_backend.controller.admin;

import hwc_backend.dto.recommandation.RegleRecommandationDTO;
import hwc_backend.service.RecommandationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/regles-recommandation")
@RequiredArgsConstructor
public class ReglesRecommandationAdminController {

    private final RecommandationService recommandationService;

    @GetMapping
    public ResponseEntity<List<RegleRecommandationDTO>> getAll() {
        return ResponseEntity.ok(recommandationService.getRegles());
    }

    @PostMapping
    public ResponseEntity<RegleRecommandationDTO> create(@Valid @RequestBody RegleRecommandationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recommandationService.createRegle(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegleRecommandationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RegleRecommandationDTO dto
    ) {
        return ResponseEntity.ok(recommandationService.updateRegle(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recommandationService.deleteRegle(id);
        return ResponseEntity.noContent().build();
    }
}
