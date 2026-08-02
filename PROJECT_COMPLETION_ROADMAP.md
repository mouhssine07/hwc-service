# HWC — Roadmap de finalisation du projet

Ce document est la checklist de reference pour amener HWC de son etat actuel a un produit termine, teste et deployable.

## Etat actuel

- Branche de travail : `feature/new-functionality`
- Phase 1 terminee : site vitrine, administration, authentification admin, CRUD, upload et API publique.
- Phase 2 deja terminee : authentification client, diagnostic, recommandations, dashboard BI, rapports PDF IA et chatbot IA.
- Prochain module fonctionnel : **Module 7B — Coach IA hebdomadaire**.

## Priorite P0 — Terminer les fonctionnalites produit

Ces elements sont indispensables pour considerer le perimetre fonctionnel Phase 2 comme termine.

### Module 7B — Coach IA hebdomadaire

- [x] Creer les entites `CoachObjectifsHebdo`, `CoachObjectifResultat` et `CoachEmailEnvoye`.
- [x] Creer leurs repositories et DTOs.
- [x] Implementer `CoachIAService` et `CoachIAServiceImpl`.
- [x] Generer trois objectifs depuis le dernier diagnostic finalise et ses recommandations.
- [x] Utiliser Ollama local par defaut, avec un fallback fiable si la generation IA echoue.
- [x] Sauvegarder les objectifs par semaine pour eviter toute regeneration a chaque ouverture.
- [x] Ajouter le suivi de realisation de chaque objectif.
- [x] Conserver l'historique des semaines precedentes.
- [x] Ajouter `GET /api/client/coach/current-week`.
- [x] Ajouter `PATCH /api/client/coach/objectifs/{id}/complete`.
- [x] Ajouter `GET /api/client/coach/history`.
- [x] Verifier strictement qu'un client ne peut acceder ou modifier que ses propres objectifs.
- [x] Ajouter le scheduler Coach IA pour la generation hebdomadaire et les rappels lundi/vendredi.
- [x] Prevoir un comportement sans SMTP afin que le module reste utilisable en developpement.
- [x] Creer `coachApi.js`.
- [x] Creer la page `/client/coach`.
- [x] Creer `ObjectifCard`, `WeekProgress` et `CoachHistory`.
- [x] Ajouter un acces visible au Coach depuis les pages client pertinentes.
- [x] Ajouter un bouton pour ouvrir l'Assistant IA existant depuis la page Coach.
- [x] Prevoir les etats chargement, erreur, aucun diagnostic, aucun objectif et historique vide.
- [x] Limiter le programme Coach IA a 12 semaines par diagnostic.
- [x] Afficher le numero de semaine et la progression globale du programme.
- [x] Ajouter une quantite cible, une quantite realisee, une unite et un commentaire client par objectif.
- [x] Gerer les statuts reels : a faire, en cours et termine.
- [x] Permettre au client d'enregistrer une progression partielle, par exemple 1/3 publications.
- [x] Generer un bilan du vendredi uniquement depuis les resultats declares par le client.
- [x] Reporter en priorite les objectifs non termines vers la semaine suivante.
- [x] Ne jamais inventer de leads, resultats commerciaux ou progression de score.
- [x] Ajouter les emails HTML du lundi et du vendredi avec liens vers le dashboard et le Coach.
- [x] Rendre SMTP facultatif en developpement avec les statuts `PREPARE`, `ENVOYE` et `ECHEC`.
- [x] Planifier le lundi a 08:00 et le vendredi a 17:00 dans le fuseau `Africa/Casablanca`.
- [x] Afficher trois cartes fixes dans les services HWC : SEO & SEA, Coaching Dirigeants et Refonte Site Web.
- [x] Reserver le Coach IA a Coaching Dirigeants et proposer un rendez-vous humain pour les autres services.
- [x] Enrichir les demandes de rendez-vous avec nom, telephone, service, message et date.
- [x] Ajouter un benchmark sectoriel reel base sur le dernier diagnostic des clients actifs.
- [x] Exiger au moins 10 entreprises du meme secteur avant d'afficher moyenne et top 10 %.
- [x] Afficher la position du client et trois entreprises mieux classees sous forme anonymisee.

