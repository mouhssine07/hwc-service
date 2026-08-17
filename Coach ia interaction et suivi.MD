# Coach IA — refonte de l'interaction et du suivi post-stratégie

## Contexte

Ce document complète `coach-ia-marketing-evolution.md`. Il se concentre sur deux points précis identifiés à partir de l'interface actuelle (`/client/coach/marketing`) :

1. Pendant la **construction de la stratégie**, l'interaction reste un enchaînement classique question → réponse texte, même avec des chips de suggestion.
2. Après le livrable, le bouton **"Continuer le suivi avec le Coach"** rouvre le même chat linéaire, alors que la posture du client a changé : il exécute un plan, il ne se découvre plus.

Objectif : rendre l'interaction plus riche pendant la construction, et remplacer la réouverture de chat par un véritable espace de suivi après la stratégie.

## Principes à respecter impérativement

Identiques au document précédent — à ne jamais casser :

- Le backend garde le contrôle du workflow, jamais le LLM seul.
- Le RAG reste filtré sur `service_id = MARKETING_STRATEGY`.
- Une seule question principale à la fois pendant la construction.
- Distinction stricte donnée client / hypothèse / recommandation, y compris pour les données publiques vérifiées (toujours "à confirmer", jamais présentées comme acquises).
- Propriété des sessions liée à l'utilisateur authentifié.

---

## Partie A — Interaction plus riche pendant la construction

### A.1 Widgets adaptés au type de question

- **Objectif** : remplacer le texte libre par le bon composant selon la nature de l'information demandée, pas uniquement des chips de suggestion.
- **Comportement attendu** :
  - Budget / ressources → slider ou stepper numérique (montant, heures par semaine).
  - Cible → mini persona builder (tranche d'âge en slider, tags d'intérêts cliquables).
  - Canaux → cartes à cocher (logo + nom du canal) avec un score de pertinence affiché par le coach, le client garde/écarte au lieu de taper une phrase.
  - Positionnement → matrice 2x2 interactive (ex. prix vs différenciation) où le client place un point.
  - Le champ texte libre reste toujours disponible en dessous, comme filet de sécurité, jamais supprimé.
