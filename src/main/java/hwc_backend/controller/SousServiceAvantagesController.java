package hwc_backend.controller;

import hwc_backend.dto.SousServiceAvantagesDTO;
import hwc_backend.service.SousServiceAvantagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/sous-service-avantages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SousServiceAvantagesController {

    private final SousServiceAvantagesService service;

    @GetMapping
    public ResponseEntity<List<SousServiceAvantagesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServiceAvantagesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<SousServiceAvantagesDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<SousServiceAvantagesDTO> create(@RequestBody SousServiceAvantagesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServiceAvantagesDTO> update(@PathVariable Long id, @RequestBody SousServiceAvantagesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
