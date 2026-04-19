package hwc_backend.controller;

import hwc_backend.dto.DemandesContactDTO;
import hwc_backend.service.DemandesContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/demandes-contact")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DemandesContactController {

    private final DemandesContactService service;

    @GetMapping
    public ResponseEntity<List<DemandesContactDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandesContactDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<DemandesContactDTO> create(@RequestBody DemandesContactDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
