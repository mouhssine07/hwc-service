package hwc_backend.service.impl;

import hwc_backend.dto.ChiffresClesDTO;
import hwc_backend.entity.ChiffresCles;
import hwc_backend.repository.ChiffresClesRepository;
import hwc_backend.service.ChiffresClesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class ChiffresClesServiceImpl implements ChiffresClesService {

    private final ChiffresClesRepository repository;

    @Override
    public List<ChiffresClesDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ChiffresClesDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("ChiffresCles not found with id: " + id));
    }

    @Override
    public ChiffresClesDTO create(ChiffresClesDTO dto) {
        ChiffresCles entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public ChiffresClesDTO update(Long id, ChiffresClesDTO dto) {
        ChiffresCles entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChiffresCles not found with id: " + id));
        entity.setValeur(dto.getValeur());
        entity.setLibelle(dto.getLibelle());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ChiffresClesDTO toDTO(ChiffresCles entity) {
        return new ChiffresClesDTO(entity.getId(), entity.getValeur(), entity.getLibelle());
    }

    private ChiffresCles toEntity(ChiffresClesDTO dto) {
        ChiffresCles entity = new ChiffresCles();
        entity.setValeur(dto.getValeur());
        entity.setLibelle(dto.getLibelle());
        return entity;
    }
}
