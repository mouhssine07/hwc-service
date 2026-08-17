# Évolution du Coach IA Marketing — Spécification d'implémentation

## Contexte

Ce document décrit les évolutions à apporter au **Coach IA Marketing** (service `MARKETING_STRATEGY`) de la plateforme HWC. L'objectif est de faire passer l'expérience d'un chatbot conversationnel fonctionnel à un accompagnement moderne, fluide et confortable pour le client, **sans casser l'architecture existante**.

Ce document est destiné à être suivi étape par étape par un assistant de code. Chaque section est une unité de travail indépendante, avec objectif, comportement attendu, notes techniques et critères d'acceptation.

---

## Principes à respecter impérativement

Ces règles existantes ne doivent **jamais** être cassées par les évolutions ci-dessous :

- Le backend garde le contrôle du workflow — le LLM ne décide jamais seul de changer d'étape ni de terminer la stratégie.
- Le RAG reste strictement filtré sur `service_id = MARKETING_STRATEGY`.
- Le coach pose une seule question principale à la fois.
- Aucune information n'est inventée : toujours distinguer donnée client / hypothèse / recommandation.
- La propriété des sessions reste liée à l'utilisateur authentifié.
- L'historique complet (messages, images, état structuré, livrable) reste persistant et reprenable.

Toute évolution ci-dessous doit s'intégrer **par-dessus** ces garanties, pas à leur place.

---

## Phase 1 — Conversation plus vivante et humaine

### 1.1 Streaming des réponses

- **Objectif** : afficher la réponse du coach token par token plutôt qu'en bloc.
- **Comportement attendu** : le texte s'affiche progressivement dès que l'API renvoie des tokens, avec un curseur clignotant en fin de texte pendant la génération.
- **Notes techniques** : utiliser le streaming natif de l'API OpenAI (`stream: true`), relayer les chunks au frontend via SSE ou WebSocket. Ne pas streamer les appels de function calling / décisions backend, uniquement le texte conversationnel final.
- **Critères d'acceptation** :
  - [ ] Le texte apparaît progressivement dans l'UI.
  - [ ] Aucune régression sur la sauvegarde du message final en base.
  - [ ] Fonctionne aussi bien en création qu'en reprise de session.

### 1.2 Indicateur de statut contextuel

- **Objectif** : remplacer un simple "chargement..." par un message qui reflète ce qui se passe réellement.
- **Comportement attendu** : selon l'étape en cours, afficher un message différent pendant l'attente (ex. "Je compare avec des cas similaires dans votre secteur...", "J'analyse votre positionnement...").
- **Notes techniques** : mapper chaque étape du workflow (`Découverte`, `Objectifs`, `Audit`, etc.) à une liste de messages possibles, choisis aléatoirement pour éviter la répétition.
- **Critères d'acceptation** :
  - [ ] Chaque étape a au moins 2-3 messages de statut différents.
  - [ ] Le message est cohérent avec l'étape active en base.

### 1.3 Variation des formulations

- **Objectif** : éviter que le coach répète toujours les mêmes tournures ("Si je comprends bien...").
- **Comportement attendu** : plusieurs variantes de reformulation disponibles, choisies selon le contexte ou aléatoirement.
- **Notes techniques** : enrichir le prompt système avec une liste de formulations autorisées, ou laisser le LLM varier avec une consigne explicite ("ne réutilise pas la même formule de reformulation que dans les 2 derniers échanges").
- **Critères d'acceptation** :
  - [ ] Sur une session de 10+ échanges, pas de formulation de reformulation répétée plus de 2 fois d'affilée.

### 1.4 Ton adaptatif

- **Objectif** : ajuster légèrement le registre de langage selon le profil détecté (artisan, startup, e-commerce...).
- **Comportement attendu** : le ton reste professionnel mais peut être plus direct/technique ou plus pédagogique selon les premières réponses du client.
- **Notes techniques** : ajouter un champ `tone_profile` dans l'état de session, mis à jour après les 2-3 premiers échanges, injecté dans le prompt système.
- **Critères d'acceptation** :
  - [ ] Le `tone_profile` est déterminé et stocké après la phase de découverte.
  - [ ] Le prompt système en tient compte de façon mesurable.

---

## Phase 2 — Confiance et sentiment de contrôle

