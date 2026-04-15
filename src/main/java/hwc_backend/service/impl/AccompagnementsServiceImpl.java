package hwc_backend.service.impl;

import hwc_backend.dto.AccompagnementsDTO;
import hwc_backend.entity.Accompagnements;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.AccompagnementsRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.AccompagnementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class AccompagnementsServiceImpl implements AccompagnementsService {

    private final AccompagnementsRepository repository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    public List<AccompagnementsDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccompagnementsDTO> getBySousServiceId(Long sousServiceId) {
        return repository.findAll().stream()
                .filter(a -> a.getSousService() != null && a.getSousService().getId().equals(sousServiceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccompagnementsDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Accompagnement not found with id: " + id));
    }

    @Override
    public AccompagnementsDTO create(AccompagnementsDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public AccompagnementsDTO update(Long id, AccompagnementsDTO dto) {
        Accompagnements entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accompagnement not found with id: " + id));
        entity.setTitre(dto.getTitre());
        entity.setAccroche(dto.getAccroche());
        entity.setDescription1(dto.getDescription1());
        entity.setDescription2(dto.getDescription2());
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

    private AccompagnementsDTO toDTO(Accompagnements entity) {
        return new AccompagnementsDTO(
                entity.getId(),
                entity.getTitre(),
                entity.getAccroche(),
                entity.getDescription1(),
                entity.getDescription2(),
                entity.getSousService() != null ? entity.getSousService().getId() : null
        );
    }

    private Accompagnements toEntity(AccompagnementsDTO dto) {
        Accompagnements entity = new Accompagnements();
        entity.setTitre(dto.getTitre());
        entity.setAccroche(dto.getAccroche());
        entity.setDescription1(dto.getDescription1());
        entity.setDescription2(dto.getDescription2());
        if (dto.getSousServiceId() != null) {
            SousServices sousService = sousServicesRepository.findById(dto.getSousServiceId())
                    .orElseThrow(() -> new RuntimeException("SousService not found with id: " + dto.getSousServiceId()));
            entity.setSousService(sousService);
        }
        return entity;
    }
}
