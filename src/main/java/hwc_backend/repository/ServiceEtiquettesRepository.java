package hwc_backend.repository;

import hwc_backend.entity.ServiceEtiquettes;
import hwc_backend.entity.ServiceEtiquettesId;
import hwc_backend.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Mouhssine
 * @version 1.0
 * @date 14/04/2026
 */
@Repository
public interface ServiceEtiquettesRepository extends JpaRepository<ServiceEtiquettes, ServiceEtiquettesId> {

    List<ServiceEtiquettes> findByService(Services service);
}