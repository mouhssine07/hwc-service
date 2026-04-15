package hwc_backend.service;

import hwc_backend.dto.CertificationsDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface CertificationsService {

    List<CertificationsDTO> getAll();
    CertificationsDTO getById(Long id);
    CertificationsDTO create(CertificationsDTO dto);
    CertificationsDTO update(Long id, CertificationsDTO dto);
    void delete(Long id);
}