### 2.1 Panneau latéral "Stratégie en construction"

- **Objectif** : rendre visible en temps réel la construction de la stratégie.
- **Comportement attendu** : un panneau (desktop) ou une section dépliable (mobile) affiche les éléments déjà validés (objectif, cible, positionnement, canaux...) au fur et à mesure.
- **Notes techniques** : ce panneau lit directement l'état structuré de la stratégie en base (déjà existant), pas besoin de nouvelle source de vérité. Prévoir un état "en cours de définition" vs "validé".
- **Critères d'acceptation** :
  - [ ] Le panneau se met à jour sans rechargement de page après chaque étape validée par le backend.
  - [ ] Les champs non encore définis sont visuellement distincts (grisés/vides).

### 2.2 Édition directe des informations extraites

- **Objectif** : permettre au client de corriger une information mal comprise sans repasser par un échange texte complet.
- **Comportement attendu** : chaque élément affiché dans le panneau latéral est cliquable/éditable ; la correction met à jour l'état en base et informe le LLM au tour suivant.
- **Notes techniques** : ajouter un endpoint de correction manuelle qui modifie l'état structuré directement (bypass LLM), avec log de la modification pour traçabilité.
- **Critères d'acceptation** :
  - [ ] Une correction manuelle est bien prise en compte dans les échanges suivants.
  - [ ] La correction est historisée (qui, quand, ancienne/nouvelle valeur).

### 2.3 Distinction visuelle donnée / hypothèse / recommandation

- **Objectif** : rendre visible ce qui est déjà appliqué en texte (principe existant) mais pas encore visuellement exposé.
- **Comportement attendu** : badges ou couleurs distincts pour chaque type d'information dans le panneau et dans le fil de conversation.
- **Notes techniques** : le LLM doit déjà taguer ce type d'information en sortie (à vérifier/renforcer dans le prompt structuré) ; le frontend consomme ce tag pour l'affichage.
- **Critères d'acceptation** :
  - [ ] Trois styles visuels distincts et cohérents dans toute l'application.
  - [ ] Le tag est présent dans 100% des sorties structurées du LLM.

### 2.4 Barre de progression des 10 étapes

- **Objectif** : donner une vision claire de l'avancement global.
- **Comportement attendu** : une barre ou liste des 10 étapes du workflow, avec l'étape active mise en évidence et les étapes complétées cochées.
- **Notes techniques** : dérivée directement de l'étape active stockée en base — aucune nouvelle logique métier nécessaire, uniquement de l'affichage.
- **Critères d'acceptation** :
  - [ ] Reflète fidèlement l'étape stockée côté backend à tout moment.

---

## Phase 3 — Gamification légère (sans dénaturer le sérieux du service)

### 3.1 Micro-célébrations à la validation d'étape

- **Objectif** : marquer les étapes franchies sans tomber dans le superficiel.
- **Comportement attendu** : courte animation ou message discret ("Objectif défini ✓") à chaque changement d'étape validé par le backend.
- **Notes techniques** : déclenché côté frontend sur l'événement de changement d'étape reçu du backend.
- **Critères d'acceptation** :
  - [ ] Animation légère, non intrusive, désactivable.

### 3.2 Estimation du temps/effort restant

- **Objectif** : réduire l'incertitude sur la durée du parcours.
- **Comportement attendu** : message indicatif du type "encore environ 3 échanges avant votre plan d'action", basé sur l'étape actuelle.
- **Notes techniques** : mapping statique étape → nombre d'échanges restants estimé (peut être affiné avec des données réelles plus tard).
- **Critères d'acceptation** :
  - [ ] Estimation affichée et cohérente à chaque étape.

### 3.3 Aperçu progressif du livrable

