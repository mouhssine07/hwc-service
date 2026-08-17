---
document_id: PERF-ALIGN-005
title: Alignement commercial / organisationnel
domain: performance_commerciale_organisationnelle
scope:
  - performance_commerciale
  - performance_organisationnelle
document_type: manuel_raisonnement_decision
audience: coach_ia
version: 1.0
status: valide
language: fr
owner: equipe_produit
last_updated: 2026-08-02
related_documents:
  - COACH-MISSION-001
  - PERF-DIAGCOM-003
  - PERF-DIAGORG-004
retrieval_tags:
  - alignement
  - cause_croisee
  - priorisation
  - sequencage
---

# Alignement commercial / organisationnel

## 1. Objet et périmètre du document

Ce document est le point central de l'approche intégrée du Coach IA : il définit comment vérifier et exploiter les dépendances entre performance commerciale et performance organisationnelle avant de formuler une recommandation finale. Il s'applique après qu'un diagnostic commercial (`PERF-DIAGCOM-003`), un diagnostic organisationnel (`PERF-DIAGORG-004`), ou les deux, ont été réalisés.

Placement : RAG/Performance_Commerciale_Organisationnelle/05_alignement_commercial_organisationnel.md

---

## 2. Vision et posture appliquée à l'alignement

La grande majorité des recommandations mal calibrées viennent d'un traitement en silo : une action commerciale recommandée sans tenir compte d'une contrainte organisationnelle, ou une réorganisation recommandée sans mesurer son impact commercial. Un consultant senior vérifie systématiquement les dépendances entre les deux dimensions avant de finaliser une recommandation, même quand un seul des deux diagnostics semble concerné a priori.

---

## 3. Position dans le cycle d'accompagnement

Ce document intervient en fin de phase **Diagnostic**, comme étape de vérification croisée, juste avant la formulation de la **Recommandation** (`COACH-MISSION-001`).

---

## 4. Principes non négociables

- Ne jamais finaliser une recommandation purement commerciale sans avoir vérifié une dépendance organisationnelle possible, et inversement.
- Ne jamais recommander une action qui aggraverait une contrainte déjà identifiée dans l'autre dimension.
- Toujours soumettre au client, plutôt que trancher seul, une priorisation lorsque deux chantiers concurrents apparaissent.

---

## 5. Règles de décision identifiées

### Règle ALIGN-R01 — Symptôme commercial avec cause organisationnelle non vérifiée

**Condition :** un diagnostic commercial (`PERF-DIAGCOM-003`) a été conclu sans vérifier l'hypothèse organisationnelle correspondante (ex. délai de réponse interne, capacité de traitement).

**Action :** renvoyer vers `DIAGORG-P01` avant de finaliser une recommandation purement commerciale.

### Règle ALIGN-R02 — Symptôme organisationnel à impact commercial non quantifié

**Condition :** un diagnostic organisationnel (`PERF-DIAGORG-004`) identifie un dysfonctionnement, mais son impact sur la performance commerciale n'a pas été estimé.

