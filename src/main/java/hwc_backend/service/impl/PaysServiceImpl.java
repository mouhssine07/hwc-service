package hwc_backend.service.impl;

import hwc_backend.dto.PaysDTO;
import hwc_backend.entity.Pays;
import hwc_backend.repository.PaysRepository;
import hwc_backend.service.PaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class PaysServiceImpl implements PaysService {

    private final PaysRepository repository;

    @Override
    public List<PaysDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaysDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pays not found with id: " + id));
    }

    @Override
    public PaysDTO create(PaysDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public PaysDTO update(Long id, PaysDTO dto) {
        Pays entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pays not found with id: " + id));
        entity.setNom(dto.getNom());
        entity.setCodePays(dto.getCodePays());
        entity.setVille(dto.getVille());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private PaysDTO toDTO(Pays entity) {
        return new PaysDTO(entity.getId(), entity.getNom(), entity.getCodePays(), entity.getVille());
    }

    private Pays toEntity(PaysDTO dto) {
        Pays entity = new Pays();
        entity.setNom(dto.getNom());
        entity.setCodePays(dto.getCodePays());
        entity.setVille(dto.getVille());
        return entity;
    }
}
