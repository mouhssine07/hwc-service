package hwc_backend.service;

import hwc_backend.dto.DemandesContactDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface DemandesContactService {

    List<DemandesContactDTO> getAll();
    DemandesContactDTO getById(Long id);
    DemandesContactDTO create(DemandesContactDTO dto);
    void delete(Long id);
}
