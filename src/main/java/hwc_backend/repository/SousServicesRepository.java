package hwc_backend.repository;

import hwc_backend.entity.SousServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mouhssine
 * @version 1.0
 * @date 14/04/2026
 */
@Repository
public interface SousServicesRepository extends JpaRepository<SousServices, Long> {}