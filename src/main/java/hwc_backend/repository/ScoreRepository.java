package hwc_backend.repository;

import hwc_backend.entity.Score;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByDiagnosticId(Long diagnosticId);
}
