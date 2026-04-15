package hwc_backend.service.impl;

import hwc_backend.dto.SousServicesDTO;
import hwc_backend.entity.Services;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.SousServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class SousServicesServiceImpl implements SousServicesService {

    private final SousServicesRepository repository;
    private final ServicesRepository servicesRepository;

    @Override
    public List<SousServicesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SousServicesDTO> getByServiceId(Long serviceId) {
        Services service = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        return repository.findAll().stream()
                .filter(ss -> ss.getService() != null && ss.getService().getId().equals(serviceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SousServicesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("SousService not found with id: " + id));
    }

    @Override
    public SousServicesDTO create(SousServicesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SousServicesDTO update(Long id, SousServicesDTO dto) {
        SousServices entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousService not found with id: " + id));
        entity.setTitre(dto.getTitre());
        entity.setAccroche(dto.getAccroche());
        entity.setDescription(dto.getDescription());
        entity.setDescriptionComplete(dto.getDescriptionComplete());
        entity.setIcone(dto.getIcone());
        if (dto.getServiceId() != null) {
            Services service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + dto.getServiceId()));
            entity.setService(service);
        }
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SousServicesDTO toDTO(SousServices entity) {
        return new SousServicesDTO(
                entity.getId(),
                entity.getTitre(),
                entity.getAccroche(),
                entity.getDescription(),
                entity.getDescriptionComplete(),
                entity.getIcone(),
                entity.getService() != null ? entity.getService().getId() : null,
                entity.getService() != null ? entity.getService().getTitre() : null
        );
    }

    private SousServices toEntity(SousServicesDTO dto) {
        SousServices entity = new SousServices();
        entity.setTitre(dto.getTitre());
        entity.setAccroche(dto.getAccroche());
        entity.setDescription(dto.getDescription());
        entity.setDescriptionComplete(dto.getDescriptionComplete());
        entity.setIcone(dto.getIcone());
        if (dto.getServiceId() != null) {
            Services service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + dto.getServiceId()));
            entity.setService(service);
        }
        return entity;
    }
}
