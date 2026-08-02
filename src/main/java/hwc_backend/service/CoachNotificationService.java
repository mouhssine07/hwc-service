package hwc_backend.service;

import hwc_backend.entity.CoachObjectifResultat;
import hwc_backend.entity.CoachObjectifsHebdo;
import hwc_backend.entity.User;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachNotificationService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${coach.email.enabled:false}")
    private boolean enabled;

    @Value("${coach.email.from:coach@hwc.com}")
    private String from;

    @Value("${coach.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public boolean isEnabled() {
        return enabled;
    }

    public void sendMondayObjectives(User user, CoachObjectifsHebdo week, List<CoachObjectifResultat> objectives) {
        StringBuilder items = new StringBuilder();
        for (CoachObjectifResultat objective : objectives) {
            items.append("<li style='margin-bottom:14px'><strong>")
                    .append(escape(objective.getTitre()))
                    .append("</strong><br>")
                    .append(escape(objective.getDescription()))
                    .append("<br><small>Objectif : ")
                    .append(objective.getQuantiteCible())
                    .append(" ")
                    .append(escape(objective.getUnite()))
                    .append("</small></li>");
        }
        String html = template(
                "Vos objectifs de la semaine",
                "Bonjour " + escape(user.getPrenom()) + " !",
                "<p>Voici votre plan personnalisé pour la semaine " + week.getNumeroSemaine() + "/12.</p>"
                        + "<h3>Objectifs prioritaires</h3><ol>" + items + "</ol>"
                        + "<p><strong>Conseil du Coach :</strong> " + escape(week.getConseilSemaine()) + "</p>"
        );
        send(user.getEmail(), "Vos objectifs de la semaine", html);
    }

    public void sendFridaySummary(User user, CoachObjectifsHebdo week, List<CoachObjectifResultat> objectives) {
        StringBuilder items = new StringBuilder();
        for (CoachObjectifResultat objective : objectives) {
            String status = objective.isTermine() ? "✅" : objective.getQuantiteRealisee() > 0 ? "⚠️" : "❌";
            items.append("<li style='margin-bottom:12px'>")
                    .append(status).append(" <strong>").append(escape(objective.getTitre())).append("</strong>")
                    .append("<br><small>").append(objective.getQuantiteRealisee()).append("/")
                    .append(objective.getQuantiteCible()).append(" ").append(escape(objective.getUnite())).append("</small></li>");
        }
        String html = template(
                "Bilan de votre semaine",
                "Bonjour " + escape(user.getPrenom()) + " !",
                "<p>Voici votre bilan basé sur les résultats que vous avez renseignés.</p>"
                        + "<ul style='padding-left:20px'>" + items + "</ul>"
                        + "<h3>Insight du Coach IA</h3><p>" + escape(week.getInsightIa()) + "</p>"
                        + "<p>Les objectifs non terminés seront pris en compte dans le prochain plan.</p>"
        );
        send(user.getEmail(), "Bilan de votre semaine", html);
    }

    private String template(String title, String greeting, String content) {
        return "<div style='font-family:Arial,sans-serif;max-width:680px;margin:auto;color:#1f2937'>"
                + "<div style='background:#0f766e;color:white;padding:24px;border-radius:12px 12px 0 0'><h1>" + title + "</h1></div>"
                + "<div style='border:1px solid #e5e7eb;padding:24px'><h2>" + greeting + "</h2>" + content
                + "<p style='margin-top:28px'><a href='" + frontendUrl + "/client/dashboard'>Voir mon dashboard</a>"
                + " &nbsp;|&nbsp; <a href='" + frontendUrl + "/client/coach'>Parler au Coach IA</a></p>"
                + "<p>Bon courage !<br>L'équipe HWC</p></div></div>";
    }

    private void send(String to, String subject, String html) {
        try {
            JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
            if (mailSender == null) {
                throw new IllegalStateException("Aucun service SMTP n'est configure");
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception exception) {
            throw new IllegalStateException("Echec d'envoi de l'email Coach", exception);
        }
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
