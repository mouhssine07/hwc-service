package hwc_backend.config;

import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.entity.OptionReponse;
import hwc_backend.entity.Question;
import hwc_backend.entity.RegleRecommandation;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.OptionReponseRepository;
import hwc_backend.repository.QuestionRepository;
import hwc_backend.repository.RegleRecommandationRepository;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${ADMIN_EMAIL:admin@hwc.com}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:Admin@2026}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategorieDiagnosticRepository categorieDiagnosticRepository;
    private final QuestionRepository questionRepository;
    private final OptionReponseRepository optionReponseRepository;
    private final RegleRecommandationRepository regleRecommandationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByNom("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        roleRepository.findByNom("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

        roleRepository.findByNom("ROLE_CLIENT")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_CLIENT")));

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setNom("Admin");
            admin.setPrenom("HWC");
            admin.setActif(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }

        CategorieDiagnostic maturiteDigitale = seedCategorie("Maturite digitale", "Presence en ligne, outils, process digitaux", "laptop", new BigDecimal("20.00"), 1);
        CategorieDiagnostic performanceCommerciale = seedCategorie("Performance commerciale", "Acquisition, conversion, fidelisation clients", "trending-up", new BigDecimal("25.00"), 2);
        CategorieDiagnostic leadership = seedCategorie("Leadership & Management", "Style de management, communication, vision dirigeants", "users", new BigDecimal("15.00"), 3);
        CategorieDiagnostic organisation = seedCategorie("Performance organisationnelle", "Structure interne, processus, agilite operationnelle", "settings", new BigDecimal("15.00"), 4);
        CategorieDiagnostic marketing = seedCategorie("Marketing & Communication", "Positionnement de marque, branding, contenus publies", "megaphone", new BigDecimal("25.00"), 5);

        seedQuestions(maturiteDigitale, new String[]{
                "Votre entreprise dispose-t-elle d'un site web professionnel et a jour ?",
                "Utilisez-vous des outils numeriques pour gerer vos operations quotidiennes ?",
                "Vos donnees clients sont-elles centralisees dans un CRM ou un outil equivalent ?",
                "Vos processus internes sont-ils documentes et partiellement automatises ?",
                "Suivez-vous des indicateurs digitaux pour piloter votre activite ?"
        });
        seedQuestions(performanceCommerciale, new String[]{
                "Avez-vous un processus clair de prospection commerciale ?",
                "Mesurez-vous le taux de conversion de vos prospects en clients ?",
                "Disposez-vous d'une strategie de fidelisation client structuree ?",
                "Votre equipe commerciale utilise-t-elle des objectifs et tableaux de suivi ?",
                "Analysez-vous regulierement les causes de perte d'opportunites ?"
        });
        seedQuestions(leadership, new String[]{
                "La vision de l'entreprise est-elle clairement communiquee aux equipes ?",
                "Les responsabilites de chaque collaborateur sont-elles bien definies ?",
                "Organisez-vous des points de suivi reguliers avec vos equipes ?",
                "Vos managers donnent-ils un feedback constructif et frequent ?",
                "Les decisions importantes sont-elles prises avec des donnees fiables ?"
        });
        seedQuestions(organisation, new String[]{
                "Vos processus operationnels sont-ils formalises et accessibles ?",
                "Les equipes collaborent-elles efficacement entre les departements ?",
                "Identifiez-vous et corrigez-vous les blocages operationnels rapidement ?",
                "Votre organisation peut-elle absorber une croissance d'activite ?",
                "Disposez-vous d'indicateurs pour mesurer la productivite interne ?"
        });
        seedQuestions(marketing, new String[]{
                "Votre positionnement de marque est-il clair pour vos clients cibles ?",
                "Publiez-vous regulierement du contenu sur vos canaux digitaux ?",
                "Vos campagnes marketing sont-elles planifiees et mesurees ?",
                "Votre communication met-elle en avant des preuves et cas clients ?",
                "Analysez-vous les performances de vos actions marketing ?"
        });

        seedRegle(marketing, "50.00", "<", "Developper votre strategie Marketing Digital",
                "Structurer un plan marketing mesurable, renforcer la presence digitale et prioriser les canaux d'acquisition.",
                "COURT_TERME", "+8 a +12 points marketing", "Taux de conversion;Trafic qualifie;Leads generes", 1);
        seedRegle(leadership, "50.00", "<", "Renforcer votre leadership managerial",
                "Clarifier la vision, les rituels de management et le feedback pour aligner les equipes.",
                "MOYEN_TERME", "+6 a +10 points leadership", "Rituels managers;Feedbacks realises;Objectifs suivis", 2);
        seedRegle(maturiteDigitale, "60.00", "<", "Moderniser votre maturite digitale",
                "Mettre en place les outils, donnees et automatisations prioritaires pour fluidifier les operations.",
                "LONG_TERME", "+8 a +15 points digital", "Process digitalises;Donnees centralisees;Automatisations actives", 3);
        seedRegle(performanceCommerciale, "65.00", "<", "Optimiser votre performance commerciale",
                "Formaliser la prospection, le suivi des opportunites et la fidelisation client.",
                "MOYEN_TERME", "+6 a +12 points commercial", "Taux conversion;Pipeline suivi;Clients fidelises", 2);
        seedRegle(organisation, "60.00", "<", "Restructurer votre organisation interne",
                "Documenter les processus et lever les blocages operationnels pour soutenir la croissance.",
                "LONG_TERME", "+5 a +10 points organisation", "Process formalises;Delais reduits;Productivite equipe", 3);
    }

    private CategorieDiagnostic seedCategorie(String nom, String description, String icone, BigDecimal poids, int ordre) {
        return categorieDiagnosticRepository.findByNom(nom)
                .orElseGet(() -> categorieDiagnosticRepository.save(
                        new CategorieDiagnostic(null, nom, description, icone, poids, ordre)
                ));
    }

    private void seedQuestions(CategorieDiagnostic categorie, String[] questions) {
        if (questionRepository.countByCategorieIdAndActifTrue(categorie.getId()) > 0) {
            return;
        }

        for (int i = 0; i < questions.length; i++) {
            Question question = questionRepository.save(new Question(null, categorie, questions[i], i + 1, true));
            seedOption(question, "Pas du tout en place", 0, 1);
            seedOption(question, "Partiellement en place", 2, 2);
            seedOption(question, "En place mais a optimiser", 4, 3);
            seedOption(question, "Bien structure et mesure", 5, 4);
        }
    }

    private void seedOption(Question question, String texte, int poids, int ordre) {
        optionReponseRepository.save(new OptionReponse(null, question, texte, poids, ordre));
    }

    private void seedRegle(
            CategorieDiagnostic categorie,
            String seuil,
            String operateur,
            String titre,
            String description,
            String horizon,
            String impact,
            String kpis,
            int priorite
    ) {
        if (regleRecommandationRepository.existsByCategorieIdAndTitreRecommandation(categorie.getId(), titre)) {
            return;
        }

        RegleRecommandation regle = new RegleRecommandation();
        regle.setCategorie(categorie);
        regle.setSeuilScore(new BigDecimal(seuil));
        regle.setOperateur(operateur);
        regle.setTitreRecommandation(titre);
        regle.setDescriptionRecommandation(description);
        regle.setHorizon(horizon);
        regle.setImpactEstime(impact);
        regle.setKpisSuggeres(kpis);
        regle.setPriorite(priorite);
        regle.setActif(true);
        regleRecommandationRepository.save(regle);
    }
}
