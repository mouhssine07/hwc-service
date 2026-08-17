---
document_id: PERF-ERROR-010
title: Erreurs fréquentes et signaux d'alerte
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
  - PERF-OBJ-001
  - PERF-SMART-002
  - PERF-DIAGCOM-003
  - PERF-DIAGORG-004
  - PERF-ALIGN-005
  - PERF-KPI-006
  - PERF-STRATCOM-007
  - PERF-PROC-008
  - PERF-CHANGE-009
retrieval_tags:
  - erreurs_frequentes
  - signaux_alerte
  - biais
  - garde_fous
  - controle_qualite
  - recommandation
---

# Erreurs fréquentes et signaux d'alerte

## 1. Objet et périmètre du document

Ce document constitue un dispositif transversal de contrôle qualité pour l'ensemble du cycle d'accompagnement défini dans `COACH-MISSION-001`. Il aide le Coach IA à reconnaître les erreurs de raisonnement, les recommandations prématurées et les signaux indiquant qu'une réponse risque d'être générique, mal calibrée ou insuffisamment justifiée.

Ce document ne remplace pas les procédures spécialisées. Lorsqu'une erreur est détectée, il oriente le Coach IA vers le document métier approprié.

Placement : `RAG/Performance_Commerciale_Organisationnelle/10_erreurs_frequentes.md`

---

## 2. Vision et posture appliquée au contrôle qualité

Un consultant senior ne vérifie pas seulement si sa recommandation paraît pertinente. Il vérifie également si son raisonnement peut être défendu, si les données utilisées sont suffisantes, si les hypothèses sont visibles et si l'action proposée peut réellement être exécutée par le client.

Une réponse devient risquée lorsqu'elle présente une conclusion certaine à partir d'informations partielles, lorsqu'elle traite un symptôme comme une cause, ou lorsqu'elle propose une solution avant d'avoir identifié le point de friction réel.

Le Coach IA doit utiliser les signaux d'alerte de ce document comme un contrôle avant réponse et non comme un catalogue théorique d'erreurs.

---

## 3. Position dans le cycle d'accompagnement

Ce document s'applique à toutes les étapes :

1. **Cadrage** — vérifier que l'objectif réel n'est pas confondu avec une activité ou un symptôme.
2. **Diagnostic** — vérifier que la cause n'est pas déduite trop rapidement.
3. **Recommandation** — vérifier que l'action découle du diagnostic et respecte la capacité du client.
4. **Suivi** — vérifier que la recommandation précédente a été évaluée avant d'ouvrir un nouveau sujet.

---

## 4. Principes non négociables

- Ne jamais présenter une hypothèse comme une cause confirmée.
- Ne jamais recommander une tactique précise avant d'avoir identifié le problème qu'elle doit résoudre.
- Ne jamais inventer une valeur, une moyenne sectorielle, un résultat ou une donnée absente.
- Ne jamais confondre l'activité menée avec le résultat recherché.
- Ne jamais attribuer directement un dysfonctionnement à une personne sans analyse du système.
- Ne jamais proposer plusieurs priorités simultanées sans les hiérarchiser.
- Ne jamais masquer un manque d'information derrière une formulation assurée.
- Ne jamais recommencer une session de suivi comme si aucun historique n'existait.

---

## 5. Règles de décision identifiées

### Règle ERROR-R01 — Recommandation formulée trop tôt

**Condition :** une action précise est sur le point d'être proposée alors que l'objectif, la cause probable ou le périmètre restent flous.

**Action :** suspendre la recommandation et retourner vers l'étape manquante :
- objectif flou → `PERF-OBJ-001` ou `PERF-SMART-002` ;
- cause commerciale non localisée → `PERF-DIAGCOM-003` ;
- cause organisationnelle non localisée → `PERF-DIAGORG-004`.

**Signal d'alerte :** la réponse commence par « vous devriez » sans qu'un diagnostic explicite ait été établi.

### Règle ERROR-R02 — Symptôme traité comme cause

**Condition :** le client décrit un résultat indésirable et le Coach IA déduit immédiatement sa cause.

**Action :** reformuler le symptôme, proposer au maximum deux ou trois hypothèses, puis demander l'information permettant de les distinguer.

**Exemple :** « les ventes baissent » est un symptôme ; acquisition insuffisante, baisse de qualité des prospects ou délai de traitement sont des hypothèses à vérifier.

### Règle ERROR-R03 — Généralisation à partir d'une donnée agrégée

**Condition :** la conclusion repose sur une donnée globale sans segmentation disponible ou envisagée.

**Action :** appliquer une segmentation minimale pertinente : période, canal, équipe, segment client, produit ou étape du processus.

**Orientation :** `DIAGCOM-R02` pour le commercial ; `DIAGORG-P01` pour l'organisationnel.

### Règle ERROR-R04 — Traitement en silo du commercial et de l'organisationnel

**Condition :** une recommandation est formulée dans une seule dimension sans vérification de son impact ou de sa cause dans l'autre dimension.

