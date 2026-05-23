# 🚀 HWC Platform — Phase 2 : Plateforme Intelligente

## Guide complet pour l'assistant de codage

---

## 📌 Contexte du projet

**Projet :** Harmony Works Consulting (HWC) — Plateforme intelligente d'aide à la décision  
**Stack :** React 18 + Tailwind CSS (Frontend) · Spring Boot 3.x + MySQL 8 (Backend)  
**Site live :** https://hwc-service.netlify.app/  
**État actuel :** Phase 1 terminée ✅ (Site vitrine + Dashboard Admin)

### ✅ Phase 1 — TERMINÉE (Ne pas modifier)

- Module 1 : Site vitrine public complet
- Module 2 : Dashboard Administrateur complet (CRUD, Auth JWT, Upload images)

### 🔨 Phase 2 — À CONSTRUIRE (Ce fichier)

- Module 3 : Plateforme de Diagnostic Intelligent
- Module 4 : Moteur de Recommandations
- Module 5 : Dashboard Décisionnel (BI)
- Module 6 : Génération de Rapports PDF par IA
- Module 7 : Assistant IA Chatbot RAG + Coach IA hebdomadaire

---

## Etat d'avancement reel au 23 mai 2026

### Terminé

- Authentification client backend ajoutée sur le modèle `User` / `Role` existant
- Rôle `ROLE_CLIENT` ajouté
- Endpoints disponibles :
  - `POST /api/client/auth/register`
  - `POST /api/client/auth/login`
  - `GET /api/client/auth/me`
- Champs client ajoutés à `users` :
  - `entreprise`
  - `secteur`
  - `taille_entreprise`
  - `telephone`
- Séparation des routes :
  - `/api/admin/**` réservé à `ROLE_ADMIN`
  - `/api/client/**` réservé à `ROLE_CLIENT`
- Fondations backend du diagnostic créées :
  - Entités : `CategorieDiagnostic`, `Question`, `OptionReponse`, `Diagnostic`, `ReponseDiagnostic`, `Score`
  - Enum : `DiagnosticStatut`
  - Repositories associés
- Seed automatique ajouté pour les 5 catégories de diagnostic
- DTOs du module diagnostic ajoutés :
  - `CategorieDiagnosticDTO`
  - `QuestionDTO`
  - `OptionReponseDTO`
  - `DiagnosticStartDTO`
  - `RepondreQuestionDTO`
  - `DiagnosticResultatDTO`
  - `ScoreDTO`
- Backend du diagnostic implémenté :
  - `ScoringService`
  - `DiagnosticService`
  - `DiagnosticController`
- Endpoints diagnostic disponibles :
  - `POST /api/client/diagnostics/start`
  - `GET /api/client/diagnostics/questions`
  - `POST /api/client/diagnostics/{id}/reponses`
  - `POST /api/client/diagnostics/{id}/finalize`
  - `GET /api/client/diagnostics/history`
  - `GET /api/client/diagnostics/{id}`
- Seed automatique ajouté pour les 25 questions du diagnostic et leurs options de réponse
- Test fonctionnel backend ajouté pour le parcours diagnostic client complet
- Frontend client ajouté :
  - `clientAuthStore.js`
  - `clientAxiosInstance.js`
  - `clientAuthApi.js`
  - `diagnosticApi.js`
  - `ClientProtectedRoute.jsx`
  - `ClientLoginPage.jsx`
  - `ClientRegisterPage.jsx`
  - `DiagnosticStartPage.jsx`
  - `DiagnosticQuestionsPage.jsx`
  - `DiagnosticResultatPage.jsx`
- Routes frontend client disponibles :
  - `/client/login`
  - `/client/register`
  - `/client/diagnostic`
  - `/client/diagnostic/:diagnosticId/questions`
  - `/client/diagnostic/:diagnosticId/resultat`
- Navigation admin améliorée : groupes de sidebar collapsibles avec ouverture animée
- Gestion admin des clients ajoutée :
  - endpoint `GET /api/admin/users/clients`
  - endpoint `PATCH /api/admin/users/clients/{id}/status`
  - page frontend `/admin/clients`
  - activation/désactivation des comptes clients
- Sécurité client renforcée :
  - un client désactivé est rejeté par le filtre JWT sur les requêtes suivantes
  - le frontend client vérifie périodiquement `/api/client/auth/me` et déconnecte le client si son compte est désactivé
- Module 4 Recommandations démarré :
  - entités `RegleRecommandation` et `Recommandation`
  - repositories associés
  - `RecommandationService`
  - génération automatique des recommandations à la finalisation du diagnostic
  - seed de 5 règles métier de base
  - endpoints client :
    - `GET /api/client/diagnostics/{id}/recommandations`
    - `GET /api/client/diagnostics/{id}/plan-action`
  - endpoints admin règles :
    - `GET /api/admin/regles-recommandation`
    - `GET /api/admin/regles-recommandation/categories`
    - `POST /api/admin/regles-recommandation`
    - `PUT /api/admin/regles-recommandation/{id}`
    - `DELETE /api/admin/regles-recommandation/{id}`
  - page frontend client `/client/diagnostic/:diagnosticId/recommandations`
- Interface admin des règles de recommandation ajoutée :
  - page `/admin/regles-recommandation`
  - création/modification/suppression des règles
  - sélection catégorie diagnostic, service HWC et sous-service HWC
  - activation/désactivation des règles
- Vérifications réussies :
  - Backend : `./mvnw.cmd test` -> 31 tests OK
  - Frontend : `npm run build` OK

### À reprendre ensuite

1. Tester manuellement le parcours client complet avec backend + frontend lancés localement
2. Améliorer l'UX du questionnaire si nécessaire après test réel
3. Tester manuellement la configuration des règles de recommandation depuis l'admin
4. Améliorer l'affichage client des recommandations si nécessaire
5. Démarrer le Module 5 : Dashboard décisionnel client

### Notes de reprise

- Ne pas recréer `User`, `Role`, `UserRepository` ou `RoleRepository` : ils existaient déjà depuis la phase 1 et ont été étendus.
- Le fichier de référence initial indiquait des noms comme `password_hash`, mais le code existant utilise déjà le champ Java `password`.
- Le prochain travail doit partir de l'état réel du repo, pas seulement du schéma théorique du guide.
- Les commits de référence sur `feature/new-functionality` sont :
  - `98920de feat: add diagnostic backend flow`
  - `05680d4 feat: add client diagnostic frontend`

---

## 🏗️ Architecture technique (rappel)

```
Frontend React (hwc-frontend/)
    ↓ REST API + JWT
Backend Spring Boot (hwc-backend/)
    ↓               ↓
MySQL 8         OpenAI API (GPT-4o-mini)
```

### Nouvelles tables BDD à créer (Phase 2)

