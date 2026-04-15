package hwc_backend.service.impl;

import hwc_backend.dto.EtiquettesDTO;
import hwc_backend.dto.ServicesDTO;
import hwc_backend.entity.Services;
import hwc_backend.repository.ServiceEtiquettesRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository repository;
    private final ServiceEtiquettesRepository serviceEtiquettesRepository;

    @Override
    public List<ServicesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @Override
    public ServicesDTO create(ServicesDTO dto) {
        Services entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public ServicesDTO update(Long id, ServicesDTO dto) {
        Services entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        entity.setAccroche(dto.getAccroche());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setIcone(dto.getIcone());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ServicesDTO toDTO(Services entity) {
        List<EtiquettesDTO> etiquettes = serviceEtiquettesRepository.findByService(entity)
                .stream()
                .map(se -> new EtiquettesDTO(se.getEtiquette().getId(), se.getEtiquette().getNom()))
                .collect(Collectors.toList());

        return new ServicesDTO(
                entity.getId(),
                entity.getAccroche(),
                entity.getTitre(),
                entity.getDescription(),
                entity.getIcone(),
                etiquettes
        );
    }

    private Services toEntity(ServicesDTO dto) {
        Services entity = new Services();
        entity.setAccroche(dto.getAccroche());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setIcone(dto.getIcone());
        return entity;
    }
}
