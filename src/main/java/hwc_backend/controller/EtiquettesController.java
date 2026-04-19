package hwc_backend.controller;

import hwc_backend.dto.EtiquettesDTO;
import hwc_backend.service.EtiquettesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/etiquettes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EtiquettesController {

    private final EtiquettesService service;

    @GetMapping
    public ResponseEntity<List<EtiquettesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquettesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<EtiquettesDTO> create(@RequestBody EtiquettesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquettesDTO> update(@PathVariable Long id, @RequestBody EtiquettesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
