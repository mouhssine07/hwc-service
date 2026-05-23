package hwc_backend.service.impl;

import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.ReponseDiagnostic;
import hwc_backend.entity.Score;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.QuestionRepository;
import hwc_backend.repository.ReponseDiagnosticRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.service.ScoringService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
    private static final BigDecimal MAX_POINTS_BY_QUESTION = new BigDecimal("5");

    private final DiagnosticRepository diagnosticRepository;
    private final CategorieDiagnosticRepository categorieDiagnosticRepository;
    private final QuestionRepository questionRepository;
    private final ReponseDiagnosticRepository reponseDiagnosticRepository;
    private final ScoreRepository scoreRepository;

    @Override
    @Transactional
    public List<Score> calculerScores(Long diagnosticId) {
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));

        scoreRepository.deleteByDiagnosticId(diagnosticId);

        List<ReponseDiagnostic> reponses = reponseDiagnosticRepository.findByDiagnosticId(diagnosticId);
        List<CategorieDiagnostic> categories = categorieDiagnosticRepository.findAllByOrderByOrdreAsc();

        List<Score> scores = categories.stream()
                .map(categorie -> buildScore(diagnostic, categorie, reponses))
                .toList();

        return scoreRepository.saveAll(scores);
    }

    @Override
    public BigDecimal calculerScoreGlobal(List<Score> scores) {
        BigDecimal totalPoids = scores.stream()
                .map(score -> score.getCategorie().getPoids())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (scores.isEmpty() || totalPoids.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal totalPondere = scores.stream()
                .map(score -> score.getScore().multiply(score.getCategorie().getPoids()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPondere.divide(totalPoids, 2, RoundingMode.HALF_UP);
    }

    @Override
    public String determinerNiveau(BigDecimal scoreGlobal) {
        if (scoreGlobal.compareTo(new BigDecimal("30")) <= 0) {
            return "CRITIQUE";
        }
        if (scoreGlobal.compareTo(new BigDecimal("50")) <= 0) {
            return "FAIBLE";
        }
        if (scoreGlobal.compareTo(new BigDecimal("70")) <= 0) {
            return "MOYEN";
        }
        if (scoreGlobal.compareTo(new BigDecimal("85")) <= 0) {
            return "BON";
        }
        return "EXCELLENT";
    }

    private Score buildScore(Diagnostic diagnostic, CategorieDiagnostic categorie, List<ReponseDiagnostic> reponses) {
        int pointsObtenus = reponses.stream()
                .filter(reponse -> categorie.getId().equals(reponse.getQuestion().getCategorie().getId()))
                .mapToInt(reponse -> reponse.getOptionReponse().getPoids())
                .sum();

        long questionCount = questionRepository.countByCategorieIdAndActifTrue(categorie.getId());
        int pointsMax = Math.toIntExact(questionCount * MAX_POINTS_BY_QUESTION.intValue());

        BigDecimal scoreCategorie = pointsMax == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(pointsObtenus)
                .multiply(ONE_HUNDRED)
                .divide(BigDecimal.valueOf(pointsMax), 2, RoundingMode.HALF_UP);

        Score score = new Score();
        score.setDiagnostic(diagnostic);
        score.setCategorie(categorie);
        score.setScore(scoreCategorie);
        score.setPointsObtenus(pointsObtenus);
        score.setPointsMax(pointsMax);
        return score;
    }
}
