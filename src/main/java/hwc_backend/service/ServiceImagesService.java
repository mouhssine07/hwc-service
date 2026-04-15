package hwc_backend.service;

import hwc_backend.dto.ServiceImagesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface ServiceImagesService {

    List<ServiceImagesDTO> getAll();
    List<ServiceImagesDTO> getByServiceId(Long serviceId);
    ServiceImagesDTO getById(Long id);
    ServiceImagesDTO create(ServiceImagesDTO dto);
    ServiceImagesDTO update(Long id, ServiceImagesDTO dto);
    void delete(Long id);
}
