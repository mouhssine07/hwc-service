package hwc_backend.controller;

import hwc_backend.dto.AccompagnementsDTO;
import hwc_backend.service.AccompagnementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/accompagnements")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AccompagnementsController {

    private final AccompagnementsService service;

    @GetMapping
    public ResponseEntity<List<AccompagnementsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccompagnementsDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-sous-service/{sousServiceId}")
    public ResponseEntity<List<AccompagnementsDTO>> getBySousServiceId(@PathVariable Long sousServiceId) {
        return ResponseEntity.ok(service.getBySousServiceId(sousServiceId));
    }

    @PostMapping
    public ResponseEntity<AccompagnementsDTO> create(@RequestBody AccompagnementsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccompagnementsDTO> update(@PathVariable Long id, @RequestBody AccompagnementsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
