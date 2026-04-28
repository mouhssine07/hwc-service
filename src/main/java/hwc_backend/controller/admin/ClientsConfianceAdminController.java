package hwc_backend.controller.admin;

import hwc_backend.dto.ClientsConfianceDTO;
import hwc_backend.service.ClientsConfianceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/clients-confiance")
@RequiredArgsConstructor
public class ClientsConfianceAdminController {

    private final ClientsConfianceService service;

    @GetMapping
    public ResponseEntity<Page<ClientsConfianceDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientsConfianceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientsConfianceDTO> create(@Valid @RequestBody ClientsConfianceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientsConfianceDTO> update(@PathVariable Long id, @Valid @RequestBody ClientsConfianceDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
