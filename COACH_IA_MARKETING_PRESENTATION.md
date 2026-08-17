# Rapport complet — Coach IA « Stratégie Marketing Digital »

## 1. Résumé exécutif

Ce rapport décrit la conception, l'implémentation et la validation du premier service spécialisé du Coach IA HWC :

> Développer votre stratégie Marketing Digital

Son identifiant technique unique est `MARKETING_STRATEGY`.

Le service accompagne un client de manière progressive afin de produire une mini-stratégie Marketing Digital réaliste, mesurable et adaptée à son entreprise. Il ne fournit pas immédiatement une réponse générique : il collecte les informations nécessaires, conserve l'état de la session, consulte une bibliothèque métier isolée grâce au RAG, puis génère un livrable final structuré.

La réalisation couvre :

- une bibliothèque de 12 documents Marketing Digital ;
- deux documents globaux du Coach IA ;
- une ingestion Markdown avec métadonnées YAML ;
- un découpage en chunks par sections ;
- des embeddings locaux avec Ollama ;
- une recherche vectorielle obligatoirement filtrée par service ;
- un workflow contrôlé comportant 11 états ;
- une persistance MySQL des sessions et messages ;
- cinq endpoints REST sécurisés ;
- une interface conversationnelle React ;
- un livrable final JSON et Markdown ;
- dix scénarios métier ;
- des tests unitaires, d'intégration et des tests réels avec Ollama.

## 2. Problématique initiale

Les premiers documents importés dans `hwc-frontend/src/coach-ia/` concernent principalement :

- la performance commerciale ;
- la performance organisationnelle ;
- l'alignement commercial et organisationnel ;
- les KPI de performance ;
- l'optimisation des processus ;
- la gestion du changement.

Ils ne correspondent pas au périmètre du premier service Marketing Digital. Les intégrer dans un index commun aurait créé plusieurs risques :

- récupération de règles provenant du mauvais métier ;
- recommandations commerciales au lieu de recommandations marketing ;
- difficulté à expliquer la source d'une réponse ;
- impossibilité de tester chaque service indépendamment ;
- mélange futur des quatre services du Coach IA.

La décision fondamentale est donc la suivante :

> Chaque service possède sa bibliothèque, ses prompts, son workflow et son filtre de recherche. Les services sont développés et validés un par un.

Les fichiers commerciaux et organisationnels restent disponibles pour un futur service, mais ils ne sont ni déplacés ni indexés dans `MARKETING_STRATEGY`.

## 3. Objectifs fonctionnels

Le service doit permettre au client de :

1. décrire son entreprise et son activité ;
2. transformer une intention en objectif SMART ;
3. analyser son marketing existant ;
4. identifier une cible prioritaire ;
5. construire un positionnement et une proposition de valeur ;
6. sélectionner des canaux justifiés ;
7. vérifier le budget, le temps et les ressources ;
8. produire un plan d'action sur quatre semaines ;
9. sélectionner des KPI utiles ;
10. obtenir une mini-stratégie finale structurée.

Le service doit aussi reprendre une session existante et ne jamais recommencer la conversation depuis zéro lorsque des informations ont déjà été sauvegardées.

## 4. Principes de conception

### 4.1 Isolation par service

Tous les chunks utilisables dans ce workflow reçoivent :

```text
service_id = MARKETING_STRATEGY
```

Chaque recherche vectorielle construit également un filtre obligatoire sur cette valeur. Le filtre n'est pas fourni par le frontend et ne dépend pas du texte utilisateur : il est imposé par le backend.

### 4.2 Le RAG ne remplace pas l'état de session

Le RAG apporte des références métier, mais il ne sait pas naturellement où se trouve le client dans le parcours. Un état structuré persistant conserve donc :

- l'étape active ;
- les informations de l'entreprise ;
- l'objectif ;
- l'audit ;
- la cible ;
- le positionnement ;
- le budget et les ressources ;
- les canaux ;
- les actions ;
- les KPI ;
- les informations manquantes ;
- les hypothèses ;
- le niveau de confiance ;
- le statut de finalisation.