- **Notes techniques** : chaque étape du workflow doit exposer un `input_type` attendu (`slider`, `persona_builder`, `channel_picker`, `matrix_2x2`, `free_text`) déterminé par le backend selon l'étape active. Le frontend rend le composant correspondant ; la valeur saisie est envoyée au backend dans un format structuré (pas juste une string), qui la transmet ensuite au LLM pour reformulation/validation.
- **Critères d'acceptation** :
  - [ ] Chaque étape du workflow a un `input_type` défini, avec fallback `free_text` si aucun composant dédié n'existe encore.
  - [ ] La valeur structurée envoyée est bien lisible et confirmable par le LLM (pas de perte d'information par rapport au texte libre).

### A.2 Pré-remplissage depuis les données publiques

- **Objectif** : faire parler les données avant de faire parler le client, en s'appuyant sur la fonctionnalité "Vérifier la présence publique" déjà existante.
- **Comportement attendu** : à chaque étape où une donnée publique pertinente existe (followers, avis, fréquence de posts, présence sur annuaires), le coach pré-remplit le champ correspondant et demande confirmation au lieu de poser la question à froid. Exemple : "J'ai trouvé une page Instagram à 1200 abonnés, c'est bien la vôtre ?"
- **Notes techniques** : la vérification de présence publique doit pouvoir se déclencher automatiquement en amont de l'étape "Audit" (pas uniquement sur clic manuel), avec un délai raisonnable et une gestion d'échec silencieuse (si rien n'est trouvé, on repasse simplement à la question classique).
- **Critères d'acceptation** :
  - [x] Si une donnée publique fiable est trouvée avant l'étape Audit, elle est proposée en pré-remplissage avec confirmation requise.
  - [x] Aucune donnée publique n'est jamais injectée dans l'état de la stratégie sans confirmation explicite du client.

### A.3 Réactions rapides sur les propositions du coach

- **Objectif** : éviter que chaque ajustement nécessite une réponse texte complète.
- **Comportement attendu** : quand le coach propose une hypothèse ou une recommandation intermédiaire, des réactions rapides (type "correct" / "pas tout à fait" / "à revoir") sont disponibles à côté du message, en plus du texte libre.
- **Notes techniques** : chaque réaction rapide correspond à une action backend prédéfinie (valider l'hypothèse en l'état / demander une clarification / rouvrir l'étape), sans passer par une nouvelle interprétation LLM à chaque fois.
- **Critères d'acceptation** :
  - [ ] Une réaction rapide produit une action backend directe et immédiate, sans latence d'appel LLM supplémentaire pour les cas simples ("correct").

### A.4 Saisie vocale optionnelle

- **Objectif** : réduire la friction de saisie pour un public terrain (restaurateurs, gérants de commerce).
- **Comportement attendu** : un bouton micro permet de dicter la réponse ; le texte transcrit apparaît dans le champ avant envoi, modifiable avant validation.
- **Notes techniques** : transcription uniquement (pas de synthèse vocale des réponses du coach dans cette phase), avec possibilité de correction manuelle avant envoi obligatoire.
- **Critères d'acceptation** :
  - [ ] Le texte transcrit n'est jamais envoyé automatiquement sans validation du client.

---

## Partie B — Transformation du suivi post-stratégie

### B.1 Constat

Le bouton "Continuer le suivi avec le Coach" rouvre aujourd'hui le même chat linéaire qu'en phase de construction, avec le même panneau de progression. Or après le livrable, il n'y a plus d'étapes à progresser : le panneau de progression n'a plus de sens, et remettre le client en mode conversation reproduit une posture de découverte alors qu'il est en phase d'exécution.

### B.2 Nouvel espace de suivi (dashboard-first)

- **Objectif** : remplacer la réouverture de chat par un tableau de bord d'exécution, où le chat devient secondaire.
- **Comportement attendu** : après génération du livrable, le bouton mène vers un espace structuré en quatre blocs :
  1. **Priorité proposée** — mise en avant, générée par le coach à partir des derniers résultats, visible sans action du client.
  2. **Plan d'action en checklist** — chaque action du plan a une case à cocher, une échéance, éventuellement une preuve à joindre (ex. capture d'une fiche mise à jour).
  3. **Suivi des KPI** — formulaire de saisie de valeur, avec commentaire ponctuel du coach après chaque relevé (pas un dialogue complet).
  4. **Accès rapide au coach** — un point d'entrée léger pour une question ponctuelle, pas le format plein écran de la phase de construction.
- **Notes techniques** :
  - Le plan d'action doit être stocké comme une liste d'items structurés (titre, échéance, statut, preuve optionnelle) dérivée du livrable final, pas comme du texte brut.
  - Les KPI doivent être stockés en série temporelle (valeur + date) pour permettre un historique et une tendance, pas juste la dernière valeur.
  - Le panneau de progression à 10 étapes ne doit plus s'afficher dans cet espace — il est remplacé par les blocs ci-dessus.
- **Critères d'acceptation** :
  - [x] Un client qui clique sur "Continuer le suivi" arrive sur ce nouvel espace, pas sur le chat de construction.
  - [x] Cocher une action du plan met à jour son statut en base immédiatement.
  - [x] Un relevé KPI ajouté déclenche un commentaire du coach basé sur la valeur précédente (tendance), sans ouvrir de conversation complète.

### B.3 Coach proactif sans clic préalable

- **Objectif** : que la priorité proposée n'exige pas que le client ouvre le suivi pour la découvrir.
- **Comportement attendu** : la priorité proposée par le coach est visible directement sur le tableau de bord principal du client (hors de l'espace de suivi dédié), ou déclenche une notification.
- **Notes techniques** : recalcul de la priorité proposée déclenché après chaque nouveau relevé KPI ou après une période sans activité (ex. 7 jours), pas uniquement à l'ouverture manuelle du suivi.
- **Critères d'acceptation** :
  - [x] La priorité affichée sur le tableau de bord principal est toujours cohérente avec le dernier état du suivi, sans nécessiter d'ouverture manuelle de l'espace de suivi.

### B.4 Chat secondaire contextuel

- **Objectif** : garder le chat disponible pour les questions ponctuelles sans qu'il redevienne le point d'entrée principal.
- **Comportement attendu** : un point d'accès chat (bulle ou barre légère) reste accessible en permanence dans l'espace de suivi, mais s'ouvre en superposition légère plutôt qu'en plein écran avec panneau de progression.
- **Notes techniques** : ce chat contextuel doit avoir accès à l'état complet du suivi (plan d'action, KPI, priorité en cours) pour répondre avec le bon contexte, sans redémarrer une session de découverte.
- **Critères d'acceptation** :
  - [x] Une question posée via ce chat secondaire obtient une réponse contextualisée avec l'état réel du suivi, sans redemander des informations déjà connues (entreprise, objectif, etc.).

---

## Ordre d'implémentation suggéré

1. **B.2** (nouvel espace de suivi dashboard-first) — le changement structurel le plus important, résout directement le problème signalé.
2. **B.1 → B.4** (chat secondaire contextuel) — vient compléter l'espace de suivi une fois sa structure en place.
3. **B.3** (proactivité sans clic) — s'appuie sur les données déjà collectées dans le nouvel espace de suivi.
4. **A.2** (pré-remplissage données publiques) — réutilise la vérification de présence publique déjà existante, impact rapide sur la phase de construction.
5. **A.1** (widgets adaptés) — plus gros chantier frontend, à traiter une fois les fondations de données (input_type par étape) définies.
6. **A.3** (réactions rapides) — vient enrichir les widgets une fois en place.
7. **A.4** (saisie vocale) — amélioration de confort, non bloquante, à traiter en dernier.

Chaque point peut être livré indépendamment, à condition de toujours vérifier les principes de la section "Principes à respecter impérativement".

---

## État d'avancement — mise à jour du 16 août 2026

### Étapes terminées

- [x] **B.2 — Nouvel espace de suivi dashboard-first**
  - plan d'action structuré et persisté en base ;
  - statut, échéance et référence de preuve modifiables ;
  - mesures KPI conservées en série temporelle avec tendance ;
  - panneau des 10 étapes masqué pendant le suivi ;
  - initialisation compatible avec les stratégies déjà finalisées.
- [x] **B.4 — Chat secondaire contextuel**
  - ouverture en superposition légère depuis le dashboard de suivi ;
  - contexte enrichi avec le plan, les statuts, les échéances, les KPI et la priorité ;
  - conservation du RAG Marketing et de la propriété utilisateur.
- [x] **B.3 — Coach proactif sans clic préalable**
  - priorité visible sur le dashboard principal du client ;
  - actualisation après une mesure KPI ou une modification d'action ;
  - priorité adaptée après sept jours sans activité client réelle.
- [x] **A.2 — Pré-remplissage depuis les données publiques**
  - recherche automatique avant l'étape Audit avec échec silencieux ;
  - propositions limitées aux chemins d'audit autorisés ;
  - confirmation ou rejet explicite côté client ;
  - aucune proposition publique intégrée dans l'audit sans confirmation.

### Étapes restantes

- [x] **A.1 — Widgets adaptés au type de question**
  - contrat backend `input-spec` avec fallback texte libre ;
  - slider ressources, persona builder, sélection de canaux et matrice 2x2 ;
  - valeur structurée transmise avec un résumé modifiable avant envoi.
- [x] **A.3 — Réactions rapides sur les propositions du coach**
  - actions `CORRECT`, `CLARIFY` et `REVISE` traitées directement par le backend sans appel LLM.
- [x] **A.4 — Saisie vocale optionnelle**
  - dictée navigateur en français, transcription modifiable et envoi uniquement par validation explicite.

### Validation technique

- Backend Maven : compilation réussie.
- Frontend Vite : build de production réussi.
- Tests backend : **80 réussis, 0 échec, 4 ignorés**.
