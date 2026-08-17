package hwc_backend.controller.admin;

import hwc_backend.dto.admin.AdminClientDTO;
import hwc_backend.dto.admin.UpdateClientStatusDTO;
import hwc_backend.entity.User;
import hwc_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users/clients")
@RequiredArgsConstructor
public class AdminClientsController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<AdminClientDTO>> getClients(Pageable pageable) {
        return ResponseEntity.ok(userRepository
                .findDistinctByRolesNom("ROLE_CLIENT", pageable)
                .map(this::toDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminClientDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientStatusDTO request
    ) {
        User client = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        boolean isClient = client.getRoles().stream()
                .anyMatch(role -> "ROLE_CLIENT".equals(role.getNom()));
        if (!isClient) {
            throw new EntityNotFoundException("Client not found");
        }

        client.setActif(request.getActif());
        return ResponseEntity.ok(toDTO(userRepository.save(client)));
    }

    private AdminClientDTO toDTO(User user) {
        return new AdminClientDTO(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getEntreprise(),
                user.getSecteur(),
                user.getTailleEntreprise(),
                user.getTelephone(),
                user.isActif(),
                user.getDateCreation(),
                user.getDateDerniereConnexion()
        );
    }
}
