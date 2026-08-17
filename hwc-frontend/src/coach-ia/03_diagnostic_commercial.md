---
document_id: PERF-DIAGCOM-003
title: Diagnostic de la performance commerciale
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
  - PERF-OBJ-001
  - PERF-SMART-002
retrieval_tags:
  - diagnostic
  - tunnel_de_vente
  - conversion
  - acquisition
  - cycle_de_vente
---

# Diagnostic de la performance commerciale

## 1. Objet et périmètre du document

Ce document guide la phase de **Diagnostic** (`COACH-MISSION-001`) lorsque l'objectif validé (`PERF-SMART-002`) concerne le volet commercial. Il s'applique après la phase de Cadrage, une fois l'objectif rendu mesurable.

Placement : RAG/Performance_Commerciale_Organisationnelle/03_diagnostic_commercial.md

---

## 2. Vision et posture appliquée au diagnostic commercial

Un consultant ne dit jamais « vos ventes sont insuffisantes » sans localiser précisément où, dans le parcours du prospect au client, la performance décroche. La performance commerciale se décompose toujours en étapes : acquisition, qualification, proposition, closing, fidélisation. Le diagnostic consiste à identifier l'étape où la déperdition est anormale, pas à traiter le tunnel comme un bloc unique.

---

## 3. Position dans le cycle d'accompagnement

Ce document intervient à l'étape **Diagnostic** du cycle défini dans `COACH-MISSION-001`, en aval de la validation SMART de l'objectif commercial.

---

## 4. Principes non négociables

- Ne jamais diagnostiquer un problème commercial global sans le décomposer par étape du tunnel.
- Ne jamais conclure à un problème de compétence commerciale avant d'avoir écarté une cause organisationnelle (cf. `MISSION-R02`).
- Ne jamais fonder un diagnostic sur un seul indicateur agrégé si une segmentation est possible.

---

## 5. Règles de décision identifiées

### Règle DIAGCOM-R01 — Baisse de performance mal localisée

**Condition :** le client rapporte une baisse de ses ventes sans préciser à quelle étape du tunnel elle se produit.

**Action :** décomposer le tunnel (acquisition → qualification → proposition → closing → fidélisation) et identifier, avec le client ou à partir des données disponibles, l'étape où le taux de perte est le plus élevé.

### Règle DIAGCOM-R02 — Diagnostic fondé sur un indicateur unique et agrégé

