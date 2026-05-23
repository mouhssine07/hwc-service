package hwc_backend.repository;

import hwc_backend.entity.ReponseDiagnostic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseDiagnosticRepository extends JpaRepository<ReponseDiagnostic, Long> {
    List<ReponseDiagnostic> findByDiagnosticId(Long diagnosticId);

    Optional<ReponseDiagnostic> findByDiagnosticIdAndQuestionId(Long diagnosticId, Long questionId);

    long countByDiagnosticId(Long diagnosticId);
}