### 4.3 Le backend contrôle le workflow

Le LLM extrait et reformule les informations, mais ne décide pas librement du passage à l'étape suivante. `MarketingStageEvaluator` vérifie les champs minimaux et ne permet qu'une progression contrôlée.

### 4.4 Une question principale à la fois

Le Coach réduit la charge cognitive du client. Chaque réponse contient normalement une seule question principale correspondant à l'information essentielle manquante dans l'étape active.

### 4.5 Aucune donnée inventée

Une valeur absente reste `null`, vide ou présente dans `missingInformation`. Le Coach distingue :

- les informations données par le client ;
- les hypothèses à confirmer ;
- les recommandations produites à partir des données validées.

## 5. Technologies utilisées

### Backend

- Java 21 ;
- Spring Boot 4 ;
- Spring MVC ;
- Spring Security ;
- Spring Data JPA ;
- MySQL en exécution normale ;
- H2 pour les tests ;
- Jackson pour les états JSON ;
- SnakeYAML pour les front matters ;
- LangChain4j `1.18.1` ;
- Ollama pour le LLM et les embeddings.

### Frontend

- React 18 ;
- React Router ;
- Axios ;
- Tailwind CSS ;
- Lucide React ;
- React Hot Toast ;
- Vite.

### Modèles locaux

- `llama3.2:latest` : extraction structurée et génération ;
- `nomic-embed-text` : embeddings de la bibliothèque et des requêtes.

## 6. Organisation complète des ressources

```text
src/main/resources/coach-ia/
├── global/
│   ├── 00_mission_coach_ia.md
│   └── 01_regles_globales_coach.md
└── services/
    └── marketing-strategy/
        ├── manifest.yml
        ├── knowledge/
        │   ├── 01_cadrage_entreprise.md
        │   ├── 02_objectifs_marketing_smart.md
        │   ├── 03_audit_marketing_existant.md
        │   ├── 04_cible_personas_segmentation.md
        │   ├── 05_positionnement_proposition_valeur.md
        │   ├── 06_choix_canaux_marketing.md
        │   ├── 07_budget_ressources.md
        │   ├── 08_plan_action_hebdomadaire.md
        │   ├── 09_kpi_marketing.md
        │   ├── 10_erreurs_frequentes_marketing.md
        │   ├── 11_methodologie_accompagnement_marketing.md
        │   └── 12_cas_pratiques_marketing.md
        ├── prompts/
        │   ├── system.md
        │   ├── next-question.md
        │   └── final-deliverable.md
        ├── templates/
        │   └── mini_strategie_marketing.md
        ├── schemas/
        │   ├── session-state.schema.json
        │   └── final-strategy.schema.json
        └── tests/
            └── scenarios.json
```

## 7. Rôle de chaque dossier

| Dossier | Indexé | Utilisation |
|---|---:|---|
| `global/` | Oui | Mission et règles transverses du Coach |
| `knowledge/` | Oui | Connaissance spécialisée Marketing Digital |
| `prompts/` | Non | Instructions chargées directement dans les appels LLM |
| `templates/` | Non | Construction du livrable Markdown |
| `schemas/` | Non | Contrats JSON de l'état et du résultat |
| `tests/` | Non | Validation métier uniquement |

Le service d'ingestion utilise explicitement deux chemins :

```text
classpath*:coach-ia/global/*.md
classpath*:coach-ia/services/marketing-strategy/knowledge/*.md
```

Il ne parcourt jamais automatiquement tout `coach-ia/`.

## 8. Bibliothèque Marketing Digital

### 8.1 Cadrage de l'entreprise

Collecte l'activité, les offres, le modèle B2B/B2C, la localisation, la taille, la maturité et les concurrents. Il empêche une recommandation sans compréhension du contexte.

### 8.2 Objectifs SMART

