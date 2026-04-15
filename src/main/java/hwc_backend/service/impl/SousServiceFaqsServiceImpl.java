package hwc_backend.service.impl;

import hwc_backend.dto.SousServiceFaqsDTO;
import hwc_backend.entity.SousServiceFaqs;
import hwc_backend.entity.SousServices;
import hwc_backend.repository.SousServiceFaqsRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.service.SousServiceFaqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class SousServiceFaqsServiceImpl implements SousServiceFaqsService {

    private final SousServiceFaqsRepository repository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    public List<SousServiceFaqsDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SousServiceFaqsDTO> getBySousServiceId(Long sousServiceId) {
        return repository.findAll().stream()
                .filter(f -> f.getSousService() != null && f.getSousService().getId().equals(sousServiceId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SousServiceFaqsDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("SousServiceFaq not found with id: " + id));
    }

    @Override
    public SousServiceFaqsDTO create(SousServiceFaqsDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SousServiceFaqsDTO update(Long id, SousServiceFaqsDTO dto) {
        SousServiceFaqs entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousServiceFaq not found with id: " + id));
        entity.setQuestion(dto.getQuestion());
        entity.setReponse(dto.getReponse());
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

    private SousServiceFaqsDTO toDTO(SousServiceFaqs entity) {
        return new SousServiceFaqsDTO(
                entity.getId(),
                entity.getQuestion(),
                entity.getReponse(),
                entity.getSousService() != null ? entity.getSousService().getId() : null
        );
    }

    private SousServiceFaqs toEntity(SousServiceFaqsDTO dto) {
        SousServiceFaqs entity = new SousServiceFaqs();
        entity.setQuestion(dto.getQuestion());
        entity.setReponse(dto.getReponse());
        if (dto.getSousServiceId() != null) {
            SousServices sousService = sousServicesRepository.findById(dto.getSousServiceId())
                    .orElseThrow(() -> new RuntimeException("SousService not found with id: " + dto.getSousServiceId()));
            entity.setSousService(sousService);
        }
        return entity;
    }
}
