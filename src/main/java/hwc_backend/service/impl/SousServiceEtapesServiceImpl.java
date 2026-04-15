package hwc_backend.service.impl;

import hwc_backend.dto.SousServiceEtapesDTO;
import hwc_backend.entity.SousServiceEtapes;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.SousServiceEtapesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.SousServiceEtapesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class SousServiceEtapesServiceImpl implements SousServiceEtapesService {

    private final SousServiceEtapesRepository repository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    public List<SousServiceEtapesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SousServiceEtapesDTO> getBySousServiceId(Long sousServiceId) {
        return repository.findAll().stream()
                .filter(e -> e.getSousService() != null && e.getSousService().getId().equals(sousServiceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SousServiceEtapesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("SousServiceEtape not found with id: " + id));
    }

    @Override
    public SousServiceEtapesDTO create(SousServiceEtapesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SousServiceEtapesDTO update(Long id, SousServiceEtapesDTO dto) {
        SousServiceEtapes entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousServiceEtape not found with id: " + id));
        entity.setNumero(dto.getNumero());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        if (dto.getSousServiceId() != null) {
            SousServices sousService = sousServicesRepository.findById(dto.getSousServiceId())
                    .orElseThrow(() -> new RuntimeException("SousService not found with id: " + dto.getSousServiceId()));
            entity.setSousService(sousService);
        }
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SousServiceEtapesDTO toDTO(SousServiceEtapes entity) {
        return new SousServiceEtapesDTO(
                entity.getId(),
                entity.getNumero(),
                entity.getTitre(),
                entity.getDescription(),
                entity.getSousService() != null ? entity.getSousService().getId() : null
        );
    }

    private SousServiceEtapes toEntity(SousServiceEtapesDTO dto) {
        SousServiceEtapes entity = new SousServiceEtapes();
        entity.setNumero(dto.getNumero());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        if (dto.getSousServiceId() != null) {
            SousServices sousService = sousServicesRepository.findById(dto.getSousServiceId())
                    .orElseThrow(() -> new RuntimeException("SousService not found with id: " + dto.getSousServiceId()));
            entity.setSousService(sousService);
        }
        return entity;
    }
}
