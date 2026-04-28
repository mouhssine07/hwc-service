package hwc_backend.controller.admin;

import hwc_backend.dto.TemoignagesDTO;
import hwc_backend.service.TemoignagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/temoignages")
@RequiredArgsConstructor
public class TemoignagesAdminController {

    private final TemoignagesService service;

    @GetMapping
    public ResponseEntity<Page<TemoignagesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemoignagesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<TemoignagesDTO> create(@Valid @RequestBody TemoignagesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemoignagesDTO> update(@PathVariable Long id, @Valid @RequestBody TemoignagesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
