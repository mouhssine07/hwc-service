package hwc_backend.service.impl;

import hwc_backend.dto.TemoignagesDTO;
import hwc_backend.entity.Temoignages;
import hwc_backend.repository.TemoignagesRepository;
import hwc_backend.service.TemoignagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class TemoignagesServiceImpl implements TemoignagesService {

    private final TemoignagesRepository repository;

    @Override
    public List<TemoignagesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TemoignagesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Temoignage not found with id: " + id));
    }

    @Override
    public TemoignagesDTO create(TemoignagesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public TemoignagesDTO update(Long id, TemoignagesDTO dto) {
        Temoignages entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Temoignage not found with id: " + id));
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setType(dto.getType());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TemoignagesDTO toDTO(Temoignages entity) {
        return new TemoignagesDTO(entity.getId(), entity.getNom(), entity.getLogoUrl(), entity.getType());
    }

    private Temoignages toEntity(TemoignagesDTO dto) {
        Temoignages entity = new Temoignages();
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setType(dto.getType());
        return entity;
    }
}
