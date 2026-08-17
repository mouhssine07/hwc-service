---
document_id: PERF-STRATCOM-007
title: Stratégie commerciale (acquisition, prospection, closing)
domain: performance_commerciale_organisationnelle
scope:
  - performance_commerciale
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
  - PERF-ALIGN-005
retrieval_tags:
  - strategie_commerciale
  - prospection
  - closing
  - acquisition
  - recommandation
---

# Stratégie commerciale

## 1. Objet et périmètre du document

Ce document guide la formulation de la **Recommandation** (`COACH-MISSION-001`) pour la dimension commerciale, une fois le diagnostic du tunnel de vente réalisé (`PERF-DIAGCOM-003`) et la vérification croisée effectuée (`PERF-ALIGN-005`). Il ne traite jamais la recommandation commerciale comme une étape indépendante du diagnostic.

Placement : RAG/Performance_Commerciale_Organisationnelle/07_strategie_commerciale.md

---

## 2. Vision et posture appliquée à la recommandation commerciale

Un consultant senior ne recommande jamais une tactique commerciale par réflexe ou par mode (« faites plus de réseaux sociaux »). Chaque recommandation découle directement de l'étape du tunnel identifiée comme défaillante et du canal d'acquisition principal réellement utilisé par le client.

---

## 3. Position dans le cycle d'accompagnement

Ce document intervient à l'étape **Recommandation** du cycle (`COACH-MISSION-001`), pour la dimension commerciale, après diagnostic et vérification croisée.

---

## 4. Principes non négociables

- Ne jamais formuler de recommandation commerciale précise sans diagnostic préalable du tunnel (`PERF-DIAGCOM-003`).
- Ne jamais recommander une action générique déconnectée du canal d'acquisition principal du client.
- Ne jamais recommander une augmentation de volume sur un canal déjà saturé sans le signaler.

---

## 5. Règles de décision identifiées

### Règle STRATCOM-R01 — Recommandation avant diagnostic complet

**Condition :** une tactique commerciale précise est sur le point d'être recommandée sans que le diagnostic du tunnel (`DIAGCOM-P01`) ait été réalisé.

**Action :** interdiction stricte de formuler la recommandation ; renvoyer d'abord vers `PERF-DIAGCOM-003`.

### Règle STRATCOM-R02 — Action non adaptée au canal principal du client

**Condition :** la recommandation envisagée ne tient pas compte du canal d'acquisition principal déclaré par le client (réseau personnel, inbound, prospection directe, partenariats, recommandation client...).

**Action :** adapter la recommandation au canal réellement utilisé, plutôt que proposer un canal générique ou à la mode.

### Règle STRATCOM-R03 — Sur-sollicitation d'un canal déjà saturé

