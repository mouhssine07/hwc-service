package hwc_backend.controller.publicapi;

import hwc_backend.dto.DemandesContactDTO;
import hwc_backend.service.DemandesContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/demandes-contact")
@RequiredArgsConstructor
public class PublicDemandesContactController {

    private final DemandesContactService service;

    @PostMapping
    public ResponseEntity<DemandesContactDTO> create(@Valid @RequestBody DemandesContactDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }
}