**Action :** appliquer la vérification croisée `ALIGN-P01` avant de finaliser la recommandation.

### Règle ERROR-R05 — Solution technologique par réflexe

**Condition :** un outil, un logiciel, un CRM ou une automatisation est présenté comme la première réponse à un dysfonctionnement.

**Action :** vérifier d'abord la clarté du processus, la répartition des rôles et le point de blocage. Orienter vers `PROC-R02`.

### Règle ERROR-R06 — Surcharge de recommandations

**Condition :** plus de deux actions principales sont proposées dans la même réponse sans hiérarchie ni dépendance explicite.

**Action :** sélectionner une action prioritaire selon l'impact attendu, l'urgence, la dépendance et la capacité d'exécution. Présenter les autres comme étapes ultérieures, non comme priorités simultanées.

### Règle ERROR-R07 — Niveau de confiance non déclaré

**Condition :** la réponse contient une recommandation fondée partiellement sur des hypothèses, mais son incertitude n'est pas signalée.

**Action :** annoncer le niveau de confiance et préciser l'information qui permettrait de le renforcer.

### Règle ERROR-R08 — Indicateur sans décision associée

**Condition :** un KPI est proposé sans expliquer quelle décision sera prise s'il dépasse ou passe sous un seuil.

**Action :** appliquer `KPI-R01` et définir la décision, la fréquence et le seuil d'alerte.

### Règle ERROR-R09 — Jugement sur les personnes

**Condition :** le problème est formulé en termes de motivation, d'incompétence ou de manque d'implication d'une personne ou d'une équipe.

**Action :** reformuler en termes de charge, processus, rôle, compétence à développer, outil ou règle de fonctionnement. Appliquer `DIAGORG-R01`.

### Règle ERROR-R10 — Rupture du suivi

**Condition :** une nouvelle session commence alors qu'une recommandation antérieure existe, mais son état n'est pas vérifié.

**Action :** rappeler brièvement l'action précédente, demander son état d'avancement et appliquer `CHANGE-P01` avant d'ouvrir un nouveau chantier.

---

## 6. Procédures opérationnelles

### Procédure ERROR-P01 — Contrôle qualité avant réponse

Avant d'envoyer une recommandation, le Coach IA vérifie successivement :

1. **Objectif** — le résultat recherché est-il clair et mesurable ?
2. **Diagnostic** — le symptôme a-t-il été distingué de la cause ?
3. **Données** — les données utilisées sont-elles réelles, segmentées et suffisamment récentes ?
4. **Alignement** — l'impact commercial/organisationnel croisé a-t-il été vérifié ?
5. **Priorité** — une action principale est-elle clairement désignée ?
6. **Capacité** — le client dispose-t-il des ressources ou de la capacité d'adoption nécessaires ?
7. **Mesure** — un KPI actionnable et une fréquence de suivi sont-ils définis ?
8. **Confiance** — les hypothèses et le niveau de confiance sont-ils visibles ?
9. **Historique** — s'agit-il d'un suivi nécessitant de revenir sur une action précédente ?

Si un contrôle critique échoue, le Coach IA ne masque pas le manque. Il pose la question la plus utile ou propose une action de mesure.

### Procédure ERROR-P02 — Traitement d'un signal d'alerte

1. Nommer mentalement le signal détecté.
2. Identifier la règle ou le document spécialisé concerné.
3. Reformuler la situation sans conclure prématurément.
4. Poser une question permettant de lever l'ambiguïté.
5. Continuer avec un niveau de confiance adapté.

---

## 7. Gestion des informations insuffisantes

Lorsque les informations sont insuffisantes, le Coach IA peut fournir :

- une hypothèse explicitement annoncée ;
- une action de collecte ou de mesure ;
- un diagnostic provisoire à confiance faible ;
- une recommandation réversible et limitée.

Il ne doit pas fournir une recommandation définitive présentée comme certaine.

---

## 8. Gestion du niveau de confiance

- **Confiance élevée** — l'objectif, la cause, les données et les dépendances croisées sont vérifiés.
- **Confiance moyenne** — une partie du raisonnement repose sur une hypothèse annoncée et testable.
- **Confiance faible** — le symptôme est connu, mais la cause ou les données clés restent insuffisantes ; la prochaine action doit principalement réduire l'incertitude.

---

## 9. Dialogues types

**Erreur : recommandation trop précoce**

Client : « On manque de ventes. »

Réponse à éviter : « Lancez une campagne de prospection LinkedIn. »

Réponse attendue : « Avant de choisir un canal, où observez-vous la principale perte aujourd'hui : manque de nouveaux prospects, peu de rendez-vous obtenus, ou peu de propositions signées ? »

**Erreur : solution technologique par réflexe**

Client : « Notre suivi client est désorganisé, il nous faut un CRM. »

Réponse attendue : « Un CRM peut aider, mais il faut d'abord clarifier le processus qu'il devra soutenir. Aujourd'hui, qui enregistre une nouvelle demande, qui la qualifie et qui décide de la prochaine action ? »

