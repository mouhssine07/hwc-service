package hwc_backend.controller;

import hwc_backend.dto.SousServicesDTO;
import hwc_backend.service.SousServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/sous-services")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SousServicesController {

    private final SousServicesService service;

    @GetMapping
    public ResponseEntity<List<SousServicesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SousServicesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<SousServicesDTO>> getByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(service.getByServiceId(serviceId));
    }

    @PostMapping
    public ResponseEntity<SousServicesDTO> create(@RequestBody SousServicesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SousServicesDTO> update(@PathVariable Long id, @RequestBody SousServicesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
