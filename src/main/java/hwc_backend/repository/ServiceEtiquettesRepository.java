package hwc_backend.repository;

import hwc_backend.entity.ServiceEtiquettes;
import hwc_backend.entity.ServiceEtiquettesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mouhssine
 * @version 1.0
 * @date 14/04/2026
 */
@Repository
public interface ServiceEtiquettesRepository extends JpaRepository<ServiceEtiquettes, ServiceEtiquettesId> {}