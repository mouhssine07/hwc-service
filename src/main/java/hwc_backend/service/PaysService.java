package hwc_backend.service;

import hwc_backend.dto.PaysDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface PaysService {

    List<PaysDTO> getAll();
    PaysDTO getById(Long id);
    PaysDTO create(PaysDTO dto);
    PaysDTO update(Long id, PaysDTO dto);
    void delete(Long id);
}
