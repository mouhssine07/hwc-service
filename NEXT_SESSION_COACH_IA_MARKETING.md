# Reprise prochaine session — Coach IA Marketing

## Mise à jour finale — 10 août 2026

Le service Marketing a été migré d'Ollama vers OpenAI pour le LLM et les embeddings :

```properties
openai.model=${OPENAI_MODEL:gpt-5.6-terra}
coach.marketing.rag.embedding-model=${COACH_MARKETING_EMBEDDING_MODEL:text-embedding-3-small}
```

La clé reste fournie uniquement par la variable d'environnement `OPENAI_API_KEY`. Le Coach utilise l'API Responses,
un effort de raisonnement `low`, une sortie JSON structurée et l'analyse multimodale d'images. Ollama n'est plus requis
par le nouveau module `coach/marketing`.

### Corrections de progression effectuées

Les boucles observées pendant le test manuel ont été corrigées avec une normalisation déterministe complémentaire au LLM :

- objectif chiffré et daté : création automatique du `smartStatement` et passage à l'audit ;
- cible : mémorisation séparée de `primaryNeed` et `mainObjection` ;
- canaux : reconnaissance et fusion d'Instagram, Facebook, WhatsApp, LinkedIn, TikTok, Google Ads,
  Google Business Profile, SEO, email, site web et partenariats ;
- plan d'action : mémorisation du responsable et passage aux KPI ;
- KPI : mémorisation de l'indicateur et passage à `FINAL_DELIVERABLE`.

Les réponses vagues, sociales, hors sujet ou formulées comme questions restent protégées et ne font pas avancer
artificiellement le workflow.

### Suivis persistants et reprise intelligente

Sous le bouton « Nouvelle stratégie », l'interface affiche maintenant « Suivis enregistrés ». Chaque suivi possède :

- un titre dérivé du produit/service et du nom de l'entreprise ;
- son `serviceId` ;
- l'identifiant du dernier diagnostic terminé associé lors de la création ;
- l'étape active ou le statut terminé ;
- tout l'historique de conversation.

Un suivi peut être rouvert. Après génération du livrable, le bouton « Continuer le suivi avec le Coach » active un
mode de coaching continu : exécution du plan, analyse des résultats, ajustement des actions et prochaine priorité,
sans modifier l'état finalisé.

### Images multimodales

Le client peut joindre une image JPEG, PNG ou WebP de 4 Mo maximum. L'image :

- est validée côté frontend et backend ;
- est transmise à `gpt-5.6-terra` avec le contexte de la stratégie ;
- est persistée avec le message ;
- est réaffichée lors de la reprise du suivi ;
- peut aussi être utilisée après la finalisation pour le suivi en temps réel.

### Suppression des suivis

Chaque suivi enregistré possède une icône corbeille. Après confirmation, le backend vérifie le propriétaire puis
supprime la session, ses messages, ses images et son livrable. Si la session active est supprimée, l'interface ouvre
le suivi restant le plus récent ou crée une nouvelle stratégie lorsqu'il n'en reste aucun.

### Schéma de base ajouté automatiquement

Avec `spring.jpa.hibernate.ddl-auto=update`, le redémarrage ajoute notamment :

```text
marketing_coach_sessions.diagnostic_id
marketing_coach_messages.image_data_url
marketing_coach_messages.image_name
```

### Dernière validation

```text
Backend : Tests run: 64, Failures: 0, Errors: 0, Skipped: 4
Frontend : npm.cmd run build — BUILD SUCCESS
```

Les quatre tests ignorés sont les intégrations réelles OpenAI optionnelles. Pour les lancer dans un terminal qui
possède la clé :

```powershell
$env:RUN_MARKETING_OPENAI_IT = "true"
mvn.cmd test
```

### Première tâche de la prochaine session

1. redémarrer le backend avec `OPENAI_API_KEY` disponible dans le processus ;
2. confirmer l'ingestion des 14 documents et des chunks via OpenAI embeddings ;
3. actualiser le frontend ;
4. tester la création, la finalisation et la réouverture d'un suivi ;
5. envoyer une image et vérifier sa réapparition après actualisation ;
6. continuer une stratégie finalisée ;
7. supprimer un suivi de test et vérifier la sélection automatique du suivant.