- **Objectif** : donner une récompense visuelle continue plutôt qu'un document qui tombe d'un coup à la fin.
- **Comportement attendu** : les sections du livrable final (objectifs, canaux, plan d'action...) se dévoilent au fur et à mesure qu'elles sont validées.
- **Notes techniques** : réutilise le panneau latéral (2.1) comme base, avec une mise en forme qui se rapproche du livrable final.
- **Critères d'acceptation** :
  - [ ] Le livrable final ne contient aucune surprise par rapport à ce qui a été montré progressivement.

---

## Phase 4 — Personnalisation intelligente

### 4.1 Reconnaissance de reprise de session

- **Objectif** : accueillir différemment un client qui reprend une session en cours.
- **Comportement attendu** : message d'accueil contextuel rappelant où le client s'était arrêté, avec proposition claire de continuer.
- **Notes techniques** : déclenché à l'ouverture d'une session ayant un état > "Découverte", en lisant la dernière étape validée.
- **Critères d'acceptation** :
  - [ ] Le message de reprise mentionne correctement la dernière étape complétée.

### 4.2 Suggestions de réponses rapides (chips)

- **Objectif** : accélérer la saisie sans forcer une réponse fermée.
- **Comportement attendu** : sous le champ de saisie libre, proposer 2-4 réponses rapides pertinentes selon l'étape et le secteur détecté.
- **Notes techniques** : générées à partir de la bibliothèque RAG (cas pratiques par secteur), pas inventées par le LLM à la volée. Le client garde toujours la possibilité de répondre librement.
- **Critères d'acceptation** :
  - [ ] Les suggestions varient selon le secteur d'activité détecté.
  - [ ] Cliquer une suggestion produit le même résultat que la taper.

### 4.3 Profondeur adaptative des questions

- **Objectif** : aller plus vite avec un client expert, plus lentement avec un client vague.
- **Comportement attendu** : si les réponses sont détaillées, le coach peut regrouper des sous-questions ; si elles sont vagues, il clarifie davantage avant de continuer.
- **Notes techniques** : heuristique simple sur la longueur/densité d'information de la réponse, combinée à la logique existante de validation d'étape par le backend (le LLM propose, le backend valide toujours).
- **Critères d'acceptation** :
  - [ ] Le nombre d'échanges pour une étape varie de façon mesurable selon la richesse des réponses.

---

## Phase 5 — Multimodalité enrichie

### 5.1 Annotation visuelle des images envoyées

- **Objectif** : dépasser le retour en texte seul sur les images (publicités, posts, captures).
- **Comportement attendu** : le coach peut surligner/annoter des zones de l'image (ex. avec des overlays SVG) en plus du commentaire textuel.
- **Notes techniques** : nécessite que le LLM retourne des coordonnées ou zones d'intérêt en plus du texte (sortie structurée), le frontend superpose l'annotation sur l'image d'origine.
- **Critères d'acceptation** :
  - [ ] Fonctionne pour JPEG/PNG/WebP jusqu'à 4 Mo (limite existante conservée).
  - [ ] Dégradation propre en simple commentaire texte si l'annotation échoue.

### 5.2 Enrichissement par recherche web ciblée

- **Objectif** : réduire les questions redondantes en récupérant des informations publiques disponibles.
- **Comportement attendu** : si le client mentionne le nom de son entreprise, le coach peut vérifier sa présence en ligne actuelle avant de poser des questions déjà répondables.
- **Notes techniques** : function calling limité à une recherche web encadrée, résultat présenté comme hypothèse à valider par le client (pas comme fait établi), conformément au principe "ne jamais inventer".
- **Critères d'acceptation** :
  - [ ] Toute information trouvée en ligne est présentée comme "à confirmer" et non comme acquise.

### 5.3 Éléments visuels dans le livrable final

- **Objectif** : rendre le livrable final plus concret qu'un texte pur.
- **Comportement attendu** : intégrer par exemple un calendrier visuel de publication ou un exemple de post dans le document final.
- **Notes techniques** : génération de composants visuels simples (tableau, calendrier) à partir de l'état structuré déjà existant, pas de nouvelle IA générative d'image nécessaire dans un premier temps.
- **Critères d'acceptation** :
  - [ ] Le livrable final inclut au moins un élément visuel structuré (calendrier ou tableau d'actions).

---

## Phase 6 — Continuité et fidélisation post-stratégie

### 6.1 Check-ins proactifs programmés

- **Objectif** : dépasser le simple bouton "Continuer le suivi" par une relance active.
- **Comportement attendu** : une notification ou message est envoyé automatiquement environ une semaine après la génération du livrable pour faire un point.
- **Notes techniques** : tâche planifiée (cron / job scheduler) qui vérifie les stratégies sans activité récente et déclenche une notification.
- **Critères d'acceptation** :
  - [ ] Un client avec un livrable généré depuis 7+ jours sans activité reçoit une relance.
  - [ ] Le client peut désactiver ces relances.

### 6.2 Dashboard KPI visuel commenté

- **Objectif** : rendre le suivi des KPI plus parlant qu'une liste de chiffres.
- **Comportement attendu** : graphique de tendance sur les KPI choisis, avec un commentaire automatique du coach sur les évolutions notables.
- **Notes techniques** : les KPI et leurs valeurs dans le temps doivent être stockés en série temporelle (à vérifier/adapter dans le modèle de données actuel).
- **Critères d'acceptation** :
  - [ ] Historique des valeurs de KPI consultable sous forme de graphique.
  - [ ] Commentaire généré cohérent avec la tendance réelle des données.

### 6.3 Proposition proactive de la prochaine priorité

- **Objectif** : que le coach prenne l'initiative plutôt que d'attendre une question du client.
- **Comportement attendu** : à l'ouverture du suivi continu, le coach propose une priorité d'action basée sur les derniers résultats.
- **Notes techniques** : logique de règles simples au départ (ex. KPI en baisse → priorité sur ce canal), affinable ensuite avec le LLM en s'appuyant sur les données réelles.
- **Critères d'acceptation** :
  - [ ] Une priorité est systématiquement proposée à l'ouverture du suivi continu, jamais une page vide.

---

## Phase 7 — Fondations techniques transverses

### 7.1 RAG hybride avec reranking

- **Objectif** : améliorer la pertinence des passages retrouvés dans la bibliothèque marketing.
- **Comportement attendu** : combiner recherche par similarité vectorielle et recherche lexicale (BM25 ou équivalent), puis reranker les résultats avant de les injecter dans le prompt.
- **Notes techniques** : garder le filtre strict `service_id = MARKETING_STRATEGY` à toutes les étapes de la recherche.
- **Critères d'acceptation** :
  - [ ] Aucun résultat hors `MARKETING_STRATEGY` ne remonte.
  - [ ] Amélioration mesurable de la pertinence sur un jeu de questions test.

### 7.2 Détection légère de frustration/confusion

- **Objectif** : permettre au coach de ralentir ou reformuler si le client semble perdu.
- **Comportement attendu** : si les réponses du client deviennent courtes, répétitives ou expriment une incompréhension, le coach adapte son prochain message (reformulation plus simple, proposition d'exemple).
- **Notes techniques** : heuristique simple sur signaux textuels avant d'envisager un modèle dédié ; ne doit jamais bloquer la progression, seulement l'ajuster.
- **Critères d'acceptation** :
  - [ ] Un signal de confusion déclenche une reformulation simplifiée au tour suivant.

### 7.3 Citation des sources dans les recommandations

- **Objectif** : renforcer la transparence et la crédibilité des conseils donnés.
- **Comportement attendu** : quand une recommandation s'appuie sur la bibliothèque RAG, le coach peut le mentionner brièvement (ex. "d'après nos bonnes pratiques SEO pour l'immobilier").
- **Notes techniques** : le contexte RAG injecté doit conserver ses métadonnées de source (catégorie, secteur) pour permettre cette citation.
- **Critères d'acceptation** :
  - [ ] Les recommandations s'appuyant sur le RAG mentionnent leur origine de façon naturelle, sans surcharger le message.

---

## Ordre d'implémentation suggéré

1. **Phase 1** (conversation vivante) — impact immédiat sur le ressenti, faible risque architectural.
2. **Phase 2** (confiance/contrôle) — panneau latéral et barre de progression, forte valeur perçue.
3. **Phase 7.1 et 7.3** (RAG amélioré + citations) — renforce la qualité avant d'ajouter de la personnalisation dessus.
4. **Phase 4** (personnalisation) — s'appuie sur les fondations RAG déjà solidifiées.
5. **Phase 3** (gamification légère) — vient enrichir une base déjà fluide.
6. **Phase 5** (multimodalité enrichie) — plus complexe techniquement, à traiter une fois le cœur stabilisé.
7. **Phase 6** (continuité post-stratégie) — dernière étape, car dépend de données d'usage réelles accumulées.

Chaque phase peut être livrée et testée indépendamment sans casser les précédentes, dans la mesure où les principes de la section "Principes à respecter impérativement" sont toujours vérifiés.