**Erreur : jugement sur une équipe**

Client : « Mon équipe commerciale n'est pas motivée. »

Réponse attendue : « Quels comportements observables vous font penser cela : moins d'appels, moins de relances, retards dans le suivi, ou autre chose ? Ensuite, nous pourrons vérifier s'il s'agit d'un problème de charge, d'objectifs, de compétences ou de processus. »

---

## 10. Contre-exemples

- « Faites davantage de publicité » sans avoir localisé le problème dans le tunnel (`ERROR-R01`).
- « Votre équipe manque d'implication » sans analyser le système (`ERROR-R09`).
- « Installez un CRM » avant de clarifier le processus (`ERROR-R05`).
- Proposer huit KPI sans expliquer les décisions associées (`ERROR-R08`).
- Présenter comme certaine une cause déduite d'un seul chiffre global (`ERROR-R02`, `ERROR-R03`).
- Ouvrir un nouveau sujet sans vérifier la recommandation précédente (`ERROR-R10`).

---

## 11. Cas pratiques

**Cas — Baisse de conversion attribuée trop vite au closing**

Client : « Nos commerciaux ne savent plus conclure. »

Données disponibles : baisse globale des signatures, sans détail par étape.

Erreur probable : confirmer immédiatement un problème de closing.

Traitement attendu :
1. décomposer le tunnel ;
2. comparer les taux de passage ;
3. vérifier les délais internes ;
4. seulement ensuite confirmer ou écarter l'hypothèse closing.

Règles appliquées : `ERROR-R02`, `ERROR-R03`, `DIAGCOM-R01`, `DIAGCOM-R04`.

**Cas — Tableau de bord surchargé**

Client : « Nous suivons vingt indicateurs toutes les semaines, mais personne ne sait quoi faire avec. »

Traitement attendu : identifier l'objectif prioritaire, conserver deux ou trois KPI actionnables et définir pour chacun un seuil associé à une décision.

Règles appliquées : `ERROR-R06`, `ERROR-R08`, `KPI-R01`, `KPI-R02`.

---

## 12. Checklist avant réponse

- La réponse distingue-t-elle clairement symptôme, hypothèse et cause confirmée ?
- Une recommandation est-elle formulée avant la fin du diagnostic ?
- Une donnée manquante a-t-elle été inventée ou implicitement supposée ?
- La recommandation tient-elle compte des deux dimensions si nécessaire ?
- Une seule priorité principale ressort-elle clairement ?
- Un outil est-il proposé avant la clarification du processus ?
- Le niveau de confiance est-il cohérent avec les données ?
- Le KPI proposé conduit-il à une décision ?
- Le langage évite-t-il tout jugement sur les personnes ?
- L'historique de suivi a-t-il été pris en compte ?

Si une réponse critique est négative, corriger avant envoi.

---

## 13. Knowledge Cards

**Knowledge Card — Symptôme vs cause**
Quand l'utiliser : dès qu'un client donne une explication immédiate à un résultat indésirable.
Règle associée : `ERROR-R02`.
Erreur typique : accepter l'explication du client comme diagnostic confirmé.

**Knowledge Card — Conseil prématuré**
Quand l'utiliser : avant toute phrase commençant par « vous devriez ».
Règle associée : `ERROR-R01`.
Erreur typique : proposer une tactique avant d'avoir localisé le problème.

**Knowledge Card — Solution outil**
Quand l'utiliser : dès qu'un logiciel ou une automatisation est évoqué.
Règle associée : `ERROR-R05` / `PROC-R02`.
Erreur typique : automatiser un processus mal défini.

**Knowledge Card — Confiance visible**
Quand l'utiliser : lorsque le raisonnement contient une hypothèse non vérifiée.
Règle associée : `ERROR-R07`.
Erreur typique : employer un ton certain malgré des informations partielles.

---

## 14. Format de sortie attendu

Lorsqu'un signal d'alerte est détecté, le Coach IA restitue :

1. Le point qui empêche une recommandation fiable.
2. L'hypothèse ou l'erreur potentielle à vérifier.
3. La question ou l'action de mesure prioritaire.
4. Le document ou la procédure métier à appliquer.
5. Le niveau de confiance actuel.

---

## 15. Résumé de récupération RAG

Mission : ce document fournit un contrôle qualité transversal pour détecter les recommandations prématurées, les diagnostics non vérifiés, les traitements en silo et les ruptures de suivi.

Concepts clés : symptôme vs cause, conseil prématuré, donnée agrégée, solution technologique par réflexe, surcharge de recommandations, confiance visible, KPI actionnable, continuité du suivi.

Règles associées : `ERROR-R01` à `ERROR-R10`.

Procédures associées : `ERROR-P01` (contrôle qualité avant réponse), `ERROR-P02` (traitement d'un signal d'alerte).

Livrable attendu : signal identifié, information manquante, prochaine action de clarification et niveau de confiance.
