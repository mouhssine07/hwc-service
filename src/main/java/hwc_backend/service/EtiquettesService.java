package hwc_backend.service;

import hwc_backend.dto.EtiquettesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface EtiquettesService {

    List<EtiquettesDTO> getAll();
    EtiquettesDTO getById(Long id);
    EtiquettesDTO create(EtiquettesDTO dto);
    EtiquettesDTO update(Long id, EtiquettesDTO dto);
    void delete(Long id);
}
