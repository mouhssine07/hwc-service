package hwc_backend.controller;

import hwc_backend.dto.ClientsConfianceDTO;
import hwc_backend.service.ClientsConfianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SETUP GAME
 **/
@RestController
@RequestMapping("/api/clients-confiance")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientsConfianceController {

    private final ClientsConfianceService service;

    @GetMapping
    public ResponseEntity<List<ClientsConfianceDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientsConfianceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientsConfianceDTO> create(@RequestBody ClientsConfianceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientsConfianceDTO> update(@PathVariable Long id, @RequestBody ClientsConfianceDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
