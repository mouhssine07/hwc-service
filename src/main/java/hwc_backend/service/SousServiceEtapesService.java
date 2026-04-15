package hwc_backend.service;

import hwc_backend.dto.SousServiceEtapesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface SousServiceEtapesService {

    List<SousServiceEtapesDTO> getAll();
    List<SousServiceEtapesDTO> getBySousServiceId(Long sousServiceId);
    SousServiceEtapesDTO getById(Long id);
    SousServiceEtapesDTO create(SousServiceEtapesDTO dto);
    SousServiceEtapesDTO update(Long id, SousServiceEtapesDTO dto);
    void delete(Long id);
}
