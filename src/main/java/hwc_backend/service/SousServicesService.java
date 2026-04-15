package hwc_backend.service;

import hwc_backend.dto.SousServicesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface SousServicesService {

    List<SousServicesDTO> getAll();
    List<SousServicesDTO> getByServiceId(Long serviceId);
    SousServicesDTO getById(Long id);
    SousServicesDTO create(SousServicesDTO dto);
    SousServicesDTO update(Long id, SousServicesDTO dto);
    void delete(Long id);
}
