package hwc_backend.service.impl;

import hwc_backend.dto.SousServiceAvantagesDTO;
import hwc_backend.entity.SousServiceAvantages;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.SousServiceAvantagesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.SousServiceAvantagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class SousServiceAvantagesServiceImpl implements SousServiceAvantagesService {

    private final SousServiceAvantagesRepository repository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    public List<SousServiceAvantagesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SousServiceAvantagesDTO> getBySousServiceId(Long sousServiceId) {
        return repository.findAll().stream()
                .filter(a -> a.getSousService() != null && a.getSousService().getId().equals(sousServiceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SousServiceAvantagesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("SousServiceAvantage not found with id: " + id));
    }

    @Override
    public SousServiceAvantagesDTO create(SousServiceAvantagesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SousServiceAvantagesDTO update(Long id, SousServiceAvantagesDTO dto) {
        SousServiceAvantages entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousServiceAvantage not found with id: " + id));
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

    private SousServiceAvantagesDTO toDTO(SousServiceAvantages entity) {
        return new SousServiceAvantagesDTO(
                entity.getId(),
                entity.getTitre(),
                entity.getDescription(),
                entity.getSousService() != null ? entity.getSousService().getId() : null
        );
    }

    private SousServiceAvantages toEntity(SousServiceAvantagesDTO dto) {
        SousServiceAvantages entity = new SousServiceAvantages();
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
