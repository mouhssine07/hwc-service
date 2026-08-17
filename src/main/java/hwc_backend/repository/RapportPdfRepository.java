package hwc_backend.repository;

import hwc_backend.entity.RapportPdf;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RapportPdfRepository extends JpaRepository<RapportPdf, Long> {

    List<RapportPdf> findByUserIdOrderByDateGenerationDesc(Long userId);

    void deleteByDiagnosticId(Long diagnosticId);
}
