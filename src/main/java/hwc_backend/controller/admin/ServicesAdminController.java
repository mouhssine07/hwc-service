package hwc_backend.controller.admin;

import hwc_backend.dto.ServicesDTO;
import hwc_backend.service.ServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class ServicesAdminController {

    private final ServicesService service;

    @GetMapping
    public ResponseEntity<Page<ServicesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ServicesDTO> create(@Valid @RequestBody ServicesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicesDTO> update(@PathVariable Long id, @Valid @RequestBody ServicesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