```sql
-- Utilisateurs clients
CREATE TABLE users (
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    email                   VARCHAR(150) UNIQUE NOT NULL,
    password_hash           VARCHAR(255) NOT NULL,
    nom                     VARCHAR(100),
    prenom                  VARCHAR(100),
    entreprise              VARCHAR(150),
    secteur                 VARCHAR(100),
    taille_entreprise       VARCHAR(50),
    telephone               VARCHAR(20),
    actif                   BOOLEAN DEFAULT TRUE,
    date_creation           DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_derniere_connexion DATETIME
);

-- Catégories du diagnostic (5 catégories fixes)
CREATE TABLE categories_diagnostic (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom         VARCHAR(100) NOT NULL,  -- ex: "Maturité digitale"
    description TEXT,
    icone       VARCHAR(50),
    poids       DECIMAL(5,2) DEFAULT 20.00,  -- % de pondération sur 100
    ordre       INT
);

-- Questions par catégorie
CREATE TABLE questions (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    categorie_id BIGINT NOT NULL,
    texte        TEXT NOT NULL,
    ordre        INT,
    actif        BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (categorie_id) REFERENCES categories_diagnostic(id)
);

-- Options de réponse (avec poids 0 à 5)
CREATE TABLE options_reponse (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    texte       VARCHAR(500) NOT NULL,
    poids       INT NOT NULL,  -- 0, 1, 2, 3, 4, ou 5 points
    ordre       INT,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Instances de diagnostic
CREATE TABLE diagnostics (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT NOT NULL,
    score_global     DECIMAL(5,2),
    niveau_maturite  VARCHAR(20),  -- CRITIQUE / FAIBLE / MOYEN / BON / EXCELLENT
    statut           ENUM('EN_COURS', 'TERMINE', 'ABANDONNE') DEFAULT 'EN_COURS',
    date_debut       DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_fin         DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Réponses aux questions
CREATE TABLE reponses_diagnostic (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    diagnostic_id     BIGINT NOT NULL,
    question_id       BIGINT NOT NULL,
    option_reponse_id BIGINT NOT NULL,
    FOREIGN KEY (diagnostic_id)     REFERENCES diagnostics(id),
    FOREIGN KEY (question_id)       REFERENCES questions(id),
    FOREIGN KEY (option_reponse_id) REFERENCES options_reponse(id)
);

-- Scores calculés par catégorie
CREATE TABLE scores (
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    diagnostic_id        BIGINT NOT NULL,
    categorie_id         BIGINT NOT NULL,
    score                DECIMAL(5,2),  -- /100
    points_obtenus       INT,
    points_max           INT,
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostics(id),
    FOREIGN KEY (categorie_id)  REFERENCES categories_diagnostic(id)
);

-- Règles métier pour les recommandations (configurables par admin)
CREATE TABLE regles_recommandation (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    categorie_id        BIGINT NOT NULL,
    seuil_score         DECIMAL(5,2),   -- ex: 50.0
    operateur           VARCHAR(5),     -- '<', '<=', '>', '>='
    service_hwc_id      BIGINT,         -- FK vers services existants
    sous_service_hwc_id BIGINT,         -- FK vers sous_services existants
    titre_recommandation VARCHAR(255),
    description_recommandation TEXT,
    horizon             VARCHAR(20),   -- COURT_TERME / MOYEN_TERME / LONG_TERME
    impact_estime       VARCHAR(100),
    kpis_suggeres       TEXT,           -- JSON array of KPI strings
    priorite            INT,
    FOREIGN KEY (categorie_id) REFERENCES categories_diagnostic(id)
);

-- Recommandations générées
CREATE TABLE recommandations (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    diagnostic_id       BIGINT NOT NULL,
    regle_id            BIGINT,
    titre               VARCHAR(255),
    description         TEXT,
    horizon             VARCHAR(20),
    service_hwc_id      BIGINT,
    sous_service_hwc_id BIGINT,
    impact_estime       VARCHAR(100),
    kpis                TEXT,
    priorite            INT,
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostics(id)
);

-- Rapports PDF générés par l'IA
CREATE TABLE rapports_pdf (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    diagnostic_id   BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    pdf_url         VARCHAR(500),
    introduction    TEXT,         -- section rédigée par GPT
    analyse_forts   TEXT,         -- section rédigée par GPT
    analyse_faibles TEXT,         -- section rédigée par GPT
    plan_action     TEXT,         -- section rédigée par GPT
    conclusion      TEXT,         -- section rédigée par GPT
    tokens_used     INT,
    date_generation DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostics(id),
    FOREIGN KEY (user_id)       REFERENCES users(id)
);

-- Conversations chatbot
CREATE TABLE chat_conversations (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    diagnostic_id   BIGINT,
    titre           VARCHAR(255),
    date_creation   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)       REFERENCES users(id),
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostics(id)
);

-- Messages chatbot
CREATE TABLE chat_messages (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    role            VARCHAR(20),   -- 'user' ou 'assistant'
    contenu         TEXT,
    tokens_used     INT,
    date_envoi      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES chat_conversations(id)
);

-- Objectifs hebdomadaires du Coach IA
CREATE TABLE coach_objectifs_hebdo (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    diagnostic_id   BIGINT NOT NULL,
    semaine_numero  INT,
    date_debut      DATE,
    date_fin        DATE,
    objectifs_json  TEXT,         -- JSON array des objectifs
    statut          VARCHAR(20),  -- ACTIF / TERMINE
    date_creation   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)       REFERENCES users(id),
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostics(id)
);

-- Résultats des objectifs hebdomadaires
CREATE TABLE coach_objectif_resultat (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    objectif_hebdo_id BIGINT NOT NULL,
    objectif_numero   INT,
    objectif_titre    VARCHAR(255),
    statut            VARCHAR(20),  -- A_FAIRE / EN_COURS / FAIT / NON_FAIT
    date_validation   DATETIME,
    FOREIGN KEY (objectif_hebdo_id) REFERENCES coach_objectifs_hebdo(id)
);

-- Emails envoyés par le Coach IA
CREATE TABLE coach_emails_envoyes (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    type_email  VARCHAR(50),  -- LUNDI_OBJECTIFS / VENDREDI_BILAN
    sujet       VARCHAR(255),
    contenu     TEXT,
    statut      VARCHAR(20),  -- ENVOYE / ECHEC
    date_envoi  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 📊 Données de référence à insérer (seed data)

### 5 catégories de diagnostic

```sql
INSERT INTO categories_diagnostic (nom, description, icone, poids, ordre) VALUES
('Maturité digitale',         'Présence en ligne, outils, process digitaux',                '💻', 20, 1),
('Performance commerciale',   'Acquisition, conversion, fidélisation clients',              '📈', 25, 2),
('Leadership & Management',   'Style de management, communication, vision dirigeants',      '👥', 15, 3),
('Performance organisationnelle', 'Structure interne, processus, agilité opérationnelle',  '⚙️', 15, 4),
('Marketing & Communication', 'Positionnement de marque, branding, contenus publiés',      '📣', 25, 5);
```

### Exemple de règles métier

```sql
INSERT INTO regles_recommandation (categorie_id, seuil_score, operateur, titre_recommandation, horizon, priorite) VALUES
(5, 50, '<', 'Développer votre stratégie Marketing Digital', 'COURT_TERME', 1),
(3, 50, '<', 'Renforcer votre leadership managérial',        'MOYEN_TERME', 2),
(1, 60, '<', 'Moderniser votre maturité digitale',           'LONG_TERME',  3),
(2, 65, '<', 'Optimiser votre performance commerciale',       'MOYEN_TERME', 2),
(4, 60, '<', 'Restructurer votre organisation interne',       'LONG_TERME',  3);
```

---

## 🔐 Module 3 — Authentification Client (prérequis) ✅ Terminé côté backend

> ⚠️ Avant de construire le diagnostic, l'utilisateur client doit pouvoir s'inscrire et se connecter. L'admin existe déjà. On ajoute ici les clients.

### Backend Spring Boot

**État réel :**

- `User`, `Role`, `UserRepository` et `RoleRepository` existaient déjà depuis la phase 1
- `User` a été étendu avec les champs client nécessaires
- `ROLE_CLIENT` a été ajouté
- `ClientAuthController` et `RegisterClientDTO` ont été créés

**Classes prévues dans le guide initial :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   └── User.java              # Entité utilisateur client
├── repository/
│   └── UserRepository.java
├── dto/
│   ├── RegisterClientDTO.java  # email, password, nom, prenom, entreprise, secteur
│   ├── LoginRequestDTO.java    # email, password
│   └── AuthResponseDTO.java    # accessToken, refreshToken, user info
├── service/
│   ├── ClientAuthService.java
│   └── ClientAuthServiceImpl.java
└── controller/
    └── ClientAuthController.java  # /api/client/auth/register + /login + /me
```