Transforme une intention vague en résultat mesurable avec valeur cible et échéance. Une activité comme « publier davantage » n'est pas confondue avec un résultat business.

### 8.3 Audit existant

Analyse le site, le SEO, les réseaux sociaux, les publicités, l'email, Google Business Profile, le contenu, le CRM et le suivi des conversions.

### 8.4 Cible et persona

Identifie les segments, besoins, motivations, objections, comportements d'achat et décideurs. Le Coach refuse « tout le monde » comme cible opérationnelle.

### 8.5 Positionnement

Construit la proposition de valeur, les différences, les bénéfices, les preuves et le positionnement par rapport aux alternatives réellement envisagées.

### 8.6 Choix des canaux

Document central expliquant SEO, Google Ads, Meta Ads, Instagram, Facebook, LinkedIn, TikTok, email, Google Business Profile, contenu et partenariats. Chaque canal est relié à ses objectifs, conditions, limites et KPI.

### 8.7 Budget et ressources

Évite les stratégies irréalistes en vérifiant budget, temps, équipe, compétences, outils, production de contenu et capacité à traiter les prospects.

### 8.8 Plan hebdomadaire

Organise la réalisation en quatre semaines : préparation, création, lancement, mesure et optimisation. Chaque action doit être responsable, datée et mesurable.

### 8.9 KPI Marketing

Sélectionne un petit nombre d'indicateurs reliés à l'objectif : prospects qualifiés, conversion, CPL, CAC, ventes, réservations, rétention ou ROI.

### 8.10 Erreurs fréquentes

Protège contre les canaux choisis par popularité, l'absence de tracking, la confusion entre likes et résultats, le ciblage trop large et l'augmentation du trafic sans capacité de traitement.

### 8.11 Méthodologie

Décrit l'ordre obligatoire du parcours et les conditions permettant de changer d'étape.

### 8.12 Cas pratiques

Contient dix contextes : restaurant, immobilier, e-commerce, cabinet médical, salle de sport, B2B, hôtel, SaaS, artisan et centre de formation.

## 9. Manifest et métadonnées

`manifest.yml` définit :

- l'identifiant et la version du service ;
- les fichiers inclus et exclus ;
- la stratégie de chunking ;
- les métadonnées obligatoires ;
- les étapes du workflow ;
- le filtre de recherche ;
- le nombre maximal de résultats ;
- les règles de génération.

Chaque document possède un front matter YAML. Chaque chunk conserve :

| Métadonnée | Description |
|---|---|
| `document_id` | Identifiant stable du document source |
| `title` | Titre métier |
| `service_id` | `MARKETING_STRATEGY` dans le vector store |
| `source_service_id` | Valeur déclarée par le document, notamment `GLOBAL` |
| `section` | Titre Markdown de la section |
| `scope` | Périmètre fonctionnel |
| `retrieval_tags` | Termes de récupération |
| `version` | Version du contenu |
| `source_file` | Nom du fichier source |
| `chunk_index` | Position du chunk dans la section |

Les listes YAML sont transformées en valeurs scalaires compatibles avec les métadonnées LangChain4j.

## 10. Pipeline d'ingestion

La classe principale est `MarketingKnowledgeIngestionService`.

### Étapes

1. Charger uniquement les deux documents globaux et les 12 documents Marketing.
2. Lire les fichiers en UTF-8.
3. séparer le front matter YAML du contenu Markdown.
4. vérifier les six métadonnées obligatoires.
5. refuser un document hors périmètre.
6. découper le contenu selon les titres Markdown.
7. redécouper une section dépassant 2 400 caractères.
8. appliquer un chevauchement de 250 caractères.
9. enrichir chaque segment avec ses métadonnées.
10. créer un identifiant SHA-256 stable.
11. générer les embeddings avec `nomic-embed-text`.
12. remplacer le contenu du store puis ajouter les nouveaux chunks.

### Identifiants stables