**Condition :** le canal recommandé montre des signes de saturation (taux de réponse en baisse, coût d'acquisition en hausse, prospects de moins bonne qualité).

**Action :** proposer une diversification ou une optimisation qualitative du canal existant plutôt qu'une simple augmentation de volume.

---

## 6. Procédures opérationnelles

### Procédure STRATCOM-P01 — Formulation d'une recommandation commerciale

1. Vérifier que le diagnostic `DIAGCOM-P01` est réalisé et que l'étape défaillante du tunnel est identifiée.
2. Vérifier l'alignement organisationnel (`ALIGN-P01`) si une contrainte de capacité a été identifiée.
3. Sélectionner un levier d'action ciblé précisément sur l'étape défaillante.
4. Vérifier la compatibilité de ce levier avec le canal principal du client et sa capacité actuelle.
5. Formuler la recommandation avec son niveau de confiance.

---

## 7. Gestion des informations insuffisantes

Si le canal principal du client n'est pas encore clairement identifié, le Coach IA le demande avant de recommander un levier, plutôt que de proposer une action générique valable pour tous les canaux.

---

## 8. Gestion du niveau de confiance

- **Confiance élevée** — le diagnostic du tunnel est complet, le canal principal est identifié, et l'alignement organisationnel a été vérifié.
- **Confiance moyenne** — le diagnostic est partiel mais une hypothèse claire a été formulée sur l'étape défaillante.
- **Confiance faible** — le diagnostic reste qualitatif, sans localisation précise de l'étape en difficulté.

---

## 9. Dialogues types

**Recommandation ciblée après diagnostic**

Coach IA (après diagnostic ayant localisé une perte entre le rendez-vous et la proposition) : « Le point de blocage se situe entre le rendez-vous et l'envoi de la proposition. Combien de temps s'écoule en moyenne entre les deux aujourd'hui ? Réduire ce délai est souvent le levier le plus direct à cette étape. »

**Signal de saturation d'un canal**

Client : « On veut faire encore plus de prospection téléphonique. »

Coach IA : « Avez-vous constaté une évolution du taux de réponse sur ce canal récemment ? Si le taux baisse malgré un volume stable ou croissant, il peut être plus efficace de diversifier ou d'ajuster le ciblage plutôt que d'augmenter encore le volume d'appels. »

---

## 10. Contre-exemples

- Recommander une technique de closing générique sans avoir localisé l'étape défaillante du tunnel (viole `STRATCOM-R01`).
- Recommander de développer un canal (ex. réseaux sociaux) alors que le client acquiert l'essentiel de ses clients par recommandation directe (viole `STRATCOM-R02`).
- Recommander plus de volume sur un canal dont les indicateurs montrent déjà une saturation (viole `STRATCOM-R03`).

---

## 11. Cas pratiques

**Cas — Recommandation ciblée sur le closing**

Diagnostic (`PERF-DIAGCOM-003`) : le taux de perte le plus élevé se situe entre la proposition et la signature.

Canal principal : recommandation client (bouche-à-oreille).

Recommandation : plutôt que d'investir dans l'acquisition, revoir le processus de suivi post-proposition (relance, réponse aux objections) puisque c'est à cette étape que se situe la perte.

Règle appliquée : `STRATCOM-P01`.

---

## 12. Checklist avant réponse

- Le diagnostic du tunnel est-il réalisé et l'étape défaillante identifiée (`STRATCOM-R01`) ?
- La recommandation tient-elle compte du canal principal réel du client (`STRATCOM-R02`) ?
- Le canal recommandé montre-t-il des signes de saturation à prendre en compte (`STRATCOM-R03`) ?
- L'alignement organisationnel a-t-il été vérifié si une contrainte de capacité existe (`PERF-ALIGN-005`) ?

---

## 13. Knowledge Cards

**Knowledge Card — Recommandation ciblée sur l'étape défaillante**
Quand l'utiliser : uniquement après diagnostic complet du tunnel.
Règle associée : `STRATCOM-R01`.
Erreur typique : recommander une tactique commerciale générique sans lien avec le diagnostic.

**Knowledge Card — Adéquation canal / recommandation**
Quand l'utiliser : à chaque recommandation touchant l'acquisition.
Règle associée : `STRATCOM-R02`.
Erreur typique : recommander un canal à la mode plutôt que le canal réellement utilisé par le client.

---

## 14. Format de sortie attendu

À l'issue de la formulation de la recommandation commerciale, le Coach IA restitue :

1. L'étape du tunnel ciblée par la recommandation.
2. Le levier d'action proposé, adapté au canal principal du client.
3. Le niveau de confiance de la recommandation.
4. La transition vers la construction du pilotage associé (`PERF-KPI-006`).

---

## 15. Résumé de récupération RAG

Mission : ce document structure la formulation d'une recommandation commerciale toujours reliée au diagnostic du tunnel et au canal d'acquisition réel du client.

Concepts clés : levier ciblé sur l'étape défaillante, adéquation au canal principal, saturation d'un canal.

Règles associées : `STRATCOM-R01` (recommandation prématurée), `STRATCOM-R02` (canal inadapté), `STRATCOM-R03` (sur-sollicitation d'un canal saturé).

Procédure associée : `STRATCOM-P01` (formulation d'une recommandation commerciale).

Livrable attendu : recommandation ciblée, adaptée au canal, avec niveau de confiance.
