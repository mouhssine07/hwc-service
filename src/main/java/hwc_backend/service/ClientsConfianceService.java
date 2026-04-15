package hwc_backend.service;

import hwc_backend.dto.ClientsConfianceDTO;

import java.util.List;

/**
 * @author SETUP GAME
 **/
public interface ClientsConfianceService {

    List<ClientsConfianceDTO> getAll();
    ClientsConfianceDTO getById(Long id);
    ClientsConfianceDTO create(ClientsConfianceDTO dto);
    ClientsConfianceDTO update(Long id, ClientsConfianceDTO dto);
    void delete(Long id);
}