**Condition :** seule une donnée globale est disponible (ex. chiffre d'affaires total), sans détail par canal, par commercial ou par segment.

**Action :** proposer une segmentation minimale avant de formuler une conclusion, ou signaler explicitement que le diagnostic reste à confiance faible tant que cette segmentation n'est pas faite.

### Règle DIAGCOM-R03 — Confusion entre volume et qualité des prospects

**Condition :** une baisse du taux de conversion est évoquée.

**Action :** distinguer si la cause probable est un volume de prospects insuffisant, ou une qualité/adéquation insuffisante des prospects générés, avant de recommander une action.

### Règle DIAGCOM-R04 — Symptôme commercial à cause potentiellement organisationnelle

**Condition :** le ralentissement se situe entre deux étapes internes du processus (ex. délai de réponse à une demande, délai de validation d'une offre).

**Action :** appliquer `MISSION-R02` — vérifier l'hypothèse organisationnelle avant de conclure à un problème de discours ou de compétence commerciale. Le cas échéant, orienter vers `PERF-DIAGORG-004`.

### Règle DIAGCOM-R05 — Cycle de vente anormalement long

**Condition :** le cycle de vente est significativement plus long que la moyenne historique ou déclarée du client.

**Action :** identifier, si possible avec des données, à quelle étape précise le cycle s'allonge, plutôt que de supposer une cause générale (ex. « le marché est plus difficile »).

---

## 6. Procédures opérationnelles

### Procédure DIAGCOM-P01 — Diagnostic du tunnel commercial

1. Cartographier les étapes du tunnel commercial du client (elles peuvent varier selon le secteur).
2. Recueillir, pour chaque étape disponible, un indicateur de passage (taux de conversion d'une étape à l'autre).
3. Identifier l'étape présentant le taux de perte le plus élevé ou le plus anormal.
4. Formuler une ou plusieurs hypothèses de cause pour cette étape, en distinguant cause commerciale directe et cause organisationnelle (`DIAGCOM-R04`).
5. Proposer une action ciblée sur l'étape identifiée, avec son niveau de confiance (section 8).

---

## 7. Gestion des informations insuffisantes

Si le client ne dispose pas d'un tunnel structuré ou de données par étape, le Coach IA ne renonce pas au diagnostic : il propose une reconstitution approximative à partir des informations qualitatives disponibles (« où avez-vous le sentiment de perdre le plus de prospects ? ») et recommande, en parallèle, la mise en place d'un suivi minimal par étape pour affiner le diagnostic lors du prochain échange.

---

## 8. Gestion du niveau de confiance

- **Confiance élevée** — le tunnel est cartographié avec des indicateurs par étape, permettant d'isoler clairement l'étape en difficulté.
- **Confiance moyenne** — seules des données partielles ou qualitatives sont disponibles, complétées par des hypothèses annoncées.
- **Confiance faible** — seul un ressenti global est disponible, sans détail par étape.

---

## 9. Dialogues types

**Décomposition du tunnel**

Client : « Nos ventes ont baissé de 20 % ce trimestre. »

Coach IA : « Pour situer précisément la baisse : avez-vous constaté moins de nouveaux prospects entrants, moins de rendez-vous transformés en propositions, ou moins de propositions transformées en signatures ? »

**Distinction volume / qualité des prospects**

Client : « On a autant de prospects qu'avant, mais on convertit moins. »

Coach IA : « Est-ce que ces prospects viennent des mêmes sources qu'avant, ou une partie provient de nouveaux canaux ? Une baisse de conversion à volume égal vient souvent d'un changement dans la qualité ou l'adéquation des prospects. »

---

## 10. Contre-exemples

- Recommander une amélioration générique du discours commercial sans avoir localisé l'étape du tunnel en difficulté (viole `DIAGCOM-R01`).
- Conclure à partir du seul chiffre d'affaires global sans chercher à le segmenter (viole `DIAGCOM-R02`).
- Attribuer une baisse de conversion à un manque de compétence commerciale sans avoir vérifié une cause organisationnelle possible (viole `DIAGCOM-R04`).

---

## 11. Cas pratiques

**Cas — Localisation d'une baisse de conversion**

Client : « On a de plus en plus de demandes entrantes mais on ne signe pas plus. »

Diagnostic : le volume d'acquisition augmente, mais la conversion stagne — le problème se situe donc en aval de l'acquisition (qualification, proposition ou closing), pas sur l'acquisition elle-même.

Réponse attendue : « Le nombre de demandes augmente mais pas les signatures : le point de blocage se situe après l'acquisition. Combien de ces demandes aboutissent à un rendez-vous qualifié, et combien de rendez-vous aboutissent à une proposition ? »

Règle appliquée : `DIAGCOM-R01`, `DIAGCOM-R03`.

---

## 12. Checklist avant réponse

- Le tunnel commercial a-t-il été décomposé par étape (`DIAGCOM-R01`) ?
- Le diagnostic repose-t-il sur un indicateur segmenté ou uniquement agrégé (`DIAGCOM-R02`) ?
- La distinction volume/qualité des prospects a-t-elle été faite si pertinente (`DIAGCOM-R03`) ?
- Une cause organisationnelle a-t-elle été envisagée avant de conclure à une cause commerciale directe (`DIAGCOM-R04`) ?
- La longueur du cycle de vente a-t-elle été comparée à une référence (`DIAGCOM-R05`) ?

---

## 13. Knowledge Cards

**Knowledge Card — Décomposition du tunnel commercial**
Quand l'utiliser : dès qu'une baisse de performance commerciale globale est évoquée.
Règle associée : `DIAGCOM-R01`.
Erreur typique : traiter le tunnel comme un bloc unique.

**Knowledge Card — Volume vs qualité des prospects**
Quand l'utiliser : dès qu'une baisse du taux de conversion est évoquée.
Règle associée : `DIAGCOM-R03`.
Erreur typique : recommander plus de volume alors que le problème est la qualité.

**Knowledge Card — Cause organisationnelle sous un symptôme commercial**
Quand l'utiliser : dès qu'un délai ou une friction interne est mentionné dans le tunnel.
Règle associée : `DIAGCOM-R04` / `MISSION-R02`.
Erreur typique : traiter un problème de process comme un problème de compétence commerciale.

---

## 14. Format de sortie attendu

À l'issue du diagnostic commercial, le Coach IA restitue :

1. L'étape du tunnel identifiée comme la plus en difficulté.
2. La ou les hypothèses de cause (commerciale directe, organisationnelle, ou les deux).
3. Le niveau de confiance du diagnostic (section 8).
4. La transition vers une recommandation ciblée (`PERF-STRATCOM-007`) ou, si la cause est organisationnelle, vers `PERF-DIAGORG-004`.

---

## 15. Résumé de récupération RAG

Mission : ce document structure le diagnostic de la performance commerciale par décomposition du tunnel de vente, en distinguant causes commerciales directes et causes organisationnelles.

Concepts clés : tunnel de vente, segmentation des indicateurs, volume vs qualité des prospects, cycle de vente, cause croisée.

Règles associées : `DIAGCOM-R01` à `DIAGCOM-R05`.

Procédure associée : `DIAGCOM-P01` (diagnostic du tunnel commercial).

Livrable attendu : diagnostic localisé avec hypothèses de cause et niveau de confiance.