Ne pas indexer ni supprimer `hwc-frontend/src/coach-ia/` : ce dossier utilisateur reste réservé à un futur service.

---

## Instruction principale pour Codex

Lire ce fichier entièrement avant de modifier le projet.

Le développement porte uniquement sur le premier service du Coach IA :

```text
MARKETING_STRATEGY — Développer votre stratégie Marketing Digital
```

Ne pas intégrer les fichiers de performance commerciale et organisationnelle dans ce service. Les fichiers présents dans `hwc-frontend/src/coach-ia/` appartiennent à un futur service distinct.

## État exact au moment de l'arrêt

Une première implémentation complète du service Marketing a été créée, mais elle doit encore être testée manuellement dans l'application réelle et corrigée selon le comportement attendu par l'utilisateur.

Le dernier problème observé était :

```text
POST /api/client/coach/marketing/sessions/{sessionId}/messages
400 Bad Request
```

La cause identifiée était le RAG désactivé par défaut :

```properties
coach.marketing.rag.enabled=${COACH_MARKETING_RAG_ENABLED:false}
```

La valeur par défaut a été changée en `true` dans :

```text
src/main/resources/application.properties
```

Le backend doit être complètement redémarré pour appliquer cette modification. Si une variable d'environnement force encore `false`, lancer avec :

```powershell
$env:COACH_MARKETING_RAG_ENABLED = "true"
mvn.cmd spring-boot:run
```

Au démarrage, vérifier la présence du log :

```text
Marketing knowledge ingestion completed documents=14 chunks=44 serviceId=MARKETING_STRATEGY
```

## Dernière correction frontend

Une erreur React a été corrigée dans :

```text
hwc-frontend/src/pages/client/MarketingStrategyCoachPage.jsx
```

Ancien code incorrect :

```jsx
useEffect(() => endRef.current?.scrollIntoView(...), [messages]);
```

Ce code retournait implicitement la valeur de `scrollIntoView`, ce qui provoquait :

```text
useEffect must not return anything besides a function
destroy is not a function
```

Le hook utilise maintenant un bloc sans valeur retournée.

Le message `share-modal.js` ne correspond à aucun fichier du dépôt. Il semble être injecté par une extension du navigateur. Tester en navigation privée si nécessaire.

## Éléments déjà implémentés

### Ressources

```text
src/main/resources/coach-ia/global/
src/main/resources/coach-ia/services/marketing-strategy/
```

Contenu :

- 2 documents globaux ;
- 12 documents Marketing Digital ;
- `manifest.yml` ;
- 3 prompts ;
- template Markdown final ;
- 2 schémas JSON ;
- 10 scénarios métier.

### Backend

Module principal :

```text
src/main/java/hwc_backend/coach/marketing/
```

Éléments présents :

- ingestion Markdown ;
- métadonnées YAML ;
- embeddings Ollama ;
- vector store LangChain4j en mémoire ;
- filtre obligatoire `service_id=MARKETING_STRATEGY` ;
- état persistant de session ;
- historique des messages ;
- workflow par étapes ;
- prompts ;
- génération du livrable JSON et Markdown ;
- endpoints REST ;
- vérification du propriétaire de session.

### Frontend

Fichiers principaux :

```text
hwc-frontend/src/api/marketingCoachApi.js
hwc-frontend/src/pages/client/MarketingStrategyCoachPage.jsx
hwc-frontend/src/components/dashboard/ServicesRecommandes.jsx
hwc-frontend/src/pages/client/ClientProtectedRoute.jsx
hwc-frontend/src/App.jsx
```

Route :

```text
/client/coach/marketing
```

La carte envoie :

```json
{
  "serviceId": "MARKETING_STRATEGY"
}
```

## Endpoints créés