**Endpoints :**

- `POST /api/client/auth/register` → inscription client
- `POST /api/client/auth/login` → connexion client
- `GET /api/client/auth/me` → profil du client connecté

**Configuration SecurityConfig :** ajouter ces routes comme publiques dans la config existante.

### Frontend React

**Nouvelles pages à créer dans `src/pages/client/` :**

- `ClientRegisterPage.jsx` — formulaire d'inscription
- `ClientLoginPage.jsx` — formulaire de connexion
- `ClientProtectedRoute.jsx` — route protégée pour les clients

**Store Zustand `src/store/clientAuthStore.js` :**

```js
// State: clientUser, clientToken, isClientAuthenticated
// Actions: clientLogin(), clientLogout(), loadClientFromStorage()
```

---

## 🧠 Module 3 — Plateforme de Diagnostic Intelligent

### Scénario réel (référence métier)

> Karim Bennani, dirigeant de BennaniTextile (PME 40 employés), arrive sur le site HWC et lance un diagnostic.
> Il répond à 25 questions réparties sur 5 catégories.
> Ses résultats : Marketing 35/100 | Leadership 45/100 | Maturité digitale 52/100 | Performance org. 60/100 | Performance commerciale 70/100
> Score global = (35 + 45 + 52 + 60 + 70) / 5 = **52.4/100 → Niveau MOYEN**

### Algorithme de scoring

```
Pour chaque catégorie :
  points_obtenus = somme des poids des options choisies
  points_max     = nombre de questions × 5
  score_categorie = (points_obtenus / points_max) × 100

Score global = moyenne des scores de toutes les catégories
OU
Score global pondéré = Σ (score_categorie × poids_categorie / 100)

Niveaux de maturité :
  0  – 30  → 🔴 CRITIQUE (urgent)
  31 – 50  → 🟠 FAIBLE (à améliorer)
  51 – 70  → 🟡 MOYEN (peut mieux faire)
  71 – 85  → 🟢 BON (optimisable)
  86 – 100 → ⭐ EXCELLENT (maintien)
```

### Backend Spring Boot

**État réel au 16 mai 2026 : fondations backend terminées**

- Créé :
  - `CategorieDiagnostic`
  - `Question`
  - `OptionReponse`
  - `Diagnostic`
  - `DiagnosticStatut`
  - `ReponseDiagnostic`
  - `Score`
- Créés aussi :
  - `CategorieDiagnosticRepository`
  - `QuestionRepository`
  - `OptionReponseRepository`
  - `DiagnosticRepository`
  - `ReponseDiagnosticRepository`
  - `ScoreRepository`
- Seed automatique des 5 catégories ajouté dans `DataInitializer`
- Encore à faire :
  - DTOs
  - `DiagnosticService`
  - `ScoringService`
  - `DiagnosticController`
  - questions/options de référence
  - tests fonctionnels du diagnostic

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   ├── CategorieDiagnostic.java
│   ├── Question.java
│   ├── OptionReponse.java
│   ├── Diagnostic.java
│   ├── ReponseDiagnostic.java
│   └── Score.java
├── repository/
│   ├── CategorieDiagnosticRepository.java
│   ├── QuestionRepository.java
│   ├── OptionReponseRepository.java
│   ├── DiagnosticRepository.java
│   ├── ReponseDiagnosticRepository.java
│   └── ScoreRepository.java
├── dto/
│   ├── CategorieDiagnosticDTO.java
│   ├── QuestionDTO.java
│   ├── OptionReponseDTO.java
│   ├── DiagnosticStartDTO.java     # réponse au démarrage
│   ├── RepondreQuestionDTO.java    # payload : questionId + optionReponseId
│   ├── DiagnosticResultatDTO.java  # scores + niveau + statut
│   └── ScoreDTO.java               # categorieNom + score + niveau
├── service/
│   ├── DiagnosticService.java
│   ├── DiagnosticServiceImpl.java
│   └── ScoringService.java         # logique de calcul des scores
└── controller/
    └── DiagnosticController.java   # /api/client/diagnostics
```

**Endpoints à créer :**

```java
// DiagnosticController.java — tous protégés par JWT client

POST   /api/client/diagnostics/start
// Crée un nouveau diagnostic statut=EN_COURS, retourne {diagnosticId}

GET    /api/client/diagnostics/questions
// Retourne toutes les questions groupées par catégorie avec leurs options

POST   /api/client/diagnostics/{id}/reponses
// Body: { questionId, optionReponseId }
// Enregistre une réponse, retourne la progression

POST   /api/client/diagnostics/{id}/finalize
// 1. Calcule les scores par catégorie via ScoringService
// 2. Calcule le score global
// 3. Détermine le niveau de maturité
// 4. Lance le moteur de recommandations
// 5. Change statut → TERMINE
// Retourne DiagnosticResultatDTO

GET    /api/client/diagnostics/history
// Historique des diagnostics du client connecté

GET    /api/client/diagnostics/{id}
// Détail d'un diagnostic (scores + recommandations)
```

**ScoringService.java :**

```java
// Méthodes principales :
public List<Score> calculerScores(Long diagnosticId)
public double calculerScoreGlobal(List<Score> scores)
public String determinerNiveau(double scoreGlobal)
// Niveau : CRITIQUE / FAIBLE / MOYEN / BON / EXCELLENT
```

### Frontend React

**Nouvelles pages et composants à créer :**

```
hwc-frontend/src/
├── pages/client/
│   ├── DiagnosticStartPage.jsx    # Page d'accueil du diagnostic
│   ├── DiagnosticQuestionsPage.jsx # Questionnaire avec progression
│   └── DiagnosticResultatPage.jsx  # Résultats après finalisation
└── components/diagnostic/
    ├── ProgressBar.jsx             # Barre de progression (ex: 12/25)
    ├── CategoryProgress.jsx        # Indicateur par catégorie
    ├── QuestionCard.jsx            # Affiche une question + 4 options
    └── ScoreCard.jsx               # Carte résultat par catégorie
```

**Flow utilisateur complet :**

```
1. Client clique "Démarrer mon diagnostic gratuit"
   → DiagnosticStartPage (présentation + bouton commencer)

