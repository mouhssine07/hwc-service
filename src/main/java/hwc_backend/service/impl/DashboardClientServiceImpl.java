package hwc_backend.service.impl;

import hwc_backend.dto.dashboard.AlerteCritiqueDTO;
import hwc_backend.dto.dashboard.DashboardClientDTO;
import hwc_backend.dto.dashboard.HistoriqueDiagnosticDTO;
import hwc_backend.dto.dashboard.RecommandationResumeDTO;
import hwc_backend.dto.dashboard.ScoreCategorieDTO;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import hwc_backend.entity.Recommandation;
import hwc_backend.entity.Score;
import hwc_backend.entity.User;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.RecommandationRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.DashboardClientService;
import hwc_backend.service.ScoringService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardClientServiceImpl implements DashboardClientService {

    private static final BigDecimal SCORE_CRITIQUE = new BigDecimal("40.00");

    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final ScoreRepository scoreRepository;
    private final RecommandationRepository recommandationRepository;
    private final ScoringService scoringService;

    @Override
    @Transactional(readOnly = true)
    public DashboardClientDTO getDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Diagnostic> diagnostics = diagnosticRepository
                .findByUserIdAndStatutOrderByDateDebutDesc(user.getId(), DiagnosticStatut.TERMINE).stream()
                .sorted(Comparator.comparing(this::diagnosticSortDate).reversed())
                .toList();

        if (diagnostics.isEmpty()) {
            return new DashboardClientDTO(
                    false,
                    "Aucun diagnostic finalise. Lancez un diagnostic pour afficher votre dashboard.",
                    null,
                    null,
                    null,
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    0,
                    null,
                    null
            );
        }

        Diagnostic dernierDiagnostic = diagnostics.get(0);
        List<ScoreCategorieDTO> scores = scoreRepository.findByDiagnosticId(dernierDiagnostic.getId()).stream()
                .sorted(Comparator.comparing(score -> score.getCategorie().getOrdre(), Comparator.nullsLast(Integer::compareTo)))
                .map(this::toScoreCategorieDTO)
                .toList();
        List<ScoreCategorieDTO> categoriesFaibles = scores.stream()
                .filter(score -> score.getScore() != null && score.getScore().compareTo(SCORE_CRITIQUE) <= 0)
                .toList();
        List<RecommandationResumeDTO> recommandations = recommandationRepository
                .findByDiagnosticIdOrderByPrioriteAsc(dernierDiagnostic.getId()).stream()
                .map(this::toRecommandationResumeDTO)
                .toList();

        return new DashboardClientDTO(
                true,
                "Dashboard client disponible.",
                dernierDiagnostic.getId(),
                dernierDiagnostic.getScoreGlobal(),
                dernierDiagnostic.getNiveauMaturite(),
                scores,
                diagnostics.stream().map(this::toHistoriqueDiagnosticDTO).toList(),
                categoriesFaibles,
                categoriesFaibles.stream().map(this::toAlerteCritiqueDTO).toList(),
                recommandations.stream().limit(5).toList(),
                recommandations,
                recommandations.stream()
                        .filter(recommandation -> recommandation.getServiceHwcNom() != null)
                        .filter(distinctByService())
                        .toList(),
                diagnostics.size(),
                dernierDiagnostic.getDateFin(),
                calculerProgression(diagnostics)
        );
    }

    private java.util.function.Predicate<RecommandationResumeDTO> distinctByService() {
        java.util.Set<String> seen = new java.util.HashSet<>();
        return recommandation -> seen.add(recommandation.getServiceHwcNom());
    }

    private java.time.LocalDateTime diagnosticSortDate(Diagnostic diagnostic) {
        return diagnostic.getDateFin() != null ? diagnostic.getDateFin() : diagnostic.getDateDebut();
    }

    private BigDecimal calculerProgression(List<Diagnostic> diagnostics) {
        if (diagnostics.size() < 2
                || diagnostics.get(0).getScoreGlobal() == null
                || diagnostics.get(1).getScoreGlobal() == null) {
            return null;
        }
        return diagnostics.get(0).getScoreGlobal().subtract(diagnostics.get(1).getScoreGlobal());
    }

    private HistoriqueDiagnosticDTO toHistoriqueDiagnosticDTO(Diagnostic diagnostic) {
        return new HistoriqueDiagnosticDTO(
                diagnostic.getId(),
                diagnostic.getScoreGlobal(),
                diagnostic.getNiveauMaturite(),
                diagnostic.getDateFin()
        );
    }

    private ScoreCategorieDTO toScoreCategorieDTO(Score score) {
        return new ScoreCategorieDTO(
                score.getCategorie().getId(),
                score.getCategorie().getNom(),
                scoringService.determinerNiveau(score.getScore()),
                score.getScore(),
                score.getPointsObtenus(),
                score.getPointsMax(),
                score.getCategorie().getOrdre()
        );
    }

    private AlerteCritiqueDTO toAlerteCritiqueDTO(ScoreCategorieDTO score) {
        return new AlerteCritiqueDTO(
                score.getCategorieId(),
                score.getCategorieNom(),
                score.getScore(),
                "Score critique sur " + score.getCategorieNom() + ". Action prioritaire recommandee.",
                "HAUTE"
        );
    }

    private RecommandationResumeDTO toRecommandationResumeDTO(Recommandation recommandation) {
        return new RecommandationResumeDTO(
                recommandation.getId(),
                recommandation.getTitre(),
                recommandation.getDescription(),
                recommandation.getHorizon(),
                recommandation.getServiceHwc() != null ? recommandation.getServiceHwc().getTitre() : null,
                recommandation.getSousServiceHwc() != null ? recommandation.getSousServiceHwc().getTitre() : null,
                recommandation.getImpactEstime(),
                recommandation.getPriorite()
        );
    }
}
