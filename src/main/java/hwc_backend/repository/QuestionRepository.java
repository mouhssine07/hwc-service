package hwc_backend.repository;

import hwc_backend.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategorieIdAndActifTrueOrderByOrdreAsc(Long categorieId);

    long countByCategorieIdAndActifTrue(Long categorieId);

    long countByActifTrue();
}