L'identifiant d'un chunk est calculé à partir de :

```text
document_id | section | chunk_index
```

Cela améliore la traçabilité et prépare les futures opérations de mise à jour ciblée.

### Activation

L'ingestion automatique est contrôlée par :

```text
coach.marketing.rag.enabled
```

Elle est activée par défaut dans l'application afin que le Coach Marketing fonctionne directement lorsque les modèles Ollama requis sont installés. Elle reste explicitement désactivée dans `src/test/resources/application.properties` pour que les tests standards puissent s'exécuter sans Ollama. Lorsqu'elle est active, l'ingestion s'exécute après le démarrage Spring.

## 11. Recherche RAG filtrée

`MarketingRagServiceImpl` reçoit une question enrichie avec l'étape active, puis :

1. génère son embedding ;
2. construit une `EmbeddingSearchRequest` ;
3. applique le nombre maximal de résultats ;
4. applique le score minimal ;
5. impose le filtre :

```java
metadataKey("service_id").isEqualTo("MARKETING_STRATEGY")
```

6. retourne le texte, le score, l'identifiant et les métadonnées ;
7. journalise le document, la section, le score et le service.

Un test négatif place volontairement dans le store un chunk commercial ayant exactement le même embedding que le chunk Marketing. Seul le chunk Marketing est retourné.

## 12. Architecture Java

```text
src/main/java/hwc_backend/coach/marketing/
├── config/
│   └── MarketingRagConfiguration.java
├── controller/
│   └── MarketingStrategyController.java
├── entity/
│   ├── MarketingSession.java
│   └── MarketingSessionMessage.java
├── ingestion/
│   └── MarketingKnowledgeIngestionService.java
├── model/
│   ├── MarketingKnowledgeChunk.java
│   ├── MarketingMessageRequest.java
│   ├── MarketingMessageResponse.java
│   ├── MarketingSessionCreateRequest.java
│   ├── MarketingSessionState.java
│   ├── MarketingStrategyResult.java
│   └── MarketingStrategyStage.java
├── repository/
│   ├── MarketingSessionMessageRepository.java
│   └── MarketingSessionRepository.java
└── service/
    ├── MarketingDeliverableService.java
    ├── MarketingDeliverableServiceImpl.java
    ├── MarketingLanguageModelService.java
    ├── MarketingPromptService.java
    ├── MarketingRagService.java
    ├── MarketingRagServiceImpl.java
    ├── MarketingSessionService.java
    ├── MarketingSessionServiceImpl.java
    ├── MarketingStageEvaluator.java
    ├── MarketingStrategyService.java
    ├── MarketingStrategyServiceImpl.java
    └── OllamaMarketingLanguageModelService.java
```

## 13. État de session

Les étapes sont :

```text
COMPANY_DISCOVERY
OBJECTIVES
CURRENT_AUDIT
TARGET_AUDIENCE
POSITIONING
CHANNEL_SELECTION
BUDGET_AND_RESOURCES
ACTION_PLAN
KPI_SELECTION
FINAL_DELIVERABLE
COMPLETED
```

### Informations minimales par étape

| Étape | Conditions principales |
|---|---|
| Entreprise | secteur, offres, modèle, localisation, taille |
| Objectif | type, cible, échéance, formulation SMART |
| Audit | statut du site et du tracking |
| Cible | segment prioritaire et persona |
| Positionnement | proposition de valeur |
| Canaux | au moins un canal justifié |
| Budget | montant, temps disponible, capacité de traitement |
| Plan | actions hebdomadaires |
| KPI | au moins un indicateur |
| Livrable | toutes les étapes précédentes complètes |

Le niveau de confiance utilise `LOW`, `MEDIUM` ou `HIGH` selon l'avancement et les informations manquantes.

## 14. Persistance JPA/MySQL

### Table `marketing_coach_sessions`

Elle contient notamment :

