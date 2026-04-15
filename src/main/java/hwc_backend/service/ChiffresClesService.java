package hwc_backend.service;

import hwc_backend.dto.ChiffresClesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface ChiffresClesService {

    List<ChiffresClesDTO> getAll();
    ChiffresClesDTO getById(Long id);
    ChiffresClesDTO create(ChiffresClesDTO dto);
    ChiffresClesDTO update(Long id, ChiffresClesDTO dto);
    void delete(Long id);
}
