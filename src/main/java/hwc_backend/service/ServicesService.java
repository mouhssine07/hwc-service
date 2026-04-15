package hwc_backend.service;

import hwc_backend.dto.ServicesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface ServicesService {

    List<ServicesDTO> getAll();
    ServicesDTO getById(Long id);
    ServicesDTO create(ServicesDTO dto);
    ServicesDTO update(Long id, ServicesDTO dto);
    void delete(Long id);
}
