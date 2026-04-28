package hwc_backend.controller.admin;

import hwc_backend.dto.AccompagnementsDTO;
import hwc_backend.service.AccompagnementsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/accompagnements")
@RequiredArgsConstructor
public class AccompagnementsAdminController {

    private final AccompagnementsService service;

    @GetMapping
    public ResponseEntity<Page<AccompagnementsDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccompagnementsDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<AccompagnementsDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<AccompagnementsDTO> create(@Valid @RequestBody AccompagnementsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccompagnementsDTO> update(@PathVariable Long id, @Valid @RequestBody AccompagnementsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
