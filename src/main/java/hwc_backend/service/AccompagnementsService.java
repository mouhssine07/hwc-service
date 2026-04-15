package hwc_backend.service;

import hwc_backend.dto.AccompagnementsDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface AccompagnementsService {

    List<AccompagnementsDTO> getAll();
    List<AccompagnementsDTO> getBySousServiceId(Long sousServiceId);
    AccompagnementsDTO getById(Long id);
    AccompagnementsDTO create(AccompagnementsDTO dto);
    AccompagnementsDTO update(Long id, AccompagnementsDTO dto);
    void delete(Long id);
}