- UUID de session ;
- utilisateur propriétaire ;
- service immuable ;
- étape active ;
- état JSON complet ;
- livrable JSON ;
- livrable Markdown ;
- statut terminé ;
- version optimiste ;
- dates de création et modification.

### Table `marketing_coach_messages`

Elle conserve :

- la session ;
- le rôle `USER`, `ASSISTANT` ou `SYSTEM` ;
- le contenu ;
- l'étape active ;
- le contexte RAG sérialisé ;
- la date de création.

### Sécurité de propriété

Chaque lecture et modification utilise une requête combinant :

```text
sessionId + email du client authentifié
```

Une session étrangère est présentée comme introuvable. `@Version` protège également contre deux mises à jour concurrentes qui écraseraient le même état.

## 15. Workflow conversationnel détaillé

```text
Message utilisateur
        ↓
Vérification de l'authentification et du propriétaire
        ↓
Chargement de l'état persistant
        ↓
Sauvegarde du message USER
        ↓
Recherche RAG filtrée sur l'étape + le message
        ↓
Chargement de system.md et next-question.md
        ↓
Construction du prompt avec état + message + chunks
        ↓
Appel JSON à llama3.2
        ↓
Fusion sécurisée du patch LLM avec l'état persistant
        ↓
Réapplication des champs protégés
        ↓
Évaluation déterministe de l'étape
        ↓
Sauvegarde transactionnelle de l'état
        ↓
Sauvegarde de la question ASSISTANT
        ↓
Réponse frontend
```

### Fusion des réponses partielles

Les tests réels ont montré que `llama3.2` peut retourner un `updatedState` partiel. Le backend effectue donc une fusion récursive avec l'état existant au lieu de remplacer entièrement celui-ci.

Les champs suivants sont toujours protégés et réappliqués depuis la base :

- `serviceId` ;
- `sessionId` ;
- `stage` ;
- `completed`.

Les valeurs `null` produites par le LLM n'effacent pas une information déjà validée.

## 16. Prompts

### `system.md`

Définit l'identité Marketing du Coach, l'interdiction d'inventer, l'usage du RAG, la séparation données/hypothèses/recommandations et la règle d'une question principale.

### `next-question.md`

Demande un JSON contenant :

```json
{
  "updatedState": {},
  "reply": "Question suivante"
}
```

Le prompt contient des exemples explicites d'extraction : restaurant vers `Restauration`, B2C vers `businessModel`, Casablanca vers `location`, TPE vers `size`.

### `final-deliverable.md`

Demande un JSON final sans donnée inventée, avec canaux justifiés, exactement quatre semaines, KPI, hypothèses et priorité immédiate.

## 17. Génération du livrable

La finalisation suit ces contrôles :

1. l'étape active doit être `FINAL_DELIVERABLE` ;
2. toutes les étapes métier sont revérifiées ;
3. le LLM retourne un objet JSON ;
4. les champs obligatoires sont vérifiés ;
5. l'identifiant de session doit correspondre ;
6. un à trois canaux sont acceptés ;
7. le plan contient exactement quatre semaines ;
8. au moins un KPI est présent ;
9. le JSON est normalisé ;
10. le template Markdown est rempli ;
11. les deux versions sont persistées ;
12. la session passe à `COMPLETED` avec confiance `HIGH`.

Le livrable comporte 11 sections : entreprise, audit, objectif, cible, positionnement, canaux, plan, budget, KPI, hypothèses et priorité immédiate.

## 18. API REST

Base :

```text
/api/client/coach/marketing/sessions
```

### Créer une session

```http
POST /api/client/coach/marketing/sessions
Content-Type: application/json

{
  "serviceId": "MARKETING_STRATEGY"
}
```

Seule cette valeur est acceptée par validation Jakarta.

### Envoyer un message

```http
POST /api/client/coach/marketing/sessions/{sessionId}/messages
Content-Type: application/json

{
  "message": "Nous sommes un restaurant B2C à Casablanca."
}
```

Le message est obligatoire et limité à 4 000 caractères.