2. POST /api/client/diagnostics/start
   → Obtient diagnosticId

3. GET /api/client/diagnostics/questions
   → Charge toutes les questions

4. DiagnosticQuestionsPage : Karim répond
   → Affiche les questions catégorie par catégorie
   → Barre de progression visible en permanence
   → POST réponse à chaque question
   → Sauvegarde automatique (si fermeture → peut reprendre)

5. Dernière question → bouton "Finaliser"
   → POST /api/client/diagnostics/{id}/finalize

6. Redirection vers DiagnosticResultatPage
   → Affiche scores + niveau + message de niveau

7. Bouton "Voir mes recommandations" → Module 4
8. Bouton "Voir mon dashboard" → Module 5
```

**DiagnosticQuestionsPage.jsx — Logique d'affichage :**

```jsx
// Questions par catégorie (5 catégories × 5 questions = 25)
// Afficher une question à la fois OU toutes par catégorie
// Progress : "Question 8 / 25" + "Catégorie 2/5 : Performance commerciale"
// Options : 4 boutons radio cliquables (A, B, C, D)
// Auto-save : envoyer POST dès qu'une option est sélectionnée
// Navigation : Précédent / Suivant + "Financer" sur la dernière question
```

---

## 🤖 Module 4 — Moteur de Recommandations

### Scénario réel (référence métier)

> Après le diagnostic de Karim :
>
> - Marketing 35/100 < 50 → RÈGLE 1 activée → Service "Marketing Digital" + SEO & SEA
> - Leadership 45/100 < 50 → RÈGLE 2 activée → Service "Leadership" + Coaching dirigeants
> - Maturité digitale 52/100 < 60 → RÈGLE 3 activée → Audit & Stratégie + Sites web
>
> Plan d'action généré :
>
> - 🔴 COURT TERME (1 mois) : SEO, réseaux sociaux, Google Ads
> - 🟡 MOYEN TERME (3 mois) : Coaching dirigeant, formation management
> - 🟢 LONG TERME (6+ mois) : Refonte site web, CRM, automatisation
>
> Impact estimé : +23 points en 6 mois (52 → 75/100)

### Backend Spring Boot

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   ├── RegleRecommandation.java
│   └── Recommandation.java
├── repository/
│   ├── RegleRecommandationRepository.java
│   └── RecommandationRepository.java
├── dto/
│   ├── RecommandationDTO.java
│   │   # titre, description, horizon, serviceHwcNom, souServiceHwcNom,
│   │   # impactEstime, kpis[], priorite
│   └── PlanActionDTO.java
│       # courtTerme[], moyenTerme[], longTerme[], impactTotal
├── service/
│   ├── RecommandationService.java
│   └── RecommandationServiceImpl.java
└── controller/
    └── RecommandationController.java
```

**RecommandationServiceImpl.java — Logique principale :**

```java
public List<Recommandation> genererRecommandations(Long diagnosticId) {
    // 1. Charger les scores du diagnostic
    List<Score> scores = scoreRepository.findByDiagnosticId(diagnosticId);

    // 2. Charger toutes les règles actives
    List<RegleRecommandation> regles = regleRepo.findAllActives();

    // 3. Pour chaque règle, vérifier si le score respecte la condition
    List<Recommandation> recommandations = new ArrayList<>();
    for (RegleRecommandation regle : regles) {
        Score scoreCategorie = trouverScoreParCategorie(scores, regle.getCategorieId());
        if (respecteCondition(scoreCategorie.getScore(), regle.getSeuil(), regle.getOperateur())) {
            Recommandation reco = creerRecommandation(diagnosticId, regle);
            recommandations.add(reco);
        }
    }

    // 4. Trier par priorité
    recommandations.sort(Comparator.comparing(Recommandation::getPriorite));

    // 5. Sauvegarder et retourner
    return recommandationRepository.saveAll(recommandations);
}
```

**Endpoints :**

```java
GET /api/client/diagnostics/{id}/recommandations
// Retourne la liste des recommandations groupées par horizon

GET /api/client/diagnostics/{id}/plan-action
// Retourne PlanActionDTO : {courtTerme[], moyenTerme[], longTerme[], impactTotal}

// Admin : configurer les règles
GET    /api/admin/regles-recommandation
POST   /api/admin/regles-recommandation
PUT    /api/admin/regles-recommandation/{id}
DELETE /api/admin/regles-recommandation/{id}
```

### Frontend React

**Composants à créer :**

```
hwc-frontend/src/
├── pages/client/
│   └── RecommandationsPage.jsx    # Page des recommandations
└── components/recommandations/
    ├── PlanActionTimeline.jsx     # Affiche court/moyen/long terme
    ├── RecommandationCard.jsx     # Une carte de recommandation
    ├── ServiceHwcCard.jsx         # Carte service HWC suggéré
    └── ImpactEstime.jsx           # Affiche l'impact projeté
```

**RecommandationsPage.jsx — Contenu à afficher :**

```jsx
// 1. Résumé des axes prioritaires identifiés
// Ex: "⚠️ 3 axes prioritaires identifiés"

// 2. Plan d'action avec 3 horizons temporels
// 🔴 COURT TERME (1 mois) : liste des actions
// 🟡 MOYEN TERME (3 mois) : liste des actions
// 🟢 LONG TERME (6+ mois) : liste des actions

// 3. Services HWC suggérés (cards cliquables vers le site vitrine)

// 4. Impact estimé
// "Score actuel : 52/100 → Score projeté : 75/100 (+23 pts en 6 mois)"

// 5. KPIs à suivre par catégorie
// Pour Marketing : visiteurs/mois, leads générés, ROI campagnes...

// 6. Boutons :
// [📥 Télécharger rapport PDF] → Module 6
// [📊 Voir mon dashboard] → Module 5
```

---

## 📊 Module 5 — Dashboard Décisionnel (BI)

### Scénario réel (référence métier)

> Karim se connecte et voit :
>
> - Score global en donut chart : 52/100 🟡 MOYEN
> - 4 KPI cards : 2 axes critiques | 5 catégories | 9 recommandations | +23 pts à gagner
> - Radar chart : Marketing(35) / Leadership(45) / Maturité(52) / Perf.Org(60) / Perf.Com(70)
> - Barre de progression si plusieurs diagnostics
> - Plan d'action en timeline
> - Alertes : ⚠️ Marketing critique (35/100) ⚠️ Leadership faible (45/100)
> - Services HWC recommandés
> - Comparaison secteur : Karim 52 | Moyenne secteur 58 | Top 10% : 78

### Backend Spring Boot

**Endpoint principal :**

```java
GET /api/client/dashboard
// Retourne DashboardDTO contenant :
// {
//   scoreGlobal, niveauMaturite,
//   scores: [{ categorieNom, score, niveau, message }],
//   kpis: { axesCritiques, nbCategories, nbRecommandations, pointsAGagner },
//   historiqueScores: [{ date, scoreGlobal }],  // si plusieurs diagnostics
//   recommandations: [...],
//   alertes: [{ categorie, score, niveau }],
//   servicesRecommandes: [...],
//   benchmarkSecteur: { moyenneSecteur, top10Pourcent }
// }
```

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── dto/
│   └── DashboardDTO.java         # DTO agrégé pour le dashboard complet
└── controller/
    └── DashboardClientController.java
