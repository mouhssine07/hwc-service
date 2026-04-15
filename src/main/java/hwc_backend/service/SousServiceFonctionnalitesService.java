package hwc_backend.service;

import hwc_backend.dto.SousServiceFonctionnalitesDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface SousServiceFonctionnalitesService {

    List<SousServiceFonctionnalitesDTO> getAll();
    List<SousServiceFonctionnalitesDTO> getBySousServiceId(Long sousServiceId);
    SousServiceFonctionnalitesDTO getById(Long id);
    SousServiceFonctionnalitesDTO create(SousServiceFonctionnalitesDTO dto);
    SousServiceFonctionnalitesDTO update(Long id, SousServiceFonctionnalitesDTO dto);
    void delete(Long id);
}
