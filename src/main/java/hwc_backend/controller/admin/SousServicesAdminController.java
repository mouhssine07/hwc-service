package hwc_backend.controller.admin;

import hwc_backend.dto.SousServicesDTO;
import hwc_backend.service.SousServicesService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sous-services")
@RequiredArgsConstructor
public class SousServicesAdminController {

    private final SousServicesService service;

    @GetMapping
    public ResponseEntity<Page<SousServicesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServicesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<SousServicesDTO>> getByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(service.getByServiceId(serviceId));
    }

    @PostMapping
    public ResponseEntity<SousServicesDTO> create(@Valid @RequestBody SousServicesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServicesDTO> update(@PathVariable Long id, @Valid @RequestBody SousServicesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
