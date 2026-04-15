package hwc_backend.service.impl;

import hwc_backend.dto.ClientsConfianceDTO;
import hwc_backend.entity.ClientsConfiance;
import hwc_backend.repository.ClientsConfianceRepository;
import hwc_backend.service.ClientsConfianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class ClientsConfianceServiceImpl implements ClientsConfianceService {

    private final ClientsConfianceRepository repository;

    @Override
    public List<ClientsConfianceDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientsConfianceDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("ClientsConfiance not found with id: " + id));
    }

    @Override
    public ClientsConfianceDTO create(ClientsConfianceDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public ClientsConfianceDTO update(Long id, ClientsConfianceDTO dto) {
        ClientsConfiance entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientsConfiance not found with id: " + id));
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ClientsConfianceDTO toDTO(ClientsConfiance entity) {
        return new ClientsConfianceDTO(entity.getId(), entity.getNom(), entity.getLogoUrl());
    }

    private ClientsConfiance toEntity(ClientsConfianceDTO dto) {
        ClientsConfiance entity = new ClientsConfiance();
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        return entity;
    }
}
