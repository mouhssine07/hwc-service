package hwc_backend.service;

import hwc_backend.dto.SousServiceAvantagesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface SousServiceAvantagesService {

    List<SousServiceAvantagesDTO> getAll();
    List<SousServiceAvantagesDTO> getBySousServiceId(Long sousServiceId);
    SousServiceAvantagesDTO getById(Long id);
    SousServiceAvantagesDTO create(SousServiceAvantagesDTO dto);
    SousServiceAvantagesDTO update(Long id, SousServiceAvantagesDTO dto);
    void delete(Long id);
}