**Action :** estimer ou demander au client une évaluation de cet impact (ex. nombre d'opportunités retardées ou perdues), afin de calibrer la priorité de l'action.

### Règle ALIGN-R03 — Recommandations potentiellement contradictoires

**Condition :** une action commerciale envisagée (par exemple augmenter le volume de prospects) risquerait d'aggraver une contrainte organisationnelle déjà identifiée (par exemple une équipe déjà saturée).

**Action :** signaler explicitement cette tension au client avant de recommander, et proposer un séquençage (ex. traiter la capacité avant d'augmenter le volume).

### Règle ALIGN-R04 — Priorisation entre un chantier commercial et un chantier organisationnel

**Condition :** les deux diagnostics révèlent un enjeu, mais les ressources du client ne permettent pas de traiter les deux simultanément.

**Action :** proposer un critère de priorisation explicite (impact estimé, urgence, dépendance entre les deux chantiers) et le soumettre au client plutôt que de trancher seul.

---

## 6. Procédures opérationnelles

### Procédure ALIGN-P01 — Vérification croisée avant recommandation finale

1. Reprendre le diagnostic commercial s'il existe (`PERF-DIAGCOM-003`).
2. Reprendre le diagnostic organisationnel s'il existe (`PERF-DIAGORG-004`).
3. Vérifier les dépendances : l'un des deux dysfonctionnements cause-t-il ou aggrave-t-il l'autre ?
4. Identifier la séquence d'action logique (ex. stabiliser la capacité interne avant d'augmenter le volume commercial).
5. Formuler une recommandation intégrée, avec sa priorité et son niveau de confiance.

---

## 7. Gestion des informations insuffisantes

Si un seul des deux diagnostics a pu être réalisé faute de temps ou d'information, le Coach IA ne bloque pas la recommandation : il la formule avec un niveau de confiance moyen ou faible selon le cas, et signale explicitement qu'une vérification de l'autre dimension reste à faire.

---

## 8. Gestion du niveau de confiance

- **Confiance élevée** — les deux diagnostics (commercial et organisationnel) sont disponibles et leurs dépendances ont été vérifiées.
- **Confiance moyenne** — un seul diagnostic est complet, l'autre repose sur une hypothèse annoncée.
- **Confiance faible** — aucune vérification croisée n'a pu être faite ; la recommandation ne porte que sur une seule dimension et le risque de tension avec l'autre n'est pas écarté.

---

## 9. Dialogues types

**Signalement d'une tension entre volume commercial et capacité organisationnelle**

Coach IA : « Augmenter le volume de prospects pourrait accentuer la surcharge que vous avez décrite sur le traitement des demandes. Souhaitez-vous d'abord stabiliser la capacité de traitement, ou préférez-vous absorber temporairement plus de volume malgré ce risque ? »

**Priorisation soumise au client**

Coach IA : « Deux chantiers ressortent : améliorer le taux de conversion commercial, et fluidifier la validation interne des offres. Le second semble ralentir le premier. Voulez-vous qu'on traite d'abord la validation interne, ou avez-vous une contrainte qui rend le commercial plus urgent malgré tout ? »

---

## 10. Contre-exemples

- Recommander une augmentation du volume de prospection sans vérifier la capacité de traitement de l'équipe (viole `ALIGN-R03`).
- Formuler une recommandation organisationnelle sans jamais estimer son impact sur le commercial (viole `ALIGN-R02`).
- Décider seul de la priorité entre deux chantiers sans la soumettre au client (viole `ALIGN-R04`).

---

## 11. Cas pratiques

**Cas — Tension volume / capacité**

Diagnostic commercial : le taux de conversion baisse malgré un volume de prospects en hausse (`PERF-DIAGCOM-003`).

Diagnostic organisationnel : le délai de traitement des demandes a doublé sur la même période (`PERF-DIAGORG-004`).

Analyse d'alignement : la hausse du volume commercial dépasse la capacité de traitement interne, ce qui dégrade la conversion. Recommander uniquement plus de volume aggraverait le problème.

Recommandation intégrée : stabiliser d'abord le délai de traitement, avant de chercher à augmenter davantage le volume de prospects.

Règle appliquée : `ALIGN-R03`.

---

## 12. Checklist avant réponse

- Le diagnostic commercial a-t-il été confronté à une hypothèse organisationnelle (`ALIGN-R01`) ?
- Le diagnostic organisationnel a-t-il été confronté à un impact commercial estimé (`ALIGN-R02`) ?
- La recommandation envisagée risque-t-elle d'aggraver une contrainte identifiée dans l'autre dimension (`ALIGN-R03`) ?
- Si deux chantiers concurrents existent, la priorisation a-t-elle été soumise au client plutôt que décidée seul (`ALIGN-R04`) ?

---

## 13. Knowledge Cards

**Knowledge Card — Dépendance croisée commercial/organisationnel**
Quand l'utiliser : avant toute finalisation de recommandation, quel que soit le diagnostic d'origine.
Règle associée : `ALIGN-R01` / `ALIGN-R02`.
Erreur typique : traiter les deux diagnostics comme indépendants.

**Knowledge Card — Séquençage capacité avant volume**
Quand l'utiliser : dès qu'une action commerciale d'augmentation de volume est envisagée en contexte de capacité limitée.
Règle associée : `ALIGN-R03`.
Erreur typique : recommander plus de volume à une organisation déjà saturée.

---

## 14. Format de sortie attendu

À l'issue de la vérification croisée, le Coach IA restitue :

1. Les dépendances identifiées entre les deux dimensions (le cas échéant).
2. La séquence d'action recommandée (quoi traiter en premier, et pourquoi).
3. Le niveau de confiance de cette recommandation intégrée.
4. Si une priorisation est nécessaire, la question soumise au client pour trancher.

---

## 15. Résumé de récupération RAG

Mission : ce document garantit qu'aucune recommandation commerciale ou organisationnelle n'est finalisée sans vérification des dépendances croisées entre les deux dimensions.

Concepts clés : dépendance croisée, tension volume/capacité, séquençage, priorisation soumise au client.

Règles associées : `ALIGN-R01` (cause organisationnelle non vérifiée), `ALIGN-R02` (impact commercial non quantifié), `ALIGN-R03` (recommandations contradictoires), `ALIGN-R04` (priorisation entre chantiers).

Procédure associée : `ALIGN-P01` (vérification croisée avant recommandation finale).

Livrable attendu : recommandation intégrée avec séquence d'action et niveau de confiance.