```

### Frontend React — Bibliothèques

```bash
# Installer dans hwc-frontend/
npm install recharts
```

### Frontend React — Structure

```
hwc-frontend/src/
├── pages/client/
│   └── DashboardClientPage.jsx   # Page principale du dashboard BI
└── components/dashboard/
    ├── ScoreGlobalDonut.jsx       # Donut chart score global
    ├── KpiCards.jsx               # 4 cartes KPI
    ├── RadarChart.jsx             # Radar des 5 catégories
    ├── HistoriqueBarChart.jsx     # Évolution dans le temps
    ├── CategoryDetailCards.jsx    # Cartes par catégorie avec barre
    ├── PlanActionTimeline.jsx     # Timeline court/moyen/long terme
    ├── AlertesCritiques.jsx       # Bannière alertes
    ├── ServicesRecommandes.jsx    # Cards des services HWC
    └── BenchmarkSecteur.jsx      # Comparaison secteur
```

### Layout du Dashboard (10 sections)

```jsx
// DashboardClientPage.jsx — Structure complète
<div className="min-h-screen bg-gray-50">
  {/* HEADER : Bienvenue + actions rapides */}
  <DashboardHeader
    nom={user.prenom}
    dateLastDiag={lastDiagDate}
    onDownloadPDF={handleDownloadPDF}
    onRestartDiag={handleNewDiag}
  />

  {/* SECTION 1 : Score global + Alertes */}
  <div className="grid grid-cols-2 gap-6">
    <ScoreGlobalDonut score={52} niveau="MOYEN" />
    <AlertesCritiques alertes={alertes} />
  </div>

  {/* SECTION 2 : 4 KPI Cards */}
  <KpiCards
    axesCritiques={2}
    nbCategories={5}
    nbRecommandations={9}
    pointsAGagner={23}
  />

  {/* SECTION 3 : Radar Chart + Bar Chart évolution */}
  <div className="grid grid-cols-2 gap-6">
    <RadarChartScores scores={scores} />
    <HistoriqueBarChart historique={historique} />
  </div>

  {/* SECTION 4 : Détail par catégorie */}
  <CategoryDetailCards scores={scores} />

  {/* SECTION 5 : Plan d'action timeline */}
  <PlanActionTimeline recommandations={recommandations} />

  {/* SECTION 6 : Services HWC recommandés */}
  <ServicesRecommandes services={servicesRecommandes} />

  {/* SECTION 7 : Benchmark secteur */}
  <BenchmarkSecteur scoreClient={52} moyenneSecteur={58} top10={78} />
</div>
```

**ScoreGlobalDonut.jsx — Recharts :**

```jsx
import { PieChart, Pie, Cell } from "recharts";

// Donut chart avec couleur selon le niveau :
// CRITIQUE → #EF4444 (rouge)
// FAIBLE   → #F97316 (orange)
// MOYEN    → #EAB308 (jaune)
// BON      → #22C55E (vert)
// EXCELLENT → #2E7D6B (vert HWC)
```

**RadarChart.jsx — Recharts :**

```jsx
import { RadarChart, Radar, PolarGrid, PolarAngleAxis } from "recharts";

// 5 axes : Maturité digitale, Perf. commerciale,
//          Leadership, Perf. org., Marketing
// 2 series : Score actuel + Moyenne secteur (pointillés)
```

---

## 📄 Module 6 — Génération de Rapports PDF par IA

### Scénario réel (référence métier)

> Karim clique "Télécharger rapport PDF"
> → L'IA rédige 5 sections personnalisées pour BennaniTextile :
>
> 1. Introduction avec contexte de l'entreprise
> 2. Analyse des points forts (score 70/100 en performance commerciale)
> 3. Analyse des axes faibles (Marketing 35, Leadership 45)
> 4. Plan d'action concret avec actions spécifiques
> 5. Conclusion motivante avec projection +23 pts en 6 mois
>    → Assemblage avec template HWC + graphiques
>    → PDF téléchargé automatiquement (~10 secondes)

### Backend Spring Boot

**Dépendance à ajouter dans pom.xml :**

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
    <type>pom</type>
</dependency>
<!-- OU OpenPDF (plus simple, gratuit) -->
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version>
</dependency>
```

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   └── RapportPdf.java
├── repository/
│   └── RapportPdfRepository.java
├── dto/
│   └── RapportPdfDTO.java
├── service/
│   ├── OpenAIService.java        # Service d'appel à l'API OpenAI
│   ├── RapportService.java
│   └── RapportServiceImpl.java
└── controller/
    └── RapportController.java
```

**OpenAIService.java — Configuration :**

```java
@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    // Modèle à utiliser : gpt-4o-mini (économique ~$0.01/rapport)
    private final String model = "gpt-4o-mini";

    public String generateContent(String systemPrompt, String userPrompt) {
        // Appel REST à https://api.openai.com/v1/chat/completions
        // Header: Authorization: Bearer {apiKey}
        // Body: { model, messages: [{role:"system", content:systemPrompt},
        //                           {role:"user", content:userPrompt}],
        //         max_tokens: 500, temperature: 0.7 }
    }
}
```

**application.properties — Ajouter :**

```properties
openai.api.key=${OPENAI_API_KEY}
openai.model=gpt-4o-mini
openai.max-tokens=600
openai.temperature=0.7
```

**RapportServiceImpl.java — Génération en 5 prompts :**

```java
public byte[] genererRapportPDF(Long diagnosticId) {

    // 1. Charger les données du diagnostic
    Diagnostic diag = ...;
    User user = diag.getUser();
    List<Score> scores = ...;
    List<Recommandation> recos = ...;

    String contexte = String.format(
        "Client: %s %s, Entreprise: %s, Secteur: %s. " +
        "Score global: %.1f/100 (Niveau: %s). " +
        "Scores: Marketing %.0f, Leadership %.0f, Maturité digitale %.0f, " +
        "Performance commerciale %.0f, Performance organisationnelle %.0f.",
        user.getPrenom(), user.getNom(), user.getEntreprise(),
        user.getSecteur(), diag.getScoreGlobal(), diag.getNiveauMaturite(),
        scoreMarketing, scoreLeadership, scoreMaturite, scorePerf, scorePerfOrg
    );

    // Prompt système commun
    String systemPrompt = "Tu es un consultant senior de Harmony Works Consulting (HWC). " +
        "Tu rédiges une section d'un rapport de diagnostic professionnel. " +
        "Sois précis, bienveillant et orienté solutions. Réponds en français. " +
        "Maximum 250 mots par section.";

    // 5 sections rédigées par l'IA
    String intro    = openAI.generate(systemPrompt,
        "Rédige l'introduction du rapport pour ce contexte: " + contexte);
    String forts    = openAI.generate(systemPrompt,
        "Analyse les points forts de l'entreprise basés sur: " + contexte);
    String faibles  = openAI.generate(systemPrompt,
        "Analyse les axes d'amélioration prioritaires basés sur: " + contexte);
    String planAct  = openAI.generate(systemPrompt,
        "Rédige un plan d'action concret et opérationnel basé sur: " + contexte);
    String conclusion = openAI.generate(systemPrompt,
        "Rédige une conclusion motivante avec les objectifs à 6 mois pour: " + contexte);

    // Sauvegarder en BDD
    RapportPdf rapport = new RapportPdf();
    rapport.setDiagnostic(diag);
    rapport.setUser(user);
    rapport.setIntroduction(intro);
    rapport.setAnalyseForts(forts);
    rapport.setAnalyseFaibles(faibles);
    rapport.setPlanAction(planAct);
    rapport.setConclusion(conclusion);
    rapportPdfRepository.save(rapport);

    // Générer le PDF avec le template HWC
    return genererPDFBytes(rapport, scores, recos);
}
```

**Contenu du PDF (7 pages) :**

```
Page 1 : Couverture (Logo HWC + Nom client + Date + "RAPPORT DE DIAGNOSTIC")
Page 2 : Introduction (rédigée par IA)
Page 3 : Score global + Radar chart (graphique)
Page 4 : Analyse des forces et faiblesses (rédigée par IA)
Page 5 : Plan d'action détaillé (rédigée par IA)
Page 6 : Services HWC recommandés
Page 7 : Conclusion + Contact HWC
```

**Endpoints :**

```java
GET /api/client/diagnostics/{id}/pdf
// Génère le rapport (appelle l'IA), retourne le fichier binaire PDF
// Content-Type: application/pdf
// Content-Disposition: attachment; filename="rapport-hwc-{date}.pdf"

