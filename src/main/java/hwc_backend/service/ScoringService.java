package hwc_backend.service;

import hwc_backend.entity.Score;
import java.math.BigDecimal;
import java.util.List;

public interface ScoringService {

    List<Score> calculerScores(Long diagnosticId);

    BigDecimal calculerScoreGlobal(List<Score> scores);

    String determinerNiveau(BigDecimal scoreGlobal);
}
