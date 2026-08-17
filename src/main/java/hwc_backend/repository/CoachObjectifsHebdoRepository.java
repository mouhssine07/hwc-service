package hwc_backend.repository;

import hwc_backend.entity.CoachObjectifsHebdo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachObjectifsHebdoRepository extends JpaRepository<CoachObjectifsHebdo, Long> {

    Optional<CoachObjectifsHebdo> findFirstByUserIdAndDiagnosticIdAndSemaineDebutOrderByIdAsc(
            Long userId,
            Long diagnosticId,
            LocalDate semaineDebut
    );

    List<CoachObjectifsHebdo> findByUserIdOrderBySemaineDebutDesc(Long userId);

    List<CoachObjectifsHebdo> findByUserIdAndDiagnosticIdOrderBySemaineDebutDesc(Long userId, Long diagnosticId);

    List<CoachObjectifsHebdo> findByDiagnosticId(Long diagnosticId);
}
