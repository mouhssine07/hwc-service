package hwc_backend.service.impl;

import hwc_backend.dto.recommandation.PlanActionDTO;
import hwc_backend.dto.recommandation.RecommandationDTO;
import hwc_backend.dto.recommandation.RegleRecommandationDTO;
import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.RegleRecommandation;
import hwc_backend.entity.Recommandation;
import hwc_backend.entity.Score;
import hwc_backend.entity.Services;
import hwc_backend.entity.SousServices;
import hwc_backend.entity.User;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.RegleRecommandationRepository;
import hwc_backend.repository.RecommandationRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.RecommandationService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommandationServiceImpl implements RecommandationService {

    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final ScoreRepository scoreRepository;
    private final RegleRecommandationRepository regleRepository;
    private final RecommandationRepository recommandationRepository;
    private final CategorieDiagnosticRepository categorieRepository;
    private final ServicesRepository servicesRepository;
    private final SousServicesRepository sousServicesRepository;

    @Override
    @Transactional
    public List<Recommandation> genererRecommandations(Long diagnosticId) {
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));
        List<Score> scores = scoreRepository.findByDiagnosticId(diagnosticId);
        List<RegleRecommandation> regles = regleRepository.findByActifTrueOrderByPrioriteAsc();

        recommandationRepository.deleteByDiagnosticId(diagnosticId);

        List<Recommandation> recommandations = new ArrayList<>();
        for (RegleRecommandation regle : regles) {
            Score score = scores.stream()
                    .filter(item -> Objects.equals(item.getCategorie().getId(), regle.getCategorie().getId()))
                    .findFirst()
                    .orElse(null);

            if (score != null && respecteCondition(score.getScore(), regle.getSeuilScore(), regle.getOperateur())) {
                recommandations.add(creerRecommandation(diagnostic, regle));
            }
        }

        recommandations.sort(Comparator.comparing(Recommandation::getPriorite));
        return recommandationRepository.saveAll(recommandations);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommandationDTO> getRecommandations(Long diagnosticId, String email) {
        findOwnedDiagnostic(diagnosticId, email);
        return recommandationRepository.findByDiagnosticIdOrderByPrioriteAsc(diagnosticId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanActionDTO getPlanAction(Long diagnosticId, String email) {
        List<RecommandationDTO> recommandations = getRecommandations(diagnosticId, email);
        return new PlanActionDTO(
                filterByHorizon(recommandations, "COURT_TERME"),
                filterByHorizon(recommandations, "MOYEN_TERME"),
                filterByHorizon(recommandations, "LONG_TERME"),
                recommandations.isEmpty() ? "Aucune action prioritaire generee" : "Plan priorise sur 1 a 6 mois"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegleRecommandationDTO> getRegles() {
        return regleRepository.findAll().stream()
                .sorted(Comparator.comparing(RegleRecommandation::getPriorite))
                .map(this::toRegleDTO)
                .toList();
    }

    @Override
    @Transactional
    public RegleRecommandationDTO createRegle(RegleRecommandationDTO dto) {
        RegleRecommandation regle = new RegleRecommandation();
        applyRegleDTO(regle, dto);
        return toRegleDTO(regleRepository.save(regle));
    }

    @Override
    @Transactional
    public RegleRecommandationDTO updateRegle(Long id, RegleRecommandationDTO dto) {
        RegleRecommandation regle = regleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation rule not found"));
        applyRegleDTO(regle, dto);
        return toRegleDTO(regleRepository.save(regle));
    }

    @Override
    @Transactional
    public void deleteRegle(Long id) {
        regleRepository.deleteById(id);
    }

    private Diagnostic findOwnedDiagnostic(Long diagnosticId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));

        if (!diagnostic.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Diagnostic does not belong to authenticated user");
        }
        return diagnostic;
    }

    private boolean respecteCondition(BigDecimal score, BigDecimal seuil, String operateur) {
        int comparison = score.compareTo(seuil);
        return switch (operateur) {
            case "<" -> comparison < 0;
            case "<=" -> comparison <= 0;
            case ">" -> comparison > 0;
            case ">=" -> comparison >= 0;
            case "=" -> comparison == 0;
            default -> false;
        };
    }

    private Recommandation creerRecommandation(Diagnostic diagnostic, RegleRecommandation regle) {
        Recommandation recommandation = new Recommandation();
        recommandation.setDiagnostic(diagnostic);
        recommandation.setRegle(regle);
        recommandation.setTitre(regle.getTitreRecommandation());
        recommandation.setDescription(regle.getDescriptionRecommandation());
        recommandation.setHorizon(regle.getHorizon());
        recommandation.setServiceHwc(regle.getServiceHwc());
        recommandation.setSousServiceHwc(regle.getSousServiceHwc());
        recommandation.setImpactEstime(regle.getImpactEstime());
        recommandation.setKpis(regle.getKpisSuggeres());
        recommandation.setPriorite(regle.getPriorite());
        return recommandation;
    }

    private List<RecommandationDTO> filterByHorizon(List<RecommandationDTO> recommandations, String horizon) {
        return recommandations.stream()
                .filter(recommandation -> horizon.equals(recommandation.getHorizon()))
                .toList();
    }

    private RecommandationDTO toDTO(Recommandation recommandation) {
        Services service = recommandation.getServiceHwc();
        SousServices sousService = recommandation.getSousServiceHwc();
        return new RecommandationDTO(
                recommandation.getId(),
                recommandation.getTitre(),
                recommandation.getDescription(),
                recommandation.getHorizon(),
                service != null ? service.getId() : null,
                service != null ? service.getTitre() : null,
                sousService != null ? sousService.getId() : null,
                sousService != null ? sousService.getTitre() : null,
                recommandation.getImpactEstime(),
                splitKpis(recommandation.getKpis()),
                recommandation.getPriorite()
        );
    }

    private RegleRecommandationDTO toRegleDTO(RegleRecommandation regle) {
        Services service = regle.getServiceHwc();
        SousServices sousService = regle.getSousServiceHwc();
        return new RegleRecommandationDTO(
                regle.getId(),
                regle.getCategorie().getId(),
                regle.getCategorie().getNom(),
                regle.getSeuilScore(),
                regle.getOperateur(),
                service != null ? service.getId() : null,
                service != null ? service.getTitre() : null,
                sousService != null ? sousService.getId() : null,
                sousService != null ? sousService.getTitre() : null,
                regle.getTitreRecommandation(),
                regle.getDescriptionRecommandation(),
                regle.getHorizon(),
                regle.getImpactEstime(),
                regle.getKpisSuggeres(),
                regle.getPriorite(),
                regle.isActif()
        );
    }

    private void applyRegleDTO(RegleRecommandation regle, RegleRecommandationDTO dto) {
        CategorieDiagnostic categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic category not found"));

        regle.setCategorie(categorie);
        regle.setSeuilScore(dto.getSeuilScore());
        regle.setOperateur(dto.getOperateur());
        regle.setServiceHwc(dto.getServiceHwcId() == null ? null : servicesRepository.findById(dto.getServiceHwcId())
                .orElseThrow(() -> new EntityNotFoundException("HWC service not found")));
        regle.setSousServiceHwc(dto.getSousServiceHwcId() == null ? null : sousServicesRepository.findById(dto.getSousServiceHwcId())
                .orElseThrow(() -> new EntityNotFoundException("HWC sub-service not found")));
        regle.setTitreRecommandation(dto.getTitreRecommandation());
        regle.setDescriptionRecommandation(dto.getDescriptionRecommandation());
        regle.setHorizon(dto.getHorizon());
        regle.setImpactEstime(dto.getImpactEstime());
        regle.setKpisSuggeres(dto.getKpisSuggeres());
        regle.setPriorite(dto.getPriorite());
        regle.setActif(dto.isActif());
    }

    private List<String> splitKpis(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\\R|;"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }
}