GET /api/client/rapports
// Historique des rapports générés pour le client connecté
```

### Frontend React

```jsx
// Bouton de téléchargement
const handleDownloadPDF = async () => {
  setLoading(true);
  const response = await fetch(`/api/client/diagnostics/${diagId}/pdf`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  const blob = await response.blob();
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `rapport-hwc-${new Date().toLocaleDateString()}.pdf`;
  a.click();
  setLoading(false);
};

// Afficher un loader pendant la génération (~10 secondes)
{
  loading && (
    <div>
      <Spinner />
      <p>Génération de votre rapport personnalisé en cours...</p>
      <p className="text-sm text-gray-500">
        L'IA rédige votre rapport, veuillez patienter ~10 secondes
      </p>
    </div>
  );
}
```

---

## 💬 Module 7 — Assistant IA (Chatbot RAG + Coach IA)

### 7A. Chatbot RAG

#### Scénario réel (référence métier)

> Karim : "Pourquoi mon score marketing est-il si faible ?"
>
> → RETRIEVAL : Système charge diagnostic + scores + réponses aux questions marketing + services HWC
> → CONTEXT : { client: "Karim - BennaniTextile", score_marketing: 35, reponses: [...], services_hwc: [...] }
> → GPT-4 génère : "Karim, votre score marketing de 35/100 est faible principalement car vous n'avez pas de stratégie SEO, votre présence réseaux sociaux est limitée et aucune campagne publicitaire n'est en cours. Je vous recommande de commencer par notre service 'SEO & SEA'..."

#### Backend Spring Boot

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   ├── ChatConversation.java
│   └── ChatMessage.java
├── repository/
│   ├── ChatConversationRepository.java
│   └── ChatMessageRepository.java
├── dto/
│   ├── ChatMessageRequestDTO.java  # conversationId (nullable), message
│   └── ChatMessageResponseDTO.java # role, content, conversationId
├── service/
│   ├── ChatService.java
│   └── ChatServiceImpl.java       # orchestre RAG + OpenAI
└── controller/
    └── ChatController.java
```

**ChatServiceImpl.java — Logique RAG :**

```java
public ChatMessageResponseDTO envoyerMessage(Long userId, ChatMessageRequestDTO request) {

    // 1. RETRIEVAL — Construire le contexte depuis la BDD
    User user = userRepository.findById(userId).orElseThrow();
    Diagnostic lastDiag = diagnosticRepository.findLatestByUserId(userId);

    String contexteClient = "";
    if (lastDiag != null) {
        List<Score> scores = scoreRepository.findByDiagnosticId(lastDiag.getId());
        List<Recommandation> recos = recommandationRepository.findByDiagnosticId(lastDiag.getId());

        contexteClient = String.format(
            "Informations du client:\n" +
            "- Nom: %s %s, Entreprise: %s (%s)\n" +
            "- Diagnostic du %s, Score global: %.1f/100 (%s)\n" +
            "- Scores par catégorie: Marketing=%.0f, Leadership=%.0f, " +
            "  Maturité digitale=%.0f, Perf. commerciale=%.0f, Perf. org.=%.0f\n" +
            "- Axes prioritaires: %s\n" +
            "- Services HWC recommandés: %s\n",
            user.getPrenom(), user.getNom(), user.getEntreprise(), user.getSecteur(),
            lastDiag.getDateFin(), lastDiag.getScoreGlobal(), lastDiag.getNiveauMaturite(),
            // ... scores par catégorie
            // ... services recommandés
        );
    }

    // 2. CONTEXT BUILDING — Prompt système
    String systemPrompt =
        "Tu es un consultant expert de Harmony Works Consulting (HWC). " +
        "Tu aides les dirigeants d'entreprise à comprendre leur diagnostic de maturité " +
        "et leurs recommandations stratégiques. " +
        "Réponds en français, de manière professionnelle, bienveillante et précise. " +
        "Base tes réponses UNIQUEMENT sur les données fournies. " +
        "Si une question dépasse les données disponibles, oriente vers un consultant HWC.\n\n" +
        "CONTEXTE DU CLIENT:\n" + contexteClient;

    // 3. Charger l'historique de la conversation (pour la mémoire)
    List<ChatMessage> historique = chatMessageRepository
        .findByConversationIdOrderByDateEnvoiAsc(request.getConversationId());

    // 4. GÉNÉRATION — Appel OpenAI avec historique
    String reponse = openAIService.generateWithHistory(systemPrompt, historique, request.getMessage());

    // 5. Sauvegarder les messages
    // ... sauvegarder message user + message assistant

    return new ChatMessageResponseDTO(conversationId, "assistant", reponse);
}
```

**Endpoints :**

```java
POST /api/client/chat/message
// Body: { conversationId (nullable pour nouvelle conv), message }
// Retourne: { conversationId, role: "assistant", content }

GET /api/client/chat/conversations
// Liste des conversations du client

GET /api/client/chat/conversations/{id}/messages
// Historique d'une conversation
```

#### Frontend React — Interface Chatbot

```
hwc-frontend/src/
└── components/chat/
    ├── ChatbotButton.jsx         # Bouton flottant en bas à droite
    ├── ChatbotWindow.jsx         # Fenêtre de chat
    ├── ChatMessage.jsx           # Bulle de message (user / assistant)
    ├── ChatInput.jsx             # Input + bouton envoyer
    └── ChatSuggestions.jsx       # 3 suggestions de questions au démarrage
```

**ChatbotWindow.jsx — UI :**

```jsx
// Fenêtre flottante (position: fixed, bottom-right)
// Largeur 380px, hauteur 500px
// Header : "🤖 Assistant HWC" + bouton X
// Corps : messages scrollables (user=droite en vert, assistant=gauche en blanc)
// Footer : input + bouton envoyer

// Suggestions initiales (affichées si conversation vide) :
const suggestions = [
  "💡 Pourquoi mon score marketing est-il faible ?",
  "🎯 Quels sont mes axes prioritaires ?",
  "📊 Quel service HWC me recommandez-vous en priorité ?",
];
```

---

### 7B. Coach IA Hebdomadaire

#### Scénario réel (référence métier)

> LUNDI 8h00 : Email automatique envoyé à Karim
> "Bonjour Karim ! Vos objectifs de la semaine :
> ✓ 1. Lancer votre première campagne Google Ads (Budget: 500 DH)
> ✓ 2. Publier 3 posts LinkedIn (sujets suggérés: ...)
> ✓ 3. Mettre à jour fiche Google My Business
> 📊 Semaine 7/12 | Score actuel: 65/100 (+13 depuis le début)"
>
> VENDREDI 17h00 : Email de bilan
> "✅ Campagne lancée | ⚠️ Posts LinkedIn: 1/3 | ❌ GMB non mis à jour
> Score: 65 → 67 (+2 pts) | Marketing: 35 → 42 (+7 pts)
> 💡 Insight IA: Votre campagne a généré 3 leads cette semaine !"

#### Backend Spring Boot

**Fichiers à créer :**

```
hwc-backend/src/main/java/hwc_backend/
├── entity/
│   ├── CoachObjectifsHebdo.java
│   ├── CoachObjectifResultat.java
│   └── CoachEmailEnvoye.java
├── repository/
│   ├── CoachObjectifsHebdoRepository.java
│   ├── CoachObjectifResultatRepository.java
│   └── CoachEmailEnvoyeRepository.java
├── service/
│   ├── CoachIAService.java
│   └── CoachIAServiceImpl.java
└── scheduler/
    └── CoachIAScheduler.java       # Spring @Scheduled
```

**CoachIAScheduler.java :**

```java
@Component
public class CoachIAScheduler {

    @Autowired
    private CoachIAService coachService;

    // Lundi à 8h00 — Email d'objectifs
    @Scheduled(cron = "0 0 8 * * MON")
    public void envoyerEmailsObjectifsLundi() {
        coachService.genererEtEnvoyerObjectifsSemaine();
    }

    // Vendredi à 17h00 — Email de bilan
    @Scheduled(cron = "0 0 17 * * FRI")
    public void envoyerEmailsBilanVendredi() {
        coachService.genererEtEnvoyerBilanSemaine();
    }
}
```

**CoachIAServiceImpl.java :**

```java
public void genererEtEnvoyerObjectifsSemaine() {
    // 1. Récupérer tous les clients avec diagnostic terminé
    List<User> clientsActifs = userRepository.findAllWithDiagnosticTermine();

    for (User client : clientsActifs) {
        // 2. Charger le contexte du client
        Diagnostic lastDiag = ...;
        List<Score> scores = ...;
        List<Recommandation> recos = ...;

        // 3. Générer les objectifs via GPT-4
        String prompt = String.format(
            "Tu es le coach de %s %s (entreprise: %s). " +
            "Son score actuel est %.1f/100. " +
            "Ses axes prioritaires sont: %s. " +
            "C'est la semaine %d de son programme. " +
            "Génère 3 objectifs concrets, réalisables en 1 semaine, " +
            "avec des actions précises et mesurables. " +
            "Format: JSON array de {titre, description, action_concrete}",
            client.getPrenom(), client.getNom(), client.getEntreprise(),
            lastDiag.getScoreGlobal(), axesPrioritaires, numeroSemaine
        );

        String objectifsJson = openAIService.generate(COACH_SYSTEM_PROMPT, prompt);

        // 4. Sauvegarder les objectifs
        CoachObjectifsHebdo objectifs = new CoachObjectifsHebdo();
        objectifs.setUser(client);
        objectifs.setObjectifsJson(objectifsJson);
        objectifs.setDateDebut(LocalDate.now());
        objectifs.setDateFin(LocalDate.now().plusDays(4));
        objectifs.setStatut("ACTIF");
        coachObjectifsRepo.save(objectifs);

        // 5. Envoyer l'email
        String emailContent = construireEmailLundi(client, objectifs, lastDiag);
        emailService.sendCoachEmail(client.getEmail(), "🎯 Vos objectifs de la semaine", emailContent);
    }
}
```

**Endpoints :**

```java
// Pour le client — consulter ses objectifs de la semaine
GET /api/client/coach/current-week
// Retourne les objectifs de la semaine courante + statut de chaque objectif

// Marquer un objectif comme fait
PATCH /api/client/coach/objectifs/{id}/complete

// Historique des semaines passées
GET /api/client/coach/history
```

#### Frontend React — Interface Coach IA

```
hwc-frontend/src/
├── pages/client/
│   └── CoachIAPage.jsx           # Page principale du coach
└── components/coach/
    ├── ObjectifCard.jsx           # Carte avec checkbox pour marquer comme fait
    ├── WeekProgress.jsx           # Progression semaine N/12
    └── CoachHistory.jsx           # Historique semaines passées
```

**CoachIAPage.jsx :**

```jsx
// 1. Header : "🎯 Mon Coach IA" + numéro de semaine
// 2. Barre de progression globale (semaine 7/12)
// 3. Liste des objectifs de la semaine avec statuts :
//    ✅ Lancer campagne Google Ads [Fait]
//    ⚠️ Publier 3 posts LinkedIn (1/3) [En cours]
//    ❌ Google My Business [À faire]
// 4. Historique des 4 semaines précédentes
// 5. Bouton [💬 Parler au coach IA] → ouvre le chatbot
```

---

## 📁 Structure des fichiers finaux (Phase 2 complète)

### Backend (hwc-backend/)

```
src/main/java/hwc_backend/
├── entity/
│   ├── [Phase 1 existant...]
│   ├── User.java                      # 🆕
│   ├── CategorieDiagnostic.java       # 🆕
│   ├── Question.java                  # 🆕
│   ├── OptionReponse.java             # 🆕
│   ├── Diagnostic.java                # 🆕
│   ├── ReponseDiagnostic.java         # 🆕
│   ├── Score.java                     # 🆕
│   ├── RegleRecommandation.java       # 🆕
│   ├── Recommandation.java            # 🆕
│   ├── RapportPdf.java                # 🆕
│   ├── ChatConversation.java          # 🆕
│   ├── ChatMessage.java               # 🆕
│   ├── CoachObjectifsHebdo.java       # 🆕
│   ├── CoachObjectifResultat.java     # 🆕
│   └── CoachEmailEnvoye.java          # 🆕
│
├── service/
│   ├── [Phase 1 existant...]
│   ├── ClientAuthService.java         # 🆕
│   ├── DiagnosticService.java         # 🆕
│   ├── ScoringService.java            # 🆕
│   ├── RecommandationService.java     # 🆕
│   ├── DashboardClientService.java    # 🆕
│   ├── OpenAIService.java             # 🆕
│   ├── RapportService.java            # 🆕
│   ├── ChatService.java               # 🆕
│   └── CoachIAService.java            # 🆕
│
├── controller/
│   ├── [Phase 1 existant...]
│   ├── ClientAuthController.java      # 🆕 /api/client/auth
│   ├── DiagnosticController.java      # 🆕 /api/client/diagnostics
│   ├── RecommandationController.java  # 🆕 /api/client/recommandations
│   ├── DashboardClientController.java # 🆕 /api/client/dashboard
│   ├── RapportController.java         # 🆕 /api/client/rapports
│   ├── ChatController.java            # 🆕 /api/client/chat
│   └── CoachIAController.java         # 🆕 /api/client/coach
│
└── scheduler/
    └── CoachIAScheduler.java          # 🆕 @Scheduled
```

### Frontend (hwc-frontend/src/)

```
├── pages/client/                        # 🆕 toutes nouvelles
│   ├── ClientLoginPage.jsx
│   ├── ClientRegisterPage.jsx
│   ├── DiagnosticStartPage.jsx
│   ├── DiagnosticQuestionsPage.jsx
│   ├── DiagnosticResultatPage.jsx
│   ├── RecommandationsPage.jsx
│   ├── DashboardClientPage.jsx
│   └── CoachIAPage.jsx
│
├── components/diagnostic/               # 🆕
│   ├── ProgressBar.jsx
│   ├── CategoryProgress.jsx
│   ├── QuestionCard.jsx
│   └── ScoreCard.jsx
│
├── components/dashboard/                # 🆕
│   ├── ScoreGlobalDonut.jsx
│   ├── KpiCards.jsx
│   ├── RadarChartScores.jsx
│   ├── HistoriqueBarChart.jsx
│   ├── CategoryDetailCards.jsx
│   ├── PlanActionTimeline.jsx
│   ├── AlertesCritiques.jsx
│   ├── ServicesRecommandes.jsx
│   └── BenchmarkSecteur.jsx
│
├── components/chat/                     # 🆕
│   ├── ChatbotButton.jsx
│   ├── ChatbotWindow.jsx
│   ├── ChatMessage.jsx
│   ├── ChatInput.jsx
│   └── ChatSuggestions.jsx
│
├── components/coach/                    # 🆕
│   ├── ObjectifCard.jsx
│   ├── WeekProgress.jsx
│   └── CoachHistory.jsx
│
├── api/                                 # 🆕 fichiers API
│   ├── clientAuthApi.js
│   ├── diagnosticApi.js
│   ├── recommandationApi.js
│   ├── dashboardApi.js
│   ├── rapportApi.js
│   ├── chatApi.js
│   └── coachApi.js
│
└── store/
    ├── clientAuthStore.js               # 🆕
    └── diagnosticStore.js               # 🆕
```

---

## 🔗 Tableau des routes API complètes (Phase 2)

| Méthode | Endpoint                                       | Description                  | Auth   |
| ------- | ---------------------------------------------- | ---------------------------- | ------ |
| POST    | `/api/client/auth/register`                    | Inscription client           | Public |
| POST    | `/api/client/auth/login`                       | Connexion client             | Public |
| GET     | `/api/client/auth/me`                          | Profil connecté              | Client |
| POST    | `/api/client/diagnostics/start`                | Démarrer diagnostic          | Client |
| GET     | `/api/client/diagnostics/questions`            | Charger toutes les questions | Client |
| POST    | `/api/client/diagnostics/{id}/reponses`        | Répondre à une question      | Client |
| POST    | `/api/client/diagnostics/{id}/finalize`        | Finaliser + calculer scores  | Client |
| GET     | `/api/client/diagnostics/history`              | Historique des diagnostics   | Client |
| GET     | `/api/client/diagnostics/{id}`                 | Détail d'un diagnostic       | Client |
| GET     | `/api/client/diagnostics/{id}/recommandations` | Recommandations              | Client |
| GET     | `/api/client/diagnostics/{id}/plan-action`     | Plan d'action structuré      | Client |
| GET     | `/api/client/dashboard`                        | Dashboard BI complet         | Client |
| GET     | `/api/client/diagnostics/{id}/pdf`             | Générer et télécharger PDF   | Client |
| GET     | `/api/client/rapports`                         | Historique PDF               | Client |
| POST    | `/api/client/chat/message`                     | Envoyer message chatbot      | Client |
| GET     | `/api/client/chat/conversations`               | Liste des conversations      | Client |
| GET     | `/api/client/chat/conversations/{id}/messages` | Historique conversation      | Client |
| GET     | `/api/client/coach/current-week`               | Objectifs semaine courante   | Client |
| PATCH   | `/api/client/coach/objectifs/{id}/complete`    | Marquer objectif fait        | Client |
| GET     | `/api/client/coach/history`                    | Historique coach             | Client |
| GET     | `/api/admin/regles-recommandation`             | Lister les règles            | Admin  |
| POST    | `/api/admin/regles-recommandation`             | Créer une règle              | Admin  |
| PUT     | `/api/admin/regles-recommandation/{id}`        | Modifier une règle           | Admin  |
| DELETE  | `/api/admin/regles-recommandation/{id}`        | Supprimer une règle          | Admin  |
| GET     | `/api/admin/diagnostics`                       | Tous les diagnostics (admin) | Admin  |
| GET     | `/api/admin/users/clients`                     | Liste des clients            | Admin  |

---

## 📋 Ordre de développement recommandé

```
Semaine 1 :
  ✅ Créer les premières tables BDD nécessaires au diagnostic via les entités JPA
  ✅ Insérer les seed data des catégories
  🔄 Règles de recommandation encore à faire
  🔄 DTOs encore à faire

Semaine 2 :
  ✅ Auth client (register/login)
  🔄 Module 3 Backend : `DiagnosticController` + `ScoringService`
  🔄 Tester avec Postman

Semaine 3 :
  🔄 Module 3 Frontend : pages diagnostic (Start → Questions → Résultats)
  🔄 Module 4 Backend : `RecommandationService`
  🔄 Module 4 Frontend : `RecommandationsPage`

Semaine 4 :
  🔄 Installer Recharts dans le frontend
  🔄 Module 5 Frontend : `DashboardClientPage` (10 sections)
  🔄 Module 5 Backend : `DashboardClientController`

Semaine 5 :
  🔄 Module 6 Backend : `OpenAIService` + `RapportService` + PDF
  🔄 Module 6 Frontend : bouton téléchargement + loader
  🔄 Tester la génération de PDF avec vraies données

Semaine 6 :
  🔄 Module 7A Backend : `ChatService` (RAG)
  🔄 Module 7A Frontend : `ChatbotButton` + `ChatbotWindow`
  🔄 Module 7B Backend : `CoachIAService` + `CoachIAScheduler`
  🔄 Module 7B Frontend : `CoachIAPage`

Semaine 7-8 :
  🔄 Tests fonctionnels complets
  🔄 Corrections et optimisations
  🔄 Préparation démo soutenance
```

---

## ⚠️ Règles importantes pour l'assistant

1. **Ne pas modifier** les fichiers existants de la Phase 1 (sauf ajouts dans SecurityConfig, App.jsx, application.properties)
2. **Toujours valider** côté backend ET frontend
3. **JWT client** = token séparé du token admin (même système, rôles différents : ROLE_CLIENT vs ROLE_ADMIN)
4. **OpenAI API Key** = toujours dans les variables d'environnement, jamais en dur
5. **Recharts** = librairie pour tous les graphiques du dashboard
6. **Un fichier par composant** React
7. **Clé étrangère** : les recommandations pointent vers les services HWC existants (table `services` et `sous_services` de la Phase 1)
8. **Palette de couleurs HWC** : primary `#2E7D6B`, danger `#EF4444`, warning `#EAB308`, success `#22C55E`
