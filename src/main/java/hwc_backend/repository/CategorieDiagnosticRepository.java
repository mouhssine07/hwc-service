package hwc_backend.repository;

import hwc_backend.entity.CategorieDiagnostic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieDiagnosticRepository extends JpaRepository<CategorieDiagnostic, Long> {
    Optional<CategorieDiagnostic> findByNom(String nom);

    List<CategorieDiagnostic> findAllByOrderByOrdreAsc();
}
