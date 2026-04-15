package hwc_backend.service;

import hwc_backend.dto.ServiceFonctionnalitesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface ServiceFonctionnalitesService {

    List<ServiceFonctionnalitesDTO> getAll();
    List<ServiceFonctionnalitesDTO> getByServiceId(Long serviceId);
    ServiceFonctionnalitesDTO getById(Long id);
    ServiceFonctionnalitesDTO create(ServiceFonctionnalitesDTO dto);
    ServiceFonctionnalitesDTO update(Long id, ServiceFonctionnalitesDTO dto);
    void delete(Long id);
}
