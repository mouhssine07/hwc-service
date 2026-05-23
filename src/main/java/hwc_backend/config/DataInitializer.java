package hwc_backend.config;

import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.entity.Accompagnements;
import hwc_backend.entity.Certifications;
import hwc_backend.entity.ChiffresCles;
import hwc_backend.entity.ClientsConfiance;
import hwc_backend.entity.Etiquettes;
import hwc_backend.entity.OptionReponse;
import hwc_backend.entity.Pays;
import hwc_backend.entity.Question;
import hwc_backend.entity.RegleRecommandation;
import hwc_backend.entity.ServiceEtiquettes;
import hwc_backend.entity.ServiceEtiquettesId;
import hwc_backend.entity.ServiceFonctionnalites;
import hwc_backend.entity.Services;
import hwc_backend.entity.SousServiceAvantages;
import hwc_backend.entity.SousServiceEtapes;
import hwc_backend.entity.SousServiceFaqs;
import hwc_backend.entity.SousServiceFonctionnalites;
import hwc_backend.entity.SousServices;
import hwc_backend.entity.Temoignages;
import hwc_backend.repository.AccompagnementsRepository;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.CertificationsRepository;
import hwc_backend.repository.ChiffresClesRepository;
import hwc_backend.repository.ClientsConfianceRepository;
import hwc_backend.repository.EtiquettesRepository;
import hwc_backend.repository.OptionReponseRepository;
import hwc_backend.repository.PaysRepository;
import hwc_backend.repository.QuestionRepository;
import hwc_backend.repository.RegleRecommandationRepository;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.ServiceEtiquettesRepository;
import hwc_backend.repository.ServiceFonctionnalitesRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.repository.SousServiceAvantagesRepository;
import hwc_backend.repository.SousServiceEtapesRepository;
import hwc_backend.repository.SousServiceFaqsRepository;
import hwc_backend.repository.SousServiceFonctionnalitesRepository;
import hwc_backend.repository.SousServicesRepository;
import hwc_backend.repository.TemoignagesRepository;
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
    private final ServicesRepository servicesRepository;
    private final SousServicesRepository sousServicesRepository;
    private final EtiquettesRepository etiquettesRepository;
    private final ServiceEtiquettesRepository serviceEtiquettesRepository;
    private final ServiceFonctionnalitesRepository serviceFonctionnalitesRepository;
    private final SousServiceFonctionnalitesRepository sousServiceFonctionnalitesRepository;
    private final SousServiceAvantagesRepository sousServiceAvantagesRepository;
    private final SousServiceEtapesRepository sousServiceEtapesRepository;
    private final SousServiceFaqsRepository sousServiceFaqsRepository;
    private final AccompagnementsRepository accompagnementsRepository;
    private final ChiffresClesRepository chiffresClesRepository;
    private final TemoignagesRepository temoignagesRepository;
    private final CertificationsRepository certificationsRepository;
    private final ClientsConfianceRepository clientsConfianceRepository;
    private final PaysRepository paysRepository;
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

        Etiquettes acquisition = seedEtiquette("Acquisition");
        Etiquettes strategie = seedEtiquette("Strategie");
        Etiquettes digital = seedEtiquette("Digital");
        Etiquettes leadershipTag = seedEtiquette("Leadership");

        Services marketingService = seedService(
                "Marketing Digital",
                "Transformez votre visibilite en acquisition mesurable",
                "Audit, contenus, campagnes et optimisation pour attirer des prospects qualifies.",
                "megaphone"
        );
        Services leadershipService = seedService(
                "Leadership & Management",
                "Alignez les equipes autour d'une vision claire",
                "Coaching dirigeant, rituels de management et gouvernance operationnelle.",
                "users"
        );
        Services performanceService = seedService(
                "Performance Commerciale",
                "Structurez votre prospection et vos conversions",
                "Pipeline, scripts, suivi d'opportunites et fidelisation client.",
                "trending-up"
        );
        Services organisationService = seedService(
                "Organisation & Process",
                "Fluidifiez vos operations internes",
                "Cartographie des processus, responsabilites et indicateurs de productivite.",
                "settings"
        );

        seedServiceEtiquette(marketingService, acquisition);
        seedServiceEtiquette(marketingService, digital);
        seedServiceEtiquette(leadershipService, leadershipTag);
        seedServiceEtiquette(performanceService, acquisition);
        seedServiceEtiquette(organisationService, strategie);

        SousServices seoSea = seedSousService(
                marketingService,
                "SEO & SEA",
                "Gagnez en visibilite sur les moteurs de recherche",
                "Optimisation SEO, campagnes Google Ads et pilotage des conversions.",
                "Nous auditons les mots-cles, les pages prioritaires et les campagnes payantes pour construire un plan d'acquisition mesurable.",
                "search"
        );
        SousServices contenuReseaux = seedSousService(
                marketingService,
                "Contenu & Reseaux Sociaux",
                "Construisez une presence reguliere et credible",
                "Calendrier editorial, formats de contenu et animation LinkedIn/Instagram.",
                "Nous transformons vos expertises en contenus utiles, planifies et alignes avec vos objectifs commerciaux.",
                "message-square"
        );
        SousServices coachingDirigeants = seedSousService(
                leadershipService,
                "Coaching Dirigeants",
                "Renforcez la posture et la prise de decision",
                "Accompagnement individuel pour clarifier la vision, deleguer et manager sous pression.",
                "Le coaching aide les dirigeants a structurer leurs priorites, leur communication et leurs rituels de pilotage.",
                "target"
        );
        SousServices prospection = seedSousService(
                performanceService,
                "Prospection Commerciale",
                "Transformez votre pipeline en rendez-vous qualifies",
                "Segmentation, scripts, sequence de relance et indicateurs de conversion.",
                "Nous construisons une methode commerciale claire pour mieux suivre les prospects et convertir les opportunites.",
                "phone-call"
        );
        SousServices auditProcess = seedSousService(
                organisationService,
                "Audit Organisationnel",
                "Identifiez les blocages internes prioritaires",
                "Analyse des processus, roles, flux d'information et zones de friction.",
                "L'audit permet de prioriser les actions qui liberent du temps et rendent l'organisation plus scalable.",
                "clipboard-check"
        );

        seedServiceFonctionnalite(marketingService, "Audit de presence digitale et benchmark concurrentiel");
        seedServiceFonctionnalite(marketingService, "Plan d'acquisition avec KPI de conversion");
        seedServiceFonctionnalite(leadershipService, "Diagnostic managerial et plan de rituels d'equipe");
        seedServiceFonctionnalite(performanceService, "Pipeline commercial et suivi des opportunites");
        seedServiceFonctionnalite(organisationService, "Cartographie des processus et plan d'optimisation");

        seedSousServiceContent(seoSea,
                "Recherche mots-cles et structure SEO prioritaire",
                "Visibilite rapide", "Combinaison SEO durable et campagnes payantes pour generer des leads plus vite.",
                "Audit", "Analyse technique, mots-cles et pages a potentiel.",
                "Faut-il faire SEO et SEA ensemble ?", "Oui, le SEA genere des signaux rapides pendant que le SEO construit la visibilite long terme.",
                "Plan d'acquisition SEO/SEA", "Priorites mots-cles, campagnes, pages d'atterrissage et KPI de suivi.");
        seedSousServiceContent(contenuReseaux,
                "Calendrier editorial mensuel et formats de publication",
                "Regularite", "Une presence claire et constante renforce la confiance et la memorisation.",
                "Ligne editoriale", "Definition des themes, audiences et messages cles.",
                "Quels reseaux prioriser ?", "Nous choisissons les canaux selon votre cible et vos ressources.",
                "Systeme de contenu", "Planning, templates, routines de publication et mesure d'engagement.");
        seedSousServiceContent(coachingDirigeants,
                "Rituels de pilotage, feedback et delegation",
                "Clarte manageriale", "Des rituels simples ameliorent l'alignement et reduisent les urgences.",
                "Diagnostic leadership", "Entretien dirigeant, cartographie des tensions et objectifs de progression.",
                "Combien de temps dure l'accompagnement ?", "Le format se construit generalement sur 6 a 12 semaines.",
                "Coaching operationnel", "Sessions ciblees, exercices pratiques et suivi des decisions.");
        seedSousServiceContent(prospection,
                "Segmentation prospects, scripts et tableau de suivi",
                "Pipeline lisible", "Une methode claire rend la prospection plus reguliere et plus mesurable.",
                "Ciblage", "Definition des segments prioritaires et messages d'approche.",
                "Peut-on l'adapter a une petite equipe ?", "Oui, la methode est dimensionnee selon vos ressources commerciales.",
                "Systeme commercial", "Pipeline, scripts, relances et indicateurs hebdomadaires.");
        seedSousServiceContent(auditProcess,
                "Cartographie processus et plan d'amelioration priorise",
                "Moins de friction", "Les blocages critiques sont rendus visibles et transformes en actions.",
                "Observation", "Entretiens, analyse des flux et identification des irritants.",
                "Faut-il changer toute l'organisation ?", "Non, nous priorisons les ajustements les plus impactants.",
                "Plan process", "Roles, flux, indicateurs et quick wins operationnels.");

        seedChiffreCle("25+", "questions pour mesurer la maturite");
        seedChiffreCle("5", "axes de diagnostic");
        seedChiffreCle("100%", "plan d'action personnalise");
        seedTemoignage("BennaniTextile", "/images/placeholder-client.svg", "PME textile");
        seedCertification("HWC Diagnostic Framework", "/images/placeholder-certification.svg", "Methode");
        seedClientConfiance("BennaniTextile", "/images/placeholder-client.svg");
        seedPays("Maroc", "MA", "Casablanca");
        seedPays("France", "FR", "Paris");

        seedRegle(marketing, "50.00", "<", "Developper votre strategie Marketing Digital",
                "Structurer un plan marketing mesurable, renforcer la presence digitale et prioriser les canaux d'acquisition.",
                "COURT_TERME", "+8 a +12 points marketing", "Taux de conversion;Trafic qualifie;Leads generes", 1,
                marketingService, seoSea);
        seedRegle(leadership, "50.00", "<", "Renforcer votre leadership managerial",
                "Clarifier la vision, les rituels de management et le feedback pour aligner les equipes.",
                "MOYEN_TERME", "+6 a +10 points leadership", "Rituels managers;Feedbacks realises;Objectifs suivis", 2,
                leadershipService, coachingDirigeants);
        seedRegle(maturiteDigitale, "60.00", "<", "Moderniser votre maturite digitale",
                "Mettre en place les outils, donnees et automatisations prioritaires pour fluidifier les operations.",
                "LONG_TERME", "+8 a +15 points digital", "Process digitalises;Donnees centralisees;Automatisations actives", 3,
                marketingService, seoSea);
        seedRegle(performanceCommerciale, "65.00", "<", "Optimiser votre performance commerciale",
                "Formaliser la prospection, le suivi des opportunites et la fidelisation client.",
                "MOYEN_TERME", "+6 a +12 points commercial", "Taux conversion;Pipeline suivi;Clients fidelises", 2,
                performanceService, prospection);
        seedRegle(organisation, "60.00", "<", "Restructurer votre organisation interne",
                "Documenter les processus et lever les blocages operationnels pour soutenir la croissance.",
                "LONG_TERME", "+5 a +10 points organisation", "Process formalises;Delais reduits;Productivite equipe", 3,
                organisationService, auditProcess);
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

    private Services seedService(String titre, String accroche, String description, String icone) {
        return servicesRepository.findAll().stream()
                .filter(service -> titre.equals(service.getTitre()))
                .findFirst()
                .orElseGet(() -> servicesRepository.save(new Services(null, accroche, titre, description, icone)));
    }

    private SousServices seedSousService(
            Services service,
            String titre,
            String accroche,
            String description,
            String descriptionComplete,
            String icone
    ) {
        return sousServicesRepository.findAll().stream()
                .filter(sousService -> titre.equals(sousService.getTitre()))
                .findFirst()
                .orElseGet(() -> sousServicesRepository.save(
                        new SousServices(null, titre, accroche, description, descriptionComplete, icone, service)
                ));
    }

    private Etiquettes seedEtiquette(String nom) {
        return etiquettesRepository.findAll().stream()
                .filter(etiquette -> nom.equals(etiquette.getNom()))
                .findFirst()
                .orElseGet(() -> etiquettesRepository.save(new Etiquettes(null, nom)));
    }

    private void seedServiceEtiquette(Services service, Etiquettes etiquette) {
        ServiceEtiquettesId id = new ServiceEtiquettesId(service.getId(), etiquette.getId());
        if (!serviceEtiquettesRepository.existsById(id)) {
            serviceEtiquettesRepository.save(new ServiceEtiquettes(id, service, etiquette));
        }
    }

    private void seedServiceFonctionnalite(Services service, String contenu) {
        boolean exists = serviceFonctionnalitesRepository.findAll().stream()
                .anyMatch(item -> item.getService().getId().equals(service.getId()) && contenu.equals(item.getContenu()));
        if (!exists) {
            serviceFonctionnalitesRepository.save(new ServiceFonctionnalites(null, contenu, service));
        }
    }

    private void seedSousServiceContent(
            SousServices sousService,
            String fonctionnalite,
            String avantageTitre,
            String avantageDescription,
            String etapeTitre,
            String etapeDescription,
            String faqQuestion,
            String faqReponse,
            String accompagnementTitre,
            String accompagnementDescription
    ) {
        seedSousServiceFonctionnalite(sousService, fonctionnalite);
        seedSousServiceAvantage(sousService, avantageTitre, avantageDescription);
        seedSousServiceEtape(sousService, 1, etapeTitre, etapeDescription);
        seedSousServiceFaq(sousService, faqQuestion, faqReponse);
        seedAccompagnement(sousService, accompagnementTitre, "Accompagnement HWC", accompagnementDescription,
                "Un consultant HWC suit les actions, les indicateurs et les ajustements prioritaires.");
    }

    private void seedSousServiceFonctionnalite(SousServices sousService, String contenu) {
        boolean exists = sousServiceFonctionnalitesRepository.findAll().stream()
                .anyMatch(item -> item.getSousService().getId().equals(sousService.getId()) && contenu.equals(item.getContenu()));
        if (!exists) {
            sousServiceFonctionnalitesRepository.save(new SousServiceFonctionnalites(null, contenu, sousService));
        }
    }

    private void seedSousServiceAvantage(SousServices sousService, String titre, String description) {
        boolean exists = sousServiceAvantagesRepository.findAll().stream()
                .anyMatch(item -> item.getSousService().getId().equals(sousService.getId()) && titre.equals(item.getTitre()));
        if (!exists) {
            sousServiceAvantagesRepository.save(new SousServiceAvantages(null, titre, description, sousService));
        }
    }

    private void seedSousServiceEtape(SousServices sousService, int numero, String titre, String description) {
        boolean exists = sousServiceEtapesRepository.findAll().stream()
                .anyMatch(item -> item.getSousService().getId().equals(sousService.getId()) && numero == item.getNumero());
        if (!exists) {
            sousServiceEtapesRepository.save(new SousServiceEtapes(null, numero, titre, description, sousService));
        }
    }

    private void seedSousServiceFaq(SousServices sousService, String question, String reponse) {
        boolean exists = sousServiceFaqsRepository.findAll().stream()
                .anyMatch(item -> item.getSousService().getId().equals(sousService.getId()) && question.equals(item.getQuestion()));
        if (!exists) {
            sousServiceFaqsRepository.save(new SousServiceFaqs(null, question, reponse, sousService));
        }
    }

    private void seedAccompagnement(SousServices sousService, String titre, String accroche, String description1, String description2) {
        boolean exists = accompagnementsRepository.findAll().stream()
                .anyMatch(item -> item.getSousService().getId().equals(sousService.getId()) && titre.equals(item.getTitre()));
        if (!exists) {
            accompagnementsRepository.save(new Accompagnements(null, titre, accroche, description1, description2, sousService));
        }
    }

    private void seedChiffreCle(String valeur, String libelle) {
        boolean exists = chiffresClesRepository.findAll().stream()
                .anyMatch(item -> valeur.equals(item.getValeur()) && libelle.equals(item.getLibelle()));
        if (!exists) {
            chiffresClesRepository.save(new ChiffresCles(null, valeur, libelle));
        }
    }

    private void seedTemoignage(String nom, String logoUrl, String type) {
        boolean exists = temoignagesRepository.findAll().stream()
                .anyMatch(item -> nom.equals(item.getNom()));
        if (!exists) {
            temoignagesRepository.save(new Temoignages(null, nom, logoUrl, type));
        }
    }

    private void seedCertification(String nom, String logoUrl, String type) {
        boolean exists = certificationsRepository.findAll().stream()
                .anyMatch(item -> nom.equals(item.getNom()));
        if (!exists) {
            certificationsRepository.save(new Certifications(null, nom, logoUrl, type));
        }
    }

    private void seedClientConfiance(String nom, String logoUrl) {
        boolean exists = clientsConfianceRepository.findAll().stream()
                .anyMatch(item -> nom.equals(item.getNom()));
        if (!exists) {
            clientsConfianceRepository.save(new ClientsConfiance(null, nom, logoUrl));
        }
    }

    private void seedPays(String nom, String codePays, String ville) {
        boolean exists = paysRepository.findAll().stream()
                .anyMatch(item -> nom.equals(item.getNom()) && ville.equals(item.getVille()));
        if (!exists) {
            paysRepository.save(new Pays(null, nom, codePays, ville));
        }
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
            int priorite,
            Services service,
            SousServices sousService
    ) {
        RegleRecommandation existing = regleRecommandationRepository.findAll().stream()
                .filter(regle -> regle.getCategorie().getId().equals(categorie.getId())
                        && titre.equals(regle.getTitreRecommandation()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setServiceHwc(service);
            existing.setSousServiceHwc(sousService);
            regleRecommandationRepository.save(existing);
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
        regle.setServiceHwc(service);
        regle.setSousServiceHwc(sousService);
        regle.setActif(true);
        regleRecommandationRepository.save(regle);
    }
}
