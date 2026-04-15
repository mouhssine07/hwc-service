package hwc_backend.service;

import hwc_backend.dto.TemoignagesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface TemoignagesService {

    List<TemoignagesDTO> getAll();
    TemoignagesDTO getById(Long id);
    TemoignagesDTO create(TemoignagesDTO dto);
    TemoignagesDTO update(Long id, TemoignagesDTO dto);
    void delete(Long id);
}
