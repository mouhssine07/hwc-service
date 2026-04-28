package hwc_backend.controller.admin;

import hwc_backend.dto.SousServiceAvantagesDTO;
import hwc_backend.service.SousServiceAvantagesService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sous-service-avantages")
@RequiredArgsConstructor
public class SousServiceAvantagesAdminController {

    private final SousServiceAvantagesService service;

    @GetMapping
    public ResponseEntity<Page<SousServiceAvantagesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServiceAvantagesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<SousServiceAvantagesDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<SousServiceAvantagesDTO> create(@Valid @RequestBody SousServiceAvantagesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServiceAvantagesDTO> update(@PathVariable Long id, @Valid @RequestBody SousServiceAvantagesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
