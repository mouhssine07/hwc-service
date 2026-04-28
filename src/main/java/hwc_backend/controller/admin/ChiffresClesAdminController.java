package hwc_backend.controller.admin;

import hwc_backend.dto.ChiffresClesDTO;
import hwc_backend.service.ChiffresClesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/chiffres-cles")
@RequiredArgsConstructor
public class ChiffresClesAdminController {

    private final ChiffresClesService service;

    @GetMapping
    public ResponseEntity<Page<ChiffresClesDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(AdminPageUtils.paginate(service.getAll(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChiffresClesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ChiffresClesDTO> create(@Valid @RequestBody ChiffresClesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChiffresClesDTO> update(@PathVariable Long id, @Valid @RequestBody ChiffresClesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
