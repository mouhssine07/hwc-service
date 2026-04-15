package hwc_backend.service.impl;

import hwc_backend.dto.CertificationsDTO;
import hwc_backend.entity.Certifications;
import hwc_backend.repository.CertificationsRepository;
import hwc_backend.service.CertificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SETUP GAME
 **/
@Service
@RequiredArgsConstructor
public class CertificationsServiceImpl implements CertificationsService {

    private final CertificationsRepository repository;

    @Override
    public List<CertificationsDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificationsDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Certification not found with id: " + id));
    }

    @Override
    public CertificationsDTO create(CertificationsDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public CertificationsDTO update(Long id, CertificationsDTO dto) {
        Certifications entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found with id: " + id));
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setType(dto.getType());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private CertificationsDTO toDTO(Certifications entity) {
        return new CertificationsDTO(entity.getId(), entity.getNom(), entity.getLogoUrl(), entity.getType());
    }

    private Certifications toEntity(CertificationsDTO dto) {
        Certifications entity = new Certifications();
        entity.setNom(dto.getNom());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setType(dto.getType());
        return entity;
    }
}
