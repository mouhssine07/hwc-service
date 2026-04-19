package hwc_backend.controller;

import hwc_backend.dto.SousServiceEtapesDTO;
import hwc_backend.service.SousServiceEtapesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/sous-service-etapes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SousServiceEtapesController {

    private final SousServiceEtapesService service;

    @GetMapping
    public ResponseEntity<List<SousServiceEtapesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServiceEtapesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<SousServiceEtapesDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<SousServiceEtapesDTO> create(@RequestBody SousServiceEtapesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServiceEtapesDTO> update(@PathVariable Long id, @RequestBody SousServiceEtapesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
