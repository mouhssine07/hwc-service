package hwc_backend.controller;

import hwc_backend.dto.ChiffresClesDTO;
import hwc_backend.service.ChiffresClesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/chiffres-cles")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChiffresClesController {

    private final ChiffresClesService service;

    @GetMapping
    public ResponseEntity<List<ChiffresClesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChiffresClesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ChiffresClesDTO> create(@RequestBody ChiffresClesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChiffresClesDTO> update(@PathVariable Long id, @RequestBody ChiffresClesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