### Récupérer l'état

```http
GET /api/client/coach/marketing/sessions/{sessionId}
```

### Finaliser

```http
POST /api/client/coach/marketing/sessions/{sessionId}/finalize
```

### Récupérer le livrable

```http
GET /api/client/coach/marketing/sessions/{sessionId}/deliverable
Accept: text/markdown
```

Tous ces endpoints sont protégés par le rôle `CLIENT` via la règle existante `/api/client/**`.

## 19. Intégration frontend

### Fichiers principaux

```text
hwc-frontend/src/api/marketingCoachApi.js
hwc-frontend/src/pages/client/MarketingStrategyCoachPage.jsx
hwc-frontend/src/components/dashboard/ServicesRecommandes.jsx
hwc-frontend/src/pages/client/ClientProtectedRoute.jsx
hwc-frontend/src/App.jsx
```

### Comportement

- la carte « Stratégie Marketing Digital » ouvre `/client/coach/marketing` ;
- la page crée une session avec `MARKETING_STRATEGY` ;
- l'identifiant est stocké dans `localStorage` ;
- une session existante est rechargée après actualisation ;
- l'étape et la confiance sont affichées ;
- les échanges USER/ASSISTANT utilisent des styles distincts ;
- un état de chargement apparaît pendant l'analyse ;
- le bouton final n'apparaît que lorsque la session est prête ;
- le livrable Markdown est affiché dans la page ;
- le chatbot flottant général est masqué sur toutes les routes `/client/coach...`.

## 20. Logs et observabilité

Les logs permettent d'observer :

- la création d'une session ;
- l'utilisateur propriétaire sous forme d'identifiant ;
- l'étape précédente et l'étape active ;
- le niveau de confiance ;
- le nombre d'informations manquantes ;
- chaque document indexé ;
- le nombre de chunks ;
- le document et la section récupérés ;
- le score de similarité ;
- le `service_id` récupéré ;
- les dimensions du livrable final.

Aucun mot de passe, token JWT ou secret de messagerie ne doit apparaître dans les logs.

## 21. Configuration

```properties
coach.marketing.rag.enabled=${COACH_MARKETING_RAG_ENABLED:true}
coach.marketing.rag.embedding-model=${COACH_MARKETING_EMBEDDING_MODEL:nomic-embed-text}
coach.marketing.rag.max-results=${COACH_MARKETING_RAG_MAX_RESULTS:5}
coach.marketing.rag.min-score=${COACH_MARKETING_RAG_MIN_SCORE:0.65}
```

Ollama réutilise :

```properties
ollama.base-url=${OLLAMA_BASE_URL:http://localhost:11434}
ollama.model=${OLLAMA_MODEL:llama3.2:latest}
```

## 22. Scénarios métier

`scenarios.json` contient dix scénarios :

| Scénario | Exemples de priorités | KPI principaux |
|---|---|---|
| Restaurant local | Google Business Profile, SEO local, avis | réservations, appels |
| Agence immobilière | recherche locale, landing page, preuves | estimations, mandats |
| E-commerce | tracking, email, SEO produit | conversion, CAC, marge |
| Cabinet médical | local, contenu prudent, rendez-vous | rendez-vous, présence |
| Salle de sport | offre d'essai, Meta, relance | essais, abonnements, rétention |
| Entreprise B2B | LinkedIn, contenu expert, prospection | rendez-vous, opportunités |
| Hôtel | Google Ads, contenu destination, email | réservations, occupation |
| SaaS | contenu, démo, onboarding | activation, conversion, rétention |
| Artisan | présence locale, avis, SEO local | devis, acceptation |
| Centre de formation | LinkedIn, email, webinaire | inscriptions, présence |

Chaque scénario contient également des comportements interdits pour vérifier les erreurs métier importantes.

## 23. Stratégie de tests

Les tests couvrent :

