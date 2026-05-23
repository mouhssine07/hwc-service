package hwc_backend.repository;

import hwc_backend.entity.Recommandation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommandationRepository extends JpaRepository<Recommandation, Long> {

    List<Recommandation> findByDiagnosticIdOrderByPrioriteAsc(Long diagnosticId);

    void deleteByDiagnosticId(Long diagnosticId);
}
