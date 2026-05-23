package hwc_backend.repository;

import hwc_backend.entity.RegleRecommandation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleRecommandationRepository extends JpaRepository<RegleRecommandation, Long> {

    List<RegleRecommandation> findByActifTrueOrderByPrioriteAsc();

    boolean existsByCategorieIdAndTitreRecommandation(Long categorieId, String titreRecommandation);
}
