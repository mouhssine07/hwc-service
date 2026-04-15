package hwc_backend.service.impl;

import hwc_backend.dto.SousServiceFonctionnalitesDTO;
import hwc_backend.entity.SousServiceFonctionnalites;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.SousServiceFonctionnalitesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.SousServiceFonctionnalitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class SousServiceFonctionnalitesServiceImpl implements SousServiceFonctionnalitesService {

    private final SousServiceFonctionnalitesRepository repository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    public List<SousServiceFonctionnalitesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SousServiceFonctionnalitesDTO> getBySousServiceId(Long sousServiceId) {
        return repository.findAll().stream()
                .filter(f -> f.getSousService() != null && f.getSousService().getId().equals(sousServiceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SousServiceFonctionnalitesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("SousServiceFonctionnalite not found with id: " + id));
    }

    @Override
    public SousServiceFonctionnalitesDTO create(SousServiceFonctionnalitesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SousServiceFonctionnalitesDTO update(Long id, SousServiceFonctionnalitesDTO dto) {
        SousServiceFonctionnalites entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousServiceFonctionnalite not found with id: " + id));
        entity.setContenu(dto.getContenu());
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

    private SousServiceFonctionnalitesDTO toDTO(SousServiceFonctionnalites entity) {
        return new SousServiceFonctionnalitesDTO(
                entity.getId(),
                entity.getContenu(),
                entity.getSousService() != null ? entity.getSousService().getId() : null
        );
    }

    private SousServiceFonctionnalites toEntity(SousServiceFonctionnalitesDTO dto) {
        SousServiceFonctionnalites entity = new SousServiceFonctionnalites();
        entity.setContenu(dto.getContenu());
        if (dto.getSousServiceId() != null) {
            SousServices sousService = sousServicesRepository.findById(dto.getSousServiceId())
                    .orElseThrow(() -> new RuntimeException("SousService not found with id: " + dto.getSousServiceId()));
            entity.setSousService(sousService);
        }
        return entity;
    }
}
