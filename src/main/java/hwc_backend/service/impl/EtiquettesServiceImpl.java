package hwc_backend.service.impl;

import hwc_backend.dto.EtiquettesDTO;
import hwc_backend.entity.Etiquettes;
import hwc_backend.repository.EtiquettesRepository;
import hwc_backend.service.EtiquettesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class EtiquettesServiceImpl implements EtiquettesService {

    private final EtiquettesRepository repository;

    @Override
    public List<EtiquettesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EtiquettesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Etiquette not found with id: " + id));
    }

    @Override
    public EtiquettesDTO create(EtiquettesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public EtiquettesDTO update(Long id, EtiquettesDTO dto) {
        Etiquettes entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiquette not found with id: " + id));
        entity.setNom(dto.getNom());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private EtiquettesDTO toDTO(Etiquettes entity) {
        return new EtiquettesDTO(entity.getId(), entity.getNom());
    }

    private Etiquettes toEntity(EtiquettesDTO dto) {
        Etiquettes entity = new Etiquettes();
        entity.setNom(dto.getNom());
        return entity;
    }
}
