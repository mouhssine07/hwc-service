package hwc_backend.service;

import hwc_backend.dto.SousServiceFaqsDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface SousServiceFaqsService {

    List<SousServiceFaqsDTO> getAll();
    List<SousServiceFaqsDTO> getBySousServiceId(Long sousServiceId);
    SousServiceFaqsDTO getById(Long id);
    SousServiceFaqsDTO create(SousServiceFaqsDTO dto);
    SousServiceFaqsDTO update(Long id, SousServiceFaqsDTO dto);
    void delete(Long id);
}
