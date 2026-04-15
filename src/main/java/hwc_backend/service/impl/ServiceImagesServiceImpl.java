package hwc_backend.service.impl;

import hwc_backend.dto.ServiceImagesDTO;
import hwc_backend.entity.ServiceImages;
import hwc_backend.entity.Services;
import hwc_backend.repository.ServiceImagesRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.service.ServiceImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class ServiceImagesServiceImpl implements ServiceImagesService {

    private final ServiceImagesRepository repository;
    private final ServicesRepository servicesRepository;

    @Override
    public List<ServiceImagesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceImagesDTO> getByServiceId(Long serviceId) {
        return repository.findAll().stream()
                .filter(si -> si.getService() != null && si.getService().getId().equals(serviceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceImagesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("ServiceImage not found with id: " + id));
    }

    @Override
    public ServiceImagesDTO create(ServiceImagesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public ServiceImagesDTO update(Long id, ServiceImagesDTO dto) {
        ServiceImages entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceImage not found with id: " + id));
        entity.setImageUrl(dto.getImageUrl());
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

    private ServiceImagesDTO toDTO(ServiceImages entity) {
        return new ServiceImagesDTO(
                entity.getId(),
                entity.getImageUrl(),
                entity.getService() != null ? entity.getService().getId() : null
        );
    }

    private ServiceImages toEntity(ServiceImagesDTO dto) {
        ServiceImages entity = new ServiceImages();
        entity.setImageUrl(dto.getImageUrl());
        if (dto.getServiceId() != null) {
            Services service = servicesRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + dto.getServiceId()));
            entity.setService(service);
        }
        return entity;
    }
}
