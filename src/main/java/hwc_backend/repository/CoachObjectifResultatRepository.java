package hwc_backend.repository;

import hwc_backend.entity.CoachObjectifResultat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachObjectifResultatRepository extends JpaRepository<CoachObjectifResultat, Long> {

    List<CoachObjectifResultat> findByObjectifsHebdoIdOrderByOrdreAsc(Long objectifsHebdoId);

    void deleteByObjectifsHebdoId(Long objectifsHebdoId);
}
