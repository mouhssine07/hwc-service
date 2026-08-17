package hwc_backend.service.impl;

import hwc_backend.dto.rapport.RapportGenerationResult;
import hwc_backend.dto.rapport.RapportPdfDTO;
import hwc_backend.entity.Diagnostic;
import hwc_backend.entity.DiagnosticStatut;
import hwc_backend.entity.RapportPdf;
import hwc_backend.entity.Recommandation;
import hwc_backend.entity.Score;
import hwc_backend.entity.User;
import hwc_backend.repository.DiagnosticRepository;
import hwc_backend.repository.RapportPdfRepository;
import hwc_backend.repository.RecommandationRepository;
import hwc_backend.repository.ScoreRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.service.OpenAIService;
import hwc_backend.service.RapportService;
import jakarta.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RapportServiceImpl implements RapportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final UserRepository userRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final ScoreRepository scoreRepository;
    private final RecommandationRepository recommandationRepository;
    private final RapportPdfRepository rapportPdfRepository;
    private final OpenAIService openAIService;

    @Override
    @Transactional
    public RapportPdf genererRapportDiagnostic(Long diagnosticId, String email) {
        Diagnostic diagnostic = findOwnedDiagnostic(diagnosticId, email);
        if (diagnostic.getStatut() != DiagnosticStatut.TERMINE) {
            throw new IllegalStateException("Le diagnostic doit etre finalise avant de generer un rapport PDF.");
        }

        User user = diagnostic.getUser();
        List<Score> scores = scoreRepository.findByDiagnosticId(diagnosticId);
        List<Recommandation> recommandations = recommandationRepository.findByDiagnosticIdOrderByPrioriteAsc(diagnosticId);
        RapportGenerationResult contenu = openAIService.genererRapportDiagnostic(buildContext(user, diagnostic, scores, recommandations));
        byte[] pdf = buildPdf(user, diagnostic, scores, recommandations, contenu);

        RapportPdf rapport = new RapportPdf();
        rapport.setDiagnostic(diagnostic);
        rapport.setUser(user);
        rapport.setFileName("rapport-hwc-diagnostic-" + diagnostic.getId() + ".pdf");
        rapport.setIntroduction(contenu.getIntroduction());
        rapport.setAnalyseForts(contenu.getAnalyseForts());
        rapport.setAnalyseFaibles(contenu.getAnalyseFaibles());
        rapport.setPlanAction(contenu.getPlanAction());
        rapport.setConclusion(contenu.getConclusion());
        rapport.setTokensUsed(contenu.getTokensUsed());
        rapport.setPdfContent(pdf);
        return rapportPdfRepository.save(rapport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RapportPdfDTO> getHistorique(String email) {
        User user = findUser(email);
        return rapportPdfRepository.findByUserIdOrderByDateGenerationDesc(user.getId()).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RapportPdf getRapport(Long rapportId, String email) {
        User user = findUser(email);
        RapportPdf rapport = rapportPdfRepository.findById(rapportId)
                .orElseThrow(() -> new EntityNotFoundException("Rapport PDF not found"));
        if (!rapport.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Rapport PDF does not belong to authenticated user");
        }
        return rapport;
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

    private RapportPdfDTO toDTO(RapportPdf rapport) {
        return new RapportPdfDTO(
                rapport.getId(),
                rapport.getDiagnostic().getId(),
                rapport.getFileName(),
                rapport.getDateGeneration()
        );
    }

    private String buildContext(User user, Diagnostic diagnostic, List<Score> scores, List<Recommandation> recommandations) {
        StringBuilder builder = new StringBuilder();
        builder.append("Client: ")
                .append(user.getPrenom()).append(" ").append(user.getNom())
                .append(" | Entreprise: ").append(valueOrDash(user.getEntreprise()))
                .append(" | Secteur: ").append(valueOrDash(user.getSecteur()))
                .append("\nDiagnostic: #").append(diagnostic.getId())
                .append(" | Date: ").append(diagnostic.getDateFin() == null ? "-" : diagnostic.getDateFin().format(DATE_FORMATTER))
                .append(" | Score global: ").append(diagnostic.getScoreGlobal()).append("/100")
                .append(" | Niveau: ").append(diagnostic.getNiveauMaturite())
                .append("\nScores par categorie:\n");

        for (Score score : scores) {
            builder.append("- ")
                    .append(score.getCategorie().getNom())
                    .append(": ").append(score.getScore()).append("/100")
                    .append(" (").append(score.getPointsObtenus()).append("/")
                    .append(score.getPointsMax()).append(" points)\n");
        }

        builder.append("Recommandations:\n");
        for (Recommandation recommandation : recommandations) {
            builder.append("- P").append(recommandation.getPriorite())
                    .append(" ").append(recommandation.getTitre())
                    .append(" | Horizon: ").append(recommandation.getHorizon())
                    .append(" | Impact: ").append(valueOrDash(recommandation.getImpactEstime()))
                    .append("\n");
        }
        return builder.toString();
    }

    private byte[] buildPdf(
            User user,
            Diagnostic diagnostic,
            List<Score> scores,
            List<Recommandation> recommandations,
            RapportGenerationResult contenu
    ) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(document);
            writer.addCover(user, diagnostic);
            writer.addExecutiveSummary(user, diagnostic, contenu);
            writer.addScoreTable(scores);
            writer.addAnalysis(contenu);
            writer.addRecommendations(recommandations, contenu);
            writer.close();
            document.save(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de generer le fichier PDF.", exception);
        }
    }

    private String scoresText(List<Score> scores) {
        StringBuilder builder = new StringBuilder();
        for (Score score : scores) {
            builder.append(score.getCategorie().getNom())
                    .append(": ").append(score.getScore()).append("/100 - ")
                    .append(score.getPointsObtenus()).append("/")
                    .append(score.getPointsMax()).append(" points\n");
        }
        return builder.toString();
    }

    private String recommandationsText(List<Recommandation> recommandations) {
        if (recommandations.isEmpty()) {
            return "Aucune recommandation prioritaire generee.";
        }
        StringBuilder builder = new StringBuilder();
        for (Recommandation recommandation : recommandations) {
            builder.append("P").append(recommandation.getPriorite())
                    .append(" - ").append(recommandation.getTitre())
                    .append(" (").append(recommandation.getHorizon()).append(")\n")
                    .append(valueOrDash(recommandation.getDescription()))
                    .append("\n\n");
        }
        return builder.toString();
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private static class PdfWriter {
        private static final float MARGIN = 48;
        private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
        private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
        private static final float CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2);
        private static final int PRIMARY_R = 46;
        private static final int PRIMARY_G = 125;
        private static final int PRIMARY_B = 107;
        private static final int DARK_R = 24;
        private static final int DARK_G = 35;
        private static final int DARK_B = 48;
        private static final int MUTED_R = 100;
        private static final int MUTED_G = 116;
        private static final int MUTED_B = 139;

        private final PDDocument document;
        private PDPage page;
        private PDPageContentStream stream;
        private float y;
        private int pageNumber = 0;

        PdfWriter(PDDocument document) throws IOException {
            this.document = document;
        }

        void addCover(User user, Diagnostic diagnostic) throws IOException {
            newPage(false);

            stream.setNonStrokingColor(PRIMARY_R, PRIMARY_G, PRIMARY_B);
            stream.addRect(0, PAGE_HEIGHT - 190, PAGE_WIDTH, 190);
            stream.fill();

            writeAt("HARMONY WORKS CONSULTING", MARGIN, PAGE_HEIGHT - 70, PDType1Font.HELVETICA_BOLD, 12, 255, 255, 255);
            writeAt("Rapport Diagnostic HWC 360", MARGIN, PAGE_HEIGHT - 118, PDType1Font.HELVETICA_BOLD, 28, 255, 255, 255);
            writeAt("Synthese decisionnelle et plan d'action priorise", MARGIN, PAGE_HEIGHT - 145, PDType1Font.HELVETICA, 13, 235, 255, 250);

            y = PAGE_HEIGHT - 245;
            card("Client", user.getPrenom() + " " + user.getNom(), valueOrDash(user.getEntreprise()) + " | " + valueOrDash(user.getSecteur()));
            y -= 18;
            card("Diagnostic", "Diagnostic #" + diagnostic.getId(), "Genere le " + formatDate(diagnostic));
            y -= 22;

            drawScoreHero(diagnostic);
        }

        void addExecutiveSummary(User user, Diagnostic diagnostic, RapportGenerationResult contenu) throws IOException {
            newPage(true);
            sectionTitle("Resume executif");
            paragraph(contenu.getIntroduction(), 10.5f, 15);
            y -= 12;
            drawKpiRow(diagnostic);
            y -= 24;
            sectionTitle("Lecture rapide");
            paragraph("Ce rapport transforme les reponses du diagnostic HWC 360 en une vue claire des priorites. Il combine score global, scores par categorie, alertes et recommandations pour soutenir la prise de decision.", 10.5f, 15);
            y -= 8;
            paragraph("Client analyse: " + user.getPrenom() + " " + user.getNom() + " - " + valueOrDash(user.getEntreprise()) + ".", 10.5f, 15);
        }

        void addScoreTable(List<Score> scores) throws IOException {
            ensureSpace(190);
            sectionTitle("Scores par categorie");
            tableHeader();
            for (Score score : scores) {
                tableRow(
                        score.getCategorie().getNom(),
                        String.valueOf(score.getScore()) + "/100",
                        score.getPointsObtenus() + "/" + score.getPointsMax(),
                        niveau(score)
                );
            }
        }

        void addAnalysis(RapportGenerationResult contenu) throws IOException {
            newPage(true);
            sectionTitle("Analyse consultant");
            subsection("Points forts", contenu.getAnalyseForts());
            subsection("Axes faibles", contenu.getAnalyseFaibles());
            subsection("Conclusion", contenu.getConclusion());
        }

        void addRecommendations(List<Recommandation> recommandations, RapportGenerationResult contenu) throws IOException {
            newPage(true);
            sectionTitle("Plan d'action priorise");
            paragraph(contenu.getPlanAction(), 10.5f, 15);
            y -= 12;

            if (recommandations.isEmpty()) {
                recommendationCard("Aucune recommandation prioritaire", "Aucune recommandation prioritaire generee pour ce diagnostic.", "-", null, null);
                return;
            }

            for (Recommandation recommandation : recommandations) {
                recommendationCard(
                        "P" + recommandation.getPriorite() + " - " + recommandation.getTitre(),
                        valueOrDash(recommandation.getDescription()),
                        recommandation.getHorizon(),
                        recommandation.getServiceHwc() == null ? null : recommandation.getServiceHwc().getTitre(),
                        recommandation.getImpactEstime()
                );
            }
        }

        void close() throws IOException {
            if (stream != null) {
                footer();
                stream.close();
            }
        }

        private void newPage(boolean withHeader) throws IOException {
            if (stream != null) {
                footer();
                stream.close();
            }
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            stream = new PDPageContentStream(document, page);
            pageNumber++;
            y = PAGE_HEIGHT - 58;
            if (withHeader) {
                header();
                y = PAGE_HEIGHT - 95;
            }
        }

        private void header() throws IOException {
            stream.setNonStrokingColor(PRIMARY_R, PRIMARY_G, PRIMARY_B);
            stream.addRect(0, PAGE_HEIGHT - 42, PAGE_WIDTH, 42);
            stream.fill();
            writeAt("HWC 360", MARGIN, PAGE_HEIGHT - 26, PDType1Font.HELVETICA_BOLD, 11, 255, 255, 255);
            writeAt("Rapport diagnostic", PAGE_WIDTH - 170, PAGE_HEIGHT - 26, PDType1Font.HELVETICA, 9, 235, 255, 250);
        }

        private void footer() throws IOException {
            stream.setStrokingColor(226, 232, 240);
            stream.moveTo(MARGIN, 34);
            stream.lineTo(PAGE_WIDTH - MARGIN, 34);
            stream.stroke();
            writeAt("Harmony Works Consulting - Rapport confidentiel", MARGIN, 20, PDType1Font.HELVETICA, 8, MUTED_R, MUTED_G, MUTED_B);
            writeAt("Page " + pageNumber, PAGE_WIDTH - 84, 20, PDType1Font.HELVETICA, 8, MUTED_R, MUTED_G, MUTED_B);
        }

        private void card(String label, String title, String subtitle) throws IOException {
            roundedBox(MARGIN, y - 58, CONTENT_WIDTH, 72, 248, 250, 252, 226, 232, 240);
            writeAt(label.toUpperCase(), MARGIN + 18, y - 4, PDType1Font.HELVETICA_BOLD, 8, PRIMARY_R, PRIMARY_G, PRIMARY_B);
            writeAt(title, MARGIN + 18, y - 26, PDType1Font.HELVETICA_BOLD, 15, DARK_R, DARK_G, DARK_B);
            writeAt(subtitle, MARGIN + 18, y - 45, PDType1Font.HELVETICA, 10, MUTED_R, MUTED_G, MUTED_B);
            y -= 72;
        }

        private void drawScoreHero(Diagnostic diagnostic) throws IOException {
            roundedBox(MARGIN, y - 128, CONTENT_WIDTH, 138, 255, 255, 255, 203, 213, 225);
            writeAt("Score global", MARGIN + 20, y - 18, PDType1Font.HELVETICA_BOLD, 10, PRIMARY_R, PRIMARY_G, PRIMARY_B);
            writeAt(String.valueOf(diagnostic.getScoreGlobal()), MARGIN + 20, y - 70, PDType1Font.HELVETICA_BOLD, 42, DARK_R, DARK_G, DARK_B);
            writeAt("/100", MARGIN + 140, y - 70, PDType1Font.HELVETICA_BOLD, 16, MUTED_R, MUTED_G, MUTED_B);
            pill(MARGIN + 20, y - 102, 112, 24, diagnostic.getNiveauMaturite());
            writeAt("Niveau de maturite", MARGIN + 180, y - 44, PDType1Font.HELVETICA_BOLD, 12, DARK_R, DARK_G, DARK_B);
            paragraphAt("Le score global consolide les categories ponderees du diagnostic et sert de point de depart au plan d'action.", MARGIN + 180, y - 66, CONTENT_WIDTH - 210, 10.5f, 14);
            y -= 152;
        }

        private void drawKpiRow(Diagnostic diagnostic) throws IOException {
            float gap = 12;
            float width = (CONTENT_WIDTH - (gap * 2)) / 3;
            kpiBox(MARGIN, y, width, "Score", diagnostic.getScoreGlobal() + "/100");
            kpiBox(MARGIN + width + gap, y, width, "Niveau", valueOrDash(diagnostic.getNiveauMaturite()));
            kpiBox(MARGIN + (width + gap) * 2, y, width, "Diagnostic", "#" + diagnostic.getId());
            y -= 82;
        }

        private void kpiBox(float x, float topY, float width, String label, String value) throws IOException {
            roundedBox(x, topY - 58, width, 68, 248, 250, 252, 226, 232, 240);
            writeAt(label.toUpperCase(), x + 14, topY - 8, PDType1Font.HELVETICA_BOLD, 8, MUTED_R, MUTED_G, MUTED_B);
            writeAt(value, x + 14, topY - 34, PDType1Font.HELVETICA_BOLD, 18, DARK_R, DARK_G, DARK_B);
        }

        private void sectionTitle(String title) throws IOException {
            ensureSpace(40);
            writeAt(title, MARGIN, y, PDType1Font.HELVETICA_BOLD, 18, DARK_R, DARK_G, DARK_B);
            y -= 8;
            stream.setStrokingColor(PRIMARY_R, PRIMARY_G, PRIMARY_B);
            stream.setLineWidth(2);
            stream.moveTo(MARGIN, y);
            stream.lineTo(MARGIN + 70, y);
            stream.stroke();
            y -= 22;
        }

        private void subsection(String title, String text) throws IOException {
            ensureSpace(110);
            roundedBox(MARGIN, y - 92, CONTENT_WIDTH, 104, 255, 255, 255, 226, 232, 240);
            writeAt(title, MARGIN + 16, y - 12, PDType1Font.HELVETICA_BOLD, 12, PRIMARY_R, PRIMARY_G, PRIMARY_B);
            y -= 34;
            paragraphAt(text, MARGIN + 16, y, CONTENT_WIDTH - 32, 10, 14);
            y -= 78;
        }

        private void tableHeader() throws IOException {
            ensureSpace(34);
            stream.setNonStrokingColor(PRIMARY_R, PRIMARY_G, PRIMARY_B);
            stream.addRect(MARGIN, y - 20, CONTENT_WIDTH, 24);
            stream.fill();
            writeAt("Categorie", MARGIN + 10, y - 12, PDType1Font.HELVETICA_BOLD, 9, 255, 255, 255);
            writeAt("Score", MARGIN + 290, y - 12, PDType1Font.HELVETICA_BOLD, 9, 255, 255, 255);
            writeAt("Points", MARGIN + 365, y - 12, PDType1Font.HELVETICA_BOLD, 9, 255, 255, 255);
            writeAt("Niveau", MARGIN + 435, y - 12, PDType1Font.HELVETICA_BOLD, 9, 255, 255, 255);
            y -= 26;
        }

        private void tableRow(String categorie, String score, String points, String niveau) throws IOException {
            ensureSpace(28);
            stream.setStrokingColor(226, 232, 240);
            stream.addRect(MARGIN, y - 18, CONTENT_WIDTH, 24);
            stream.stroke();
            writeAt(categorie, MARGIN + 10, y - 10, PDType1Font.HELVETICA, 9, DARK_R, DARK_G, DARK_B);
            writeAt(score, MARGIN + 290, y - 10, PDType1Font.HELVETICA_BOLD, 9, DARK_R, DARK_G, DARK_B);
            writeAt(points, MARGIN + 365, y - 10, PDType1Font.HELVETICA, 9, MUTED_R, MUTED_G, MUTED_B);
            writeAt(niveau, MARGIN + 435, y - 10, PDType1Font.HELVETICA_BOLD, 9, PRIMARY_R, PRIMARY_G, PRIMARY_B);
            y -= 24;
        }

        private void recommendationCard(String title, String description, String horizon, String service, String impact) throws IOException {
            ensureSpace(128);
            roundedBox(MARGIN, y - 106, CONTENT_WIDTH, 118, 255, 255, 255, 226, 232, 240);
            pill(MARGIN + 14, y - 22, 92, 20, valueOrDash(horizon));
            writeAt(title, MARGIN + 120, y - 16, PDType1Font.HELVETICA_BOLD, 12, DARK_R, DARK_G, DARK_B);
            y -= 42;
            paragraphAt(description, MARGIN + 14, y, CONTENT_WIDTH - 28, 9.5f, 13);
            y -= 46;
            writeAt("Service HWC: " + valueOrDash(service), MARGIN + 14, y, PDType1Font.HELVETICA_BOLD, 9, PRIMARY_R, PRIMARY_G, PRIMARY_B);
            writeAt("Impact: " + valueOrDash(impact), MARGIN + 270, y, PDType1Font.HELVETICA, 9, MUTED_R, MUTED_G, MUTED_B);
            y -= 42;
        }

        private void paragraph(String text, float fontSize, int lineHeight) throws IOException {
            paragraphAt(text, MARGIN, y, CONTENT_WIDTH, fontSize, lineHeight);
        }

        private void paragraphAt(String text, float x, float startY, float width, float fontSize, int lineHeight) throws IOException {
            y = startY;
            String safeText = text == null || text.isBlank() ? "-" : text;
            int maxLength = Math.max(36, Math.round(width / (fontSize * 0.52f)));
            for (String paragraph : safeText.split("\\R")) {
                for (String line : wrap(paragraph, maxLength)) {
                    ensureSpace(lineHeight + 6);
                    writeAt(line, x, y, PDType1Font.HELVETICA, fontSize, DARK_R, DARK_G, DARK_B);
                    y -= lineHeight;
                }
                y -= 4;
            }
        }

        private void ensureSpace(float requiredHeight) throws IOException {
            if (y - requiredHeight < 58) {
                newPage(true);
            }
        }

        private void writeAt(String text, float x, float textY, PDType1Font font, float fontSize, int red, int green, int blue) throws IOException {
            stream.beginText();
            stream.setNonStrokingColor(red, green, blue);
            stream.setFont(font, fontSize);
            stream.newLineAtOffset(x, textY);
            stream.showText(toPdfText(text));
            stream.endText();
        }

        private void roundedBox(float x, float bottomY, float width, float height, int fillR, int fillG, int fillB, int strokeR, int strokeG, int strokeB) throws IOException {
            stream.setNonStrokingColor(fillR, fillG, fillB);
            stream.addRect(x, bottomY, width, height);
            stream.fill();
            stream.setStrokingColor(strokeR, strokeG, strokeB);
            stream.setLineWidth(0.7f);
            stream.addRect(x, bottomY, width, height);
            stream.stroke();
        }

        private void pill(float x, float bottomY, float width, float height, String text) throws IOException {
            stream.setNonStrokingColor(230, 247, 241);
            stream.addRect(x, bottomY, width, height);
            stream.fill();
            writeAt(text, x + 10, bottomY + 7, PDType1Font.HELVETICA_BOLD, 8, PRIMARY_R, PRIMARY_G, PRIMARY_B);
        }

        private String niveau(Score score) {
            if (score.getScore() == null) {
                return "-";
            }
            int value = score.getScore().intValue();
            if (value <= 30) {
                return "CRITIQUE";
            }
            if (value <= 50) {
                return "FAIBLE";
            }
            if (value <= 70) {
                return "MOYEN";
            }
            if (value <= 85) {
                return "BON";
            }
            return "EXCELLENT";
        }

        private String formatDate(Diagnostic diagnostic) {
            return diagnostic.getDateFin() == null ? "-" : diagnostic.getDateFin().format(DATE_FORMATTER);
        }

        private String valueOrDash(String value) {
            return value == null || value.isBlank() ? "-" : value;
        }

        private List<String> wrap(String text, int maxLength) {
            if (text == null || text.isBlank()) {
                return List.of("");
            }
            java.util.ArrayList<String> lines = new java.util.ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String word : text.split("\\s+")) {
                if (line.length() + word.length() + 1 > maxLength) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    if (!line.isEmpty()) {
                        line.append(" ");
                    }
                    line.append(word);
                }
            }
            if (!line.isEmpty()) {
                lines.add(line.toString());
            }
            return lines;
        }

        private String toPdfText(String text) {
            String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");

            String cleaned = normalized
                    .replace("’", "'")
                    .replace("“", "\"")
                    .replace("”", "\"")
                    .replace("–", "-")
                    .replace("—", "-")
                    .replace("•", "-")
                    .replace("✅", "[OK]")
                    .replace("⚠", "[!]")
                    .replace("❌", "[X]");

            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < cleaned.length(); index++) {
                char value = cleaned.charAt(index);
                if (value == '\t') {
                    builder.append(" ");
                } else if (value >= 32 && value <= 126) {
                    builder.append(value);
                } else {
                    builder.append("?");
                }
            }
            return builder.toString();
        }
    }
}
