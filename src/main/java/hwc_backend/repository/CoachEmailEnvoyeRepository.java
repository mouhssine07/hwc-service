package hwc_backend.repository;

import hwc_backend.entity.CoachEmailEnvoye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachEmailEnvoyeRepository extends JpaRepository<CoachEmailEnvoye, Long> {

    void deleteByObjectifsHebdoId(Long objectifsHebdoId);

    boolean existsByObjectifsHebdoIdAndTypeAndStatut(Long objectifsHebdoId, String type, String statut);
}
