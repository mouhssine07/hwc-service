package hwc_backend.repository;

import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Long> {
    List<Diagnostic> findByUserIdOrderByDateDebutDesc(Long userId);

    List<Diagnostic> findByUserIdAndStatutOrderByDateDebutDesc(Long userId, DiagnosticStatut statut);
}
