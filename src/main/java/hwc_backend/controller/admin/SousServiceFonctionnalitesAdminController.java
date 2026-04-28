package hwc_backend.controller.admin;

import hwc_backend.dto.SousServiceFonctionnalitesDTO;
import hwc_backend.service.SousServiceFonctionnalitesService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sous-service-fonctionnalites")
@RequiredArgsConstructor
public class SousServiceFonctionnalitesAdminController {

    private final SousServiceFonctionnalitesService service;

    @GetMapping
    public ResponseEntity<Page<SousServiceFonctionnalitesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServiceFonctionnalitesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<SousServiceFonctionnalitesDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<SousServiceFonctionnalitesDTO> create(@Valid @RequestBody SousServiceFonctionnalitesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServiceFonctionnalitesDTO> update(@PathVariable Long id, @Valid @RequestBody SousServiceFonctionnalitesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