- chargement exclusif des 14 documents autorisés ;
- présence des métadonnées ;
- absence des fichiers commerciaux dans l'ingestion ;
- filtre vectoriel obligatoire ;
- création et reprise de session ;
- refus d'une session étrangère ;
- préremplissage du profil client ;
- progression contrôlée ;
- impossibilité de sauter une étape ;
- impossibilité de finaliser trop tôt ;
- chargement des prompts et du template ;
- routage des endpoints avec l'identité authentifiée ;
- validation des dix scénarios ;
- génération, validation, rendu et persistance du livrable ;
- ingestion réelle avec Ollama ;
- extraction JSON réelle avec `llama3.2`.

## 24. Résultats mesurés

### Régression standard

```text
Tests découverts : 50
Tests réussis : 48
Tests ignorés par défaut : 2 tests Ollama optionnels
Échecs : 0
Build backend : SUCCESS
Build frontend : SUCCESS
```

### Test RAG réel

```text
Documents indexés : 14
Chunks produits : 44
Question : Quels canaux recommander à un restaurant local qui veut davantage de réservations ?
Premier résultat : MKT-CASES-012 / Restaurant local
Score principal observé : 0,8536544154
Filtre vérifié : MARKETING_STRATEGY pour tous les résultats
```

Les autres résultats pertinents comprenaient les règles conditionnelles de choix des canaux, l'audit marketing et les règles globales de qualité.

### Test LLM réel

À partir de :

> Nous sommes un restaurant marocain B2C à Casablanca, une TPE qui vend des repas sur place et à emporter.

`llama3.2` a extrait :

```json
{
  "sector": "Restauration",
  "productsOrServices": ["repas sur place", "à emporter"],
  "businessModel": "B2C",
  "location": "Casablanca",
  "size": "TPE"
}
```

Ce test a également conduit à renforcer le prompt et à ajouter la fusion sécurisée des états partiels.

## 25. Installation et lancement

### Vérifier Ollama

```powershell
ollama list
```

