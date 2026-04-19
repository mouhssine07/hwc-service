package hwc_backend.controller;

import hwc_backend.dto.ServiceImagesDTO;
import hwc_backend.service.ServiceImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/service-images")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServiceImagesController {

    private final ServiceImagesService service;

    @GetMapping
    public ResponseEntity<List<ServiceImagesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceImagesDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<ServiceImagesDTO>> getByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(service.getByServiceId(serviceId));
    }

    @PostMapping
    public ResponseEntity<ServiceImagesDTO> create(@RequestBody ServiceImagesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceImagesDTO> update(@PathVariable Long id, @RequestBody ServiceImagesDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
