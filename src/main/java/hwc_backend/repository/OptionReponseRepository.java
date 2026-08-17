package hwc_backend.repository;

import hwc_backend.entity.OptionReponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionReponseRepository extends JpaRepository<OptionReponse, Long> {
    List<OptionReponse> findByQuestionIdOrderByOrdreAsc(Long questionId);
}