Les modèles nécessaires :

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
```

### Démarrer le backend avec le RAG

Dans le même terminal que celui du backend :

```powershell
$env:COACH_MARKETING_RAG_ENABLED = "true"
$env:COACH_MARKETING_EMBEDDING_MODEL = "nomic-embed-text"
$env:OLLAMA_MODEL = "llama3.2:latest"
mvn.cmd spring-boot:run
```

Les variables habituelles doivent aussi être présentes : `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` et les autres variables nécessaires au projet.

### Démarrer le frontend

```powershell
Set-Location hwc-frontend
npm.cmd run dev
```

## 26. Exécuter les tests

### Tests standards

```powershell
mvn.cmd test
```

### Tests Ollama réels

```powershell
$env:RUN_MARKETING_RAG_IT = "true"
mvn.cmd -Dtest=MarketingRagOllamaIntegrationTests test
mvn.cmd -Dtest=MarketingLlmOllamaIntegrationTests test
```

### Build frontend

```powershell
Set-Location hwc-frontend
npm.cmd run build
```

## 27. Déroulement recommandé de la présentation

1. Expliquer pourquoi les services ont été séparés.
2. Montrer la structure `global/` et `marketing-strategy/`.
3. Montrer un front matter YAML et ses métadonnées.
4. Présenter le filtre `service_id=MARKETING_STRATEGY`.
5. Montrer les 11 étapes et les champs minimaux.
6. Démarrer Ollama, le backend et le frontend.
7. Se connecter avec un compte CLIENT.
8. Ouvrir la carte Marketing.
9. Vérifier la requête de création de session dans l'onglet Network.
10. Répondre à la première question avec plusieurs informations explicites.
11. Montrer dans les logs les chunks et leurs scores.
12. Actualiser la page pour démontrer la reprise de session.
13. Continuer jusqu'à la finalisation ou utiliser une session préparée.
14. Générer la mini-stratégie.
15. Montrer les quatre semaines, les KPI et les hypothèses.
16. Terminer par les résultats des tests réels.

## 28. Garde-fous récapitulatifs

- service unique accepté à la création ;
- recherche filtrée côté backend ;
- ressources non métier jamais indexées ;
- validation des métadonnées ;
- une question principale ;
- canaux interdits avant l'étape prévue ;
- valeurs nulles non inventées ;
- champs d'identité protégés ;
- états partiels fusionnés sans effacer l'historique ;
- progression déterministe ;
- propriété de session vérifiée ;
- verrouillage optimiste ;
- finalisation conditionnelle ;
- schéma et structure du livrable vérifiés ;
- nombre de canaux et de semaines contrôlé.

## 29. Limites actuelles

### Store en mémoire

Le store vectoriel est actuellement `InMemoryEmbeddingStore`. Il est reconstruit au démarrage et convient au prototype et à la démonstration. Pour une production distribuée, Qdrant ou un autre store persistant est recommandé.

### Temps de réponse local

`llama3.2` s'exécute localement. Le temps de réponse dépend du CPU, de la mémoire et de la disponibilité d'Ollama.

### Historique frontend

L'état métier est repris depuis le backend, mais l'interface ne recharge pas encore la totalité de l'historique visuel des messages. Les messages sont déjà persistés et un endpoint dédié pourra être ajouté.

### Export PDF

Le livrable est disponible en JSON et Markdown. L'export PDF n'est pas encore exposé, même si PDFBox est déjà présent dans le projet.

### Migrations de base

Le projet utilise actuellement `ddl-auto=update`. Pour la production, des migrations Flyway ou Liquibase sont recommandées.

## 30. Évolutions recommandées

1. remplacer le store en mémoire par Qdrant ;
2. ajouter un endpoint d'historique des messages ;
3. ajouter l'export PDF ;
4. ajouter des migrations versionnées ;
5. créer un écran administrateur pour réindexer et voir la version des documents ;
6. ajouter des métriques de latence, tokens et taux d'erreur ;
7. tester plusieurs modèles LLM ;
8. améliorer le rendu Markdown avec un composant dédié ;
9. ajouter les trois autres services, chacun avec son propre `service_id` ;
10. créer un routeur de services uniquement lorsque chaque module spécialisé est validé.

## 31. Critères de fin du premier service

| Critère | État |
|---|---:|
| La carte ouvre une session Marketing | Terminé |
| Seuls les documents Marketing et globaux sont indexés | Terminé |
| Le RAG filtre obligatoirement le service | Terminé |
| Une session existante est reprise | Terminé |
| Le Coach suit un workflow par étapes | Terminé |
| Les réponses client sont conservées | Terminé |
| Les canaux sont proposés après le cadrage | Terminé |
| Le plan comporte quatre semaines | Terminé |
| Les KPI sont structurés | Terminé |
| Le livrable JSON et Markdown est généré | Terminé |
| Les données inventées sont interdites | Terminé |
| Les principaux cas métier sont testés | Terminé |
| Le RAG réel fonctionne avec Ollama | Terminé |
| L'extraction réelle fonctionne avec llama3.2 | Terminé |

## 32. Conclusion

Le premier service du Coach IA est maintenant isolé, structuré et validé de bout en bout. L'architecture sépare correctement la connaissance, les prompts, l'état de session, la recherche vectorielle, le workflow, la persistance, l'API et l'interface.

Le résultat ne repose pas uniquement sur un chatbot : il s'agit d'un workflow métier contrôlé, traçable et testable. Les validations réelles avec Ollama ont confirmé l'indexation, la pertinence de la recherche et l'extraction structurée. Elles ont aussi permis de corriger un comportement réel du modèle grâce à la fusion sécurisée des états partiels.

Cette base peut servir de modèle pour développer les trois services suivants sans mélanger leurs connaissances ni leurs règles.

---

Dernière validation locale : 2 août 2026, environnement Windows, Java 21, Spring Boot 4, React/Vite et Ollama local.
