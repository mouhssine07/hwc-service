package hwc_backend.controller;

import hwc_backend.dto.ServiceFonctionnalitesDTO;
import hwc_backend.service.ServiceFonctionnalitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/service-fonctionnalites")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServiceFonctionnalitesController {

    private final ServiceFonctionnalitesService service;

    @GetMapping
    public ResponseEntity<List<ServiceFonctionnalitesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceFonctionnalitesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<ServiceFonctionnalitesDTO>> getByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(service.getByServiceId(serviceId));
    }

    @PostMapping
    public ResponseEntity<ServiceFonctionnalitesDTO> create(@RequestBody ServiceFonctionnalitesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceFonctionnalitesDTO> update(@PathVariable Long id, @RequestBody ServiceFonctionnalitesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