### Verification fonctionnelle Phase 2

- [ ] Verifier le parcours client complet : inscription, connexion, diagnostic, resultat, recommandations, dashboard, PDF, chatbot et coach.
- [ ] Verifier qu'un client ne peut jamais consulter les donnees d'un autre client.
- [ ] Verifier qu'un compte client desactive perd immediatement l'acces.
- [ ] Verifier les parcours sans diagnostic finalise et sans recommandations.
- [ ] Verifier que toutes les routes client sont protegees et toutes les routes publiques restent accessibles.
- [ ] Corriger les anomalies fonctionnelles trouvees.

## Priorite P1 — Qualite, tests et securite

Ces elements sont requis avant de presenter ou publier l'application.

### Tests automatises

- [ ] Completer les tests backend du Coach IA : validation de propriete, historique, bilan du vendredi, adaptation et emails.
- [x] Tester l'integration Coach : semaine courante, programme 12 semaines, trois objectifs, progression chiffree et suppression liee.
- [ ] Completer les tests des cas limites pour diagnostic, recommandations, dashboard, PDF et chatbot.
- [ ] Ajouter des tests d'integration des endpoints sensibles (authentification et autorisations).
- [ ] Ajouter, si possible, des tests frontend pour les parcours critiques.
- [ ] Executer `./mvnw.cmd test` sans echec.
- [ ] Executer `npm.cmd run build` dans `hwc-frontend` sans echec.
- [ ] Realiser une recette manuelle desktop et mobile.

### Securite applicative

- [ ] Remplacer le mot de passe admin par defaut.
- [ ] Verifier que tous les secrets sont exclusivement dans des variables d'environnement ou fichiers non versionnes.
- [ ] Ne jamais versionner les cles OpenAI/Ollama, JWT, SMTP ou mots de passe MySQL.
- [ ] Configurer une cle JWT longue et propre a chaque environnement.
- [ ] Configurer CORS avec les domaines frontend reels en production.
- [ ] Verifier la validation serveur de toutes les donnees entrantes.
- [ ] Verifier taille, type et nom des fichiers televerses.
- [ ] Ajouter une limitation de debit sur login, inscription et endpoints IA si l'application est exposee publiquement.
- [ ] Masquer les details techniques des erreurs en production.
- [ ] Verifier les droits admin/client sur chaque endpoint.

### Stabilite et experience utilisateur

- [ ] Verifier chaque page sur mobile, tablette et desktop.
- [ ] Harmoniser les etats de chargement, vide et erreur.
- [ ] Verifier l'accessibilite minimale : contrastes, labels de formulaire, navigation clavier et messages d'erreur.
- [ ] Verifier les textes, accents, dates, fuseaux horaires et formats francais.
- [ ] Verifier les performances des pages dashboard et chatbot avec des donnees reelles.
- [ ] Ajouter des logs utiles cote backend sans journaliser de secrets ni de donnees sensibles inutilement.

## Priorite P2 — Configuration de production

Ces elements rendent le projet deployable et maintenable en conditions reelles.

### Environnements et donnees

- [ ] Definir les variables d'environnement de developpement, test et production.
- [ ] Preparer une base MySQL de production avec un utilisateur dedie et droits limites.
- [ ] Configurer la sauvegarde reguliere de la base de donnees et tester une restauration.
- [ ] Decider ou stocker durablement les images televersees et les PDFs generes.
- [ ] Ajouter une strategie de migration/versionnement de schema de base de donnees (par exemple Flyway) si le projet evolue en production.
- [ ] Preparer des donnees de demonstration distinctes des donnees de production.

### Services externes

- [ ] Configurer Ollama sur une machine/service accessible, ou choisir OpenAI comme fallback production.
- [ ] Definir les limites de cout et de temps de reponse de l'IA.
- [ ] Configurer SMTP pour les emails du Coach IA.
- [ ] Verifier l'envoi d'un email de test et les rappels lundi/vendredi.
- [ ] Prevoir un fallback et une trace quand un service IA ou email est indisponible.

### Deploiement

