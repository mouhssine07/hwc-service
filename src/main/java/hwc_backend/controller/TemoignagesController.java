package hwc_backend.controller;

import hwc_backend.dto.TemoignagesDTO;
import hwc_backend.service.TemoignagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/temoignages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TemoignagesController {

    private final TemoignagesService service;

    @GetMapping
    public ResponseEntity<List<TemoignagesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemoignagesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<TemoignagesDTO> create(@RequestBody TemoignagesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemoignagesDTO> update(@PathVariable Long id, @RequestBody TemoignagesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
