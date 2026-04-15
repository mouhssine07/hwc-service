package hwc_backend.service.impl;

import hwc_backend.dto.ServiceFonctionnalitesDTO;
import hwc_backend.entity.ServiceFonctionnalites;
import hwc_backend.entity.Services;
import hwc_backend.repository.ServiceFonctionnalitesRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.service.ServiceFonctionnalitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class ServiceFonctionnalitesServiceImpl implements ServiceFonctionnalitesService {

    private final ServiceFonctionnalitesRepository repository;
    private final ServicesRepository servicesRepository;

    @Override
    public List<ServiceFonctionnalitesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceFonctionnalitesDTO> getByServiceId(Long serviceId) {
        return repository.findAll().stream()
                .filter(sf -> sf.getService() != null && sf.getService().getId().equals(serviceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceFonctionnalitesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("ServiceFonctionnalite not found with id: " + id));
    }

    @Override
    public ServiceFonctionnalitesDTO create(ServiceFonctionnalitesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public ServiceFonctionnalitesDTO update(Long id, ServiceFonctionnalitesDTO dto) {
        ServiceFonctionnalites entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceFonctionnalite not found with id: " + id));
        entity.setContenu(dto.getContenu());
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

    private ServiceFonctionnalitesDTO toDTO(ServiceFonctionnalites entity) {
        return new ServiceFonctionnalitesDTO(
                entity.getId(),
                entity.getContenu(),
                entity.getService() != null ? entity.getService().getId() : null
        );
    }

    private ServiceFonctionnalites toEntity(ServiceFonctionnalitesDTO dto) {
        ServiceFonctionnalites entity = new ServiceFonctionnalites();
        entity.setContenu(dto.getContenu());
        if (dto.getServiceId() != null) {
            Services service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + dto.getServiceId()));
            entity.setService(service);
        }
        return entity;
    }
}