- [ ] Choisir l'hebergement du frontend, backend, base de donnees, stockage et IA.
- [ ] Configurer les profils Spring Boot de production.
- [ ] Configurer l'URL API de production dans le frontend.
- [ ] Configurer les domaines, DNS et HTTPS.
- [ ] Configurer CORS, cookies/tokens et URLs de fichiers pour les domaines deployes.
- [ ] Automatiser le build et le deploiement si possible (CI/CD).
- [ ] Ajouter un endpoint de sante ou une supervision basique du backend.
- [ ] Verifier les logs et les redemarrages apres deploiement.
- [ ] Executer la recette complete sur l'environnement de production.

## Priorite P3 — Livraison et maintien

Ces elements finalisent une livraison professionnelle et facilitent la suite du projet.

- [ ] Nettoyer les fichiers locaux non necessaires avant commit (`*.log`, fichiers d'etat, artefacts temporaires).
- [ ] Verifier `git status` et ne versionner que les fichiers utiles.
- [ ] Mettre a jour le README avec les prerequis, variables d'environnement et commandes de lancement.
- [ ] Documenter la configuration MySQL, SMTP, IA et stockage de fichiers.
- [ ] Documenter les comptes/demo ou la procedure de creation d'un admin.
- [ ] Preparer un guide utilisateur court pour le client et l'administrateur.
- [ ] Preparer un scenario de demonstration : vitrine -> inscription -> diagnostic -> recommandations -> dashboard -> PDF -> chatbot -> coach.
- [ ] Preparer une liste des limites connues et evolutions futures.
- [ ] Realiser le commit final sur `feature/new-functionality` et pousser vers `origin`.
- [ ] Ne merger vers `main` qu'apres validation finale.

## Definition de termine

Le projet est considere pret lorsque :

- [ ] Le Module 7B est termine et valide.
- [ ] Les tests backend et le build frontend passent.
- [ ] Les parcours admin et client sont testes de bout en bout.
- [ ] Les secrets et droits d'acces sont securises.
- [ ] Les services de production sont configures et sauvegardes.
- [ ] L'application est accessible en HTTPS et validee dans son environnement de deploiement.
- [ ] La documentation et la demonstration sont pretes.

## Reprise prochaine session

Dernier etat valide le 30 juillet 2026 :

- Le build frontend `npm.cmd run build` passe.
- La compilation backend passe.
- Le test d'integration client couvre connexion, diagnostic finalise, dashboard, Coach IA, progression chiffree et suppression.
- Les modifications locales ne sont pas encore committees.
- Le backend doit etre completement redemarre pour que Hibernate ajoute les nouveaux champs Coach et demandes de rendez-vous dans MySQL.

Ordre de reprise recommande :

1. Redemarrer le backend et le frontend.
2. Se connecter avec un client possedant un diagnostic finalise.
3. Verifier les trois cartes fixes de services dans le dashboard.
4. Ouvrir Coaching Dirigeants puis verifier la semaine, les trois objectifs et la saisie de progression.
5. Enregistrer une progression partielle, actualiser la page et verifier sa persistance.
6. Tester le formulaire de rendez-vous SEO & SEA et Refonte Site Web, puis verifier la demande dans l'administration.
7. Verifier le benchmark sectoriel :
   - moins de 10 entreprises : message de donnees insuffisantes ;
   - au moins 10 entreprises : moyenne, top 10 %, position et concurrents anonymises.
8. Configurer un SMTP de test et verifier les emails HTML du lundi et du vendredi.
9. Ajouter les tests encore manquants : propriete, historique, bilan, adaptation, scheduler et echec SMTP.
10. Continuer la verification fonctionnelle complete de la Phase 2.

Variables necessaires pour activer les emails :

```env
COACH_EMAIL_ENABLED=true
COACH_EMAIL_FROM=coach@hwc.com
SMTP_HOST=votre-serveur-smtp
SMTP_PORT=587
SMTP_USERNAME=votre-utilisateur
SMTP_PASSWORD=votre-mot-de-passe
SMTP_AUTH=true
SMTP_STARTTLS=true
COACH_FRONTEND_URL=http://localhost:5173
COACH_TIME_ZONE=Africa/Casablanca
```
