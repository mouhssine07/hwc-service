package hwc_backend.controller;

import hwc_backend.dto.SousServiceFonctionnalitesDTO;
import hwc_backend.service.SousServiceFonctionnalitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/sous-service-fonctionnalites")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SousServiceFonctionnalitesController {

    private final SousServiceFonctionnalitesService service;

    @GetMapping
    public ResponseEntity<List<SousServiceFonctionnalitesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServiceFonctionnalitesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<SousServiceFonctionnalitesDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<SousServiceFonctionnalitesDTO> create(@RequestBody SousServiceFonctionnalitesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServiceFonctionnalitesDTO> update(@PathVariable Long id, @RequestBody SousServiceFonctionnalitesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
