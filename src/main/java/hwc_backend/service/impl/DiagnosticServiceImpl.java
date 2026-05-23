package hwc_backend.service.impl;

import hwc_backend.dto.diagnostic.CategorieDiagnosticDTO;
import hwc_backend.dto.diagnostic.DiagnosticResultatDTO;
import hwc_backend.dto.diagnostic.DiagnosticStartDTO;
import hwc_backend.dto.diagnostic.OptionReponseDTO;
import hwc_backend.dto.diagnostic.QuestionDTO;
import hwc_backend.dto.diagnostic.RepondreQuestionDTO;
import hwc_backend.dto.diagnostic.ScoreDTO;
import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import hwc_backend.entity.OptionReponse;
import hwc_backend.entity.Question;
import hwc_backend.entity.ReponseDiagnostic;
import hwc_backend.entity.Score;
import hwc_backend.entity.User;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.OptionReponseRepository;
import hwc_backend.repository.QuestionRepository;
import hwc_backend.repository.ReponseDiagnosticRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.DiagnosticService;
import hwc_backend.service.RecommandationService;
import hwc_backend.service.ScoringService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiagnosticServiceImpl implements DiagnosticService {

    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final CategorieDiagnosticRepository categorieDiagnosticRepository;
    private final QuestionRepository questionRepository;
    private final OptionReponseRepository optionReponseRepository;
    private final ReponseDiagnosticRepository reponseDiagnosticRepository;
    private final ScoreRepository scoreRepository;
    private final ScoringService scoringService;
    private final RecommandationService recommandationService;

    @Override
    @Transactional
    public DiagnosticStartDTO start(String email) {
        User user = findUser(email);

        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setStatut(DiagnosticStatut.EN_COURS);

        Diagnostic saved = diagnosticRepository.save(diagnostic);
        return new DiagnosticStartDTO(saved.getId(), saved.getStatut(), saved.getDateDebut());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorieDiagnosticDTO> getQuestions() {
        return categorieDiagnosticRepository.findAllByOrderByOrdreAsc().stream()
                .map(this::toCategorieDTO)
                .toList();
    }

    @Override
    @Transactional
    public DiagnosticResultatDTO repondre(Long diagnosticId, RepondreQuestionDTO request, String email) {
        Diagnostic diagnostic = findOwnedDiagnostic(diagnosticId, email);
        if (diagnostic.getStatut() != DiagnosticStatut.EN_COURS) {
            throw new IllegalStateException("Diagnostic is not in progress");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        OptionReponse option = optionReponseRepository.findById(request.getOptionReponseId())
                .orElseThrow(() -> new EntityNotFoundException("Option not found"));

        if (!option.getQuestion().getId().equals(question.getId())) {
            throw new IllegalArgumentException("Option does not belong to the selected question");
        }

        ReponseDiagnostic reponse = reponseDiagnosticRepository
                .findByDiagnosticIdAndQuestionId(diagnosticId, question.getId())
                .orElseGet(ReponseDiagnostic::new);
        reponse.setDiagnostic(diagnostic);
        reponse.setQuestion(question);
        reponse.setOptionReponse(option);
        reponseDiagnosticRepository.save(reponse);

        return toDiagnosticDTO(diagnostic, List.of());
    }

    @Override
    @Transactional
    public DiagnosticResultatDTO finalizeDiagnostic(Long diagnosticId, String email) {
        Diagnostic diagnostic = findOwnedDiagnostic(diagnosticId, email);
        if (diagnostic.getStatut() != DiagnosticStatut.EN_COURS) {
            return toDiagnosticDTO(diagnostic, scoreRepository.findByDiagnosticId(diagnosticId));
        }

        long totalQuestions = questionRepository.countByActifTrue();
        long progression = reponseDiagnosticRepository.countByDiagnosticId(diagnosticId);
        if (totalQuestions > 0 && progression < totalQuestions) {
            throw new IllegalStateException("Diagnostic is incomplete");
        }

        List<Score> scores = scoringService.calculerScores(diagnosticId);
        BigDecimal scoreGlobal = scoringService.calculerScoreGlobal(scores);

        diagnostic.setScoreGlobal(scoreGlobal);
        diagnostic.setNiveauMaturite(scoringService.determinerNiveau(scoreGlobal));
        diagnostic.setStatut(DiagnosticStatut.TERMINE);
        diagnostic.setDateFin(LocalDateTime.now());

        Diagnostic saved = diagnosticRepository.save(diagnostic);
        recommandationService.genererRecommandations(saved.getId());

        return toDiagnosticDTO(saved, scores);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticResultatDTO> history(String email) {
        User user = findUser(email);
        return diagnosticRepository.findByUserIdOrderByDateDebutDesc(user.getId()).stream()
                .map(diagnostic -> toDiagnosticDTO(diagnostic, scoreRepository.findByDiagnosticId(diagnostic.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticResultatDTO getById(Long diagnosticId, String email) {
        Diagnostic diagnostic = findOwnedDiagnostic(diagnosticId, email);
        return toDiagnosticDTO(diagnostic, scoreRepository.findByDiagnosticId(diagnosticId));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Diagnostic findOwnedDiagnostic(Long diagnosticId, String email) {
        User user = findUser(email);
        Diagnostic diagnostic = diagnosticRepository.findById(diagnosticId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostic not found"));

        if (!diagnostic.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Diagnostic does not belong to authenticated user");
        }
        return diagnostic;
    }

    private CategorieDiagnosticDTO toCategorieDTO(CategorieDiagnostic categorie) {
        List<QuestionDTO> questions = questionRepository
                .findByCategorieIdAndActifTrueOrderByOrdreAsc(categorie.getId()).stream()
                .map(this::toQuestionDTO)
                .toList();

        return new CategorieDiagnosticDTO(
                categorie.getId(),
                categorie.getNom(),
                categorie.getDescription(),
                categorie.getIcone(),
                categorie.getPoids(),
                categorie.getOrdre(),
                questions
        );
    }

    private QuestionDTO toQuestionDTO(Question question) {
        List<OptionReponseDTO> options = optionReponseRepository.findByQuestionIdOrderByOrdreAsc(question.getId()).stream()
                .map(option -> new OptionReponseDTO(
                        option.getId(),
                        option.getTexte(),
                        option.getPoids(),
                        option.getOrdre()
                ))
                .toList();

        return new QuestionDTO(question.getId(), question.getTexte(), question.getOrdre(), options);
    }

    private DiagnosticResultatDTO toDiagnosticDTO(Diagnostic diagnostic, List<Score> scores) {
        int totalQuestions = Math.toIntExact(questionRepository.countByActifTrue());
        int progression = Math.toIntExact(reponseDiagnosticRepository.countByDiagnosticId(diagnostic.getId()));

        return new DiagnosticResultatDTO(
                diagnostic.getId(),
                diagnostic.getScoreGlobal(),
                diagnostic.getNiveauMaturite(),
                diagnostic.getStatut(),
                diagnostic.getDateDebut(),
                diagnostic.getDateFin(),
                progression,
                totalQuestions,
                scores.stream().map(this::toScoreDTO).toList()
        );
    }

    private ScoreDTO toScoreDTO(Score score) {
        return new ScoreDTO(
                score.getCategorie().getId(),
                score.getCategorie().getNom(),
                score.getScore(),
                scoringService.determinerNiveau(score.getScore()),
                score.getPointsObtenus(),
                score.getPointsMax()
        );
    }
}
