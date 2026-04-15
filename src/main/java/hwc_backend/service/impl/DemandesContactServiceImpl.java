package hwc_backend.service.impl;

import hwc_backend.dto.DemandesContactDTO;
import hwc_backend.entity.DemandesContact;
import hwc_backend.repository.DemandesContactRepository;
import hwc_backend.service.DemandesContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class DemandesContactServiceImpl implements DemandesContactService {

    private final DemandesContactRepository repository;

    @Override
    public List<DemandesContactDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DemandesContactDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("DemandeContact not found with id: " + id));
    }

    @Override
    public DemandesContactDTO create(DemandesContactDTO dto) {
        DemandesContact entity = new DemandesContact();
        entity.setEmail(dto.getEmail());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private DemandesContactDTO toDTO(DemandesContact entity) {
        return new DemandesContactDTO(entity.getId(), entity.getEmail());
    }
}