```http
POST /api/client/coach/marketing/sessions
POST /api/client/coach/marketing/sessions/{sessionId}/messages
GET  /api/client/coach/marketing/sessions/{sessionId}
POST /api/client/coach/marketing/sessions/{sessionId}/finalize
GET  /api/client/coach/marketing/sessions/{sessionId}/deliverable
```

## Workflow actuel

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

Le backend, et non le LLM, contrôle le changement d'étape.

## Ollama installé

Les modèles suivants sont présents localement :

```text
llama3.2:latest
nomic-embed-text
```

Un test RAG réel a réussi avec :

- 14 documents ;
- 44 chunks ;
- le cas `Restaurant local` classé premier ;
- score observé proche de `0.853654` ;
- tous les résultats filtrés sur `MARKETING_STRATEGY`.

Un test réel avec `llama3.2` a extrait correctement :

```json
{
  "sector": "Restauration",
  "productsOrServices": ["repas sur place", "à emporter"],
  "businessModel": "B2C",
  "location": "Casablanca",
  "size": "TPE"
}
```

## Tests au moment de l'arrêt

Dernière suite standard :

```text
Tests découverts : 50
Failures : 0
Errors : 0
Skipped : 2 tests Ollama optionnels
BUILD SUCCESS
```

Les deux tests Ollama optionnels ont également été exécutés séparément avec succès :

```text
MarketingRagOllamaIntegrationTests
MarketingLlmOllamaIntegrationTests
```

Le build frontend a réussi après la correction du `useEffect`.

## Important : changements non commités

Les changements de cette session ne sont pas encore commités ni poussés.

Ne pas supprimer ou écraser les fichiers non suivis. Vérifier d'abord :

```powershell
git status --short --branch
git diff --stat
```

Le fichier importé suivant appartient à l'utilisateur et doit être conservé :

```text
hwc-frontend/src/coach-ia/
```

## Rapport technique existant

Un rapport détaillé est disponible ici :

```text
COACH_IA_MARKETING_PRESENTATION.md
```

Il documente l'architecture et les résultats, mais l'utilisateur a indiqué que le comportement actuel n'est pas encore exactement celui qu'il souhaite. Ne pas supposer que l'expérience fonctionnelle est validée uniquement parce que les tests passent.

## Première tâche de la prochaine session

Procéder dans cet ordre :

1. lire ce fichier et `COACH_IA_MARKETING_PRESENTATION.md` ;
2. vérifier `git status` sans modifier les changements existants ;
3. vérifier `ollama list` ;
4. redémarrer réellement le backend avec le RAG actif ;
5. vérifier le log des 14 documents et 44 chunks ;
6. ouvrir `/client/coach/marketing` en navigation privée ;
7. créer une nouvelle session Marketing ;
8. envoyer la première réponse client ;
9. inspecter le corps HTTP exact si un `400` se reproduit ;
10. vérifier le comportement question par question avec l'utilisateur ;
11. corriger l'expérience réelle avant tout commit final.

## Points fonctionnels à valider avec l'utilisateur

- le texte exact de la première question ;
- la quantité d'informations acceptées dans une seule réponse ;
- la manière d'afficher les étapes ;
- la reprise de session après actualisation ;
- le moment exact où les canaux sont proposés ;
- le rendu visuel du livrable final ;
- la possibilité de recommencer volontairement une nouvelle session ;
- la conservation ou non de l'ancien Coach hebdomadaire ;
- la carte exacte qui doit ouvrir le Coach Marketing.

## Ne pas faire au début de la prochaine session

- ne pas ajouter le deuxième service ;
- ne pas indexer `hwc-frontend/src/coach-ia/` ;
- ne pas migrer immédiatement vers Qdrant ;
- ne pas créer un commit avant le test manuel demandé ;
- ne pas considérer `share-modal.js` comme un fichier du projet sans nouvelle preuve.

## État final de cette session

Le socle technique du premier service Marketing est présent et les tests automatisés passent. La prochaine session doit se concentrer sur le test manuel réel, la correction du `400` après redémarrage, puis l'ajustement précis du comportement et de l'interface selon les attentes de l'utilisateur.
