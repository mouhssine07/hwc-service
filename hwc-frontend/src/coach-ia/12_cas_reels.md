---
document_id: PERF-CASE-012
title: Cas réels types et scénarios intégrés
domain: performance_commerciale_organisationnelle
scope:
  - performance_commerciale
  - performance_organisationnelle
document_type: corpus_cas_pratiques_raisonnement_decision
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
  - PERF-ERROR-010
  - PERF-METHOD-011
retrieval_tags:
  - cas_reels
  - cas_types
  - scenarios
  - diagnostic
  - recommandation
  - suivi
  - performance_integree
---

# Cas réels types et scénarios intégrés

## 1. Objet et périmètre du document

Ce document regroupe des scénarios réalistes et anonymisés permettant au Coach IA d'appliquer l'ensemble de la méthode : cadrage, validation SMART, diagnostic commercial et organisationnel, alignement, recommandation, KPI, adoption et suivi.

Les cas présentés sont des **cas types composites**. Ils illustrent des situations professionnelles fréquentes sans prétendre décrire une entreprise réelle identifiable. Le Coach IA ne doit jamais copier mécaniquement une solution d'un cas vers un client ; il doit comparer les conditions, les données et les contraintes.

Placement : `RAG/Performance_Commerciale_Organisationnelle/12_cas_reels.md`

---

## 2. Règles d'utilisation des cas

- Utiliser un cas comme analogie, jamais comme preuve.
- Vérifier que le secteur, le modèle économique, le cycle de vente et la capacité du client sont comparables.
- Ne jamais appliquer la recommandation finale d'un cas sans refaire le diagnostic.
- Extraire surtout le raisonnement : symptôme → données → hypothèses → diagnostic → priorité → KPI → suivi.
- Signaler les différences importantes entre le cas de référence et la situation du client.
- Maintenir le niveau de confiance adapté aux données réelles du client.

---

## 3. Structure standard d'un cas

Chaque cas contient :

1. Contexte
2. Demande initiale
3. Données disponibles
4. Erreur à éviter
5. Cadrage
6. Diagnostic
7. Vérification croisée
8. Recommandation prioritaire
9. KPI
10. Risque d'adoption
11. Suivi
12. Règles et procédures appliquées

---

# Cas 1 — Agence B2B : croissance des demandes sans croissance des signatures

## Contexte

Une agence de services B2B de 12 personnes génère davantage de demandes grâce à des partenariats. Le chiffre d'affaires stagne depuis quatre mois.

## Demande initiale

Client : « Nous devons trouver encore plus de prospects. »

## Données disponibles

- Demandes entrantes : 45 → 70 par mois.
- Rendez-vous qualifiés : 30 → 42 par mois.
- Propositions envoyées : 24 → 25 par mois.
- Signatures : 10 → 9 par mois.
- Délai moyen rendez-vous → proposition : 2 jours → 7 jours.
- Toutes les propositions doivent être validées par le dirigeant.

## Erreur à éviter

Recommander davantage d'acquisition alors que le volume entrant a déjà augmenté.

## Cadrage

Objectif reformulé : augmenter les signatures mensuelles de 9 à 13 sous trois mois, sans augmenter immédiatement le volume de demandes.

Niveau de confiance : élevé.

## Diagnostic

Le principal décrochage se situe entre le rendez-vous et la proposition. Le délai s'est allongé en raison d'une validation centralisée.

Diagnostic commercial : perte dans le tunnel après qualification.

Diagnostic organisationnel : goulot d'étranglement décisionnel.

## Vérification croisée

Une nouvelle hausse du volume aggraverait la saturation. La capacité interne doit être stabilisée avant toute nouvelle action d'acquisition.

## Recommandation prioritaire

Définir trois catégories de propositions :
1. offres standard validables sans le dirigeant ;
2. offres nécessitant une validation rapide ;
3. offres exceptionnelles nécessitant une validation complète.

## KPI

- Résultat : taux de conversion proposition → signature.
- Avancé : délai rendez-vous → proposition.
- Seuil : alerte si plus de 20 % des propositions standard dépassent 48 heures.
- Fréquence : hebdomadaire.

## Risque d'adoption

Le dirigeant peut craindre une perte de contrôle. Commencer par un pilote sur les offres standard pendant deux semaines.

## Suivi attendu

Vérifier la part d'offres envoyées sous 48 heures et l'évolution des signatures.

## Règles appliquées

`OBJ-R01`, `SMART-P01`, `DIAGCOM-R01`, `DIAGORG-R03`, `ALIGN-R03`, `PROC-R03`, `KPI-P01`, `CHANGE-R03`.

---

# Cas 2 — E-commerce : plus de trafic, moins de conversion et surcharge du service client

## Contexte

Une boutique e-commerce augmente fortement son budget publicitaire. Le trafic progresse, mais le taux de conversion baisse et les avis clients se dégradent.

## Demande initiale

Client : « Nos publicités ne fonctionnent plus. »

## Données disponibles

- Trafic : +55 %.
- Ajouts au panier : +42 %.
- Commandes : +8 %.
- Délai de réponse du service client : 4 h → 28 h.
- Taux de rupture sur les produits promus : 3 % → 18 %.
- Annulations : +30 %.

## Erreur à éviter

Conclure à une mauvaise création publicitaire sans examiner la capacité opérationnelle et la disponibilité des produits.

## Cadrage

Objectif : rétablir le taux de conversion et réduire les annulations dans les huit semaines.

## Diagnostic

Les publicités génèrent du trafic et des intentions d'achat. La perte intervient au moment de la disponibilité produit et du traitement client.

## Vérification croisée

Le problème commercial visible est principalement aggravé par une contrainte organisationnelle : stock et capacité de réponse.

## Recommandation prioritaire

Limiter temporairement les campagnes aux produits dont le stock est fiable et mettre en place une règle d'alerte lorsque la couverture de stock passe sous un seuil défini.

## KPI

- Résultat : taux de conversion commande.
- Avancés : taux de rupture des produits promus ; délai moyen de réponse.
- Seuil : retirer un produit des campagnes lorsque le stock prévu est insuffisant pour sept jours de ventes.
- Fréquence : quotidienne pour le stock, hebdomadaire pour la conversion.

## Risque d'adoption

L'équipe marketing peut percevoir la limitation des campagnes comme un recul. Expliquer que l'objectif est de protéger la conversion et les avis avant de réaccélérer.

## Suivi attendu

Comparer les performances des produits avec stock fiable aux produits précédemment promus sans contrôle.

## Règles appliquées

`ERROR-R02`, `DIAGCOM-R03`, `DIAGORG-P01`, `ALIGN-R03`, `KPI-R03`, `CHANGE-R01`.

---

# Cas 3 — Cabinet de conseil : dépendance excessive au dirigeant

## Contexte

Un cabinet de 20 consultants connaît une croissance rapide. Les décisions commerciales, la validation des offres et l'affectation des ressources passent toutes par le fondateur.

## Demande initiale

Client : « Mon équipe n'est pas assez autonome. »

## Données disponibles

- 70 % des propositions attendent une validation du fondateur.
- Délai moyen de validation : 5 jours.
- Deux chefs de mission disposent de l'expérience nécessaire, mais aucune règle de délégation n'existe.
- Le fondateur travaille régulièrement le soir pour débloquer les dossiers.

## Erreur à éviter

Valider le jugement « manque d'autonomie » ou recommander une délégation générale sans préciser les décisions.

## Cadrage

Objectif : réduire de 5 à 2 jours le délai moyen de validation sous six semaines.

## Diagnostic

Le frein est moins un manque d'autonomie qu'une absence de règles de décision et de seuils de délégation.

## Vérification croisée

Le goulot organisationnel ralentit directement la transformation commerciale.

## Recommandation prioritaire

Créer une matrice simple :
- décisions déléguées ;
- décisions nécessitant une consultation ;
- décisions réservées au fondateur.

Tester la matrice sur un type d'offre pendant trois semaines.

## KPI

- Résultat : délai moyen de validation.
- Avancé : part des décisions traitées sans intervention du fondateur.
- Seuil : alerte si plus de 30 % des cas pilotes remontent inutilement au fondateur.

## Risque d'adoption

Crainte du fondateur sur la qualité. Prévoir un échantillon de contrôle a posteriori plutôt qu'une validation préalable systématique.

## Suivi attendu

Mesurer le délai, les erreurs éventuelles et le nombre de validations remontées.

## Règles appliquées

`DIAGORG-R01`, `DIAGORG-R02`, `DIAGORG-R03`, `ALIGN-R02`, `PROC-P01`, `CHANGE-R03`.

---

# Cas 4 — SaaS B2B : acquisition forte, activation faible

## Contexte

Une startup SaaS obtient beaucoup d'essais gratuits grâce à un nouveau canal, mais peu d'utilisateurs atteignent l'étape clé d'activation.

## Demande initiale

Client : « Nous devons améliorer notre closing. »

## Données disponibles

- Inscriptions d'essai : +80 %.
- Démonstrations demandées : stables.
- Utilisateurs ayant terminé la configuration initiale : 22 %.
- Conversion essai → abonnement : 6 %.
- Les nouveaux comptes ne reçoivent qu'un email générique.
- Le produit nécessite trois étapes de configuration.

## Erreur à éviter

Traiter le problème comme un closing commercial sans localiser la perte dans le parcours d'activation.

## Cadrage

Objectif : porter l'activation de 22 % à 35 % en deux mois.

## Diagnostic

La perte se situe avant le closing, pendant l'onboarding produit.

## Vérification croisée

Le problème est à la fois commercial et organisationnel : le dispositif d'accompagnement des essais n'est pas structuré.

## Recommandation prioritaire

Créer une séquence d'onboarding ciblée sur les trois étapes critiques et déclencher une intervention humaine lorsque l'utilisateur reste bloqué plus de 48 heures.

## KPI

- Résultat : taux d'activation.
- Avancés : taux de réalisation de chaque étape ; temps jusqu'à la première valeur obtenue.
- Fréquence : hebdomadaire.

## Risque d'adoption

L'équipe support peut être surchargée. L'intervention humaine doit cibler uniquement les comptes à potentiel élevé ou réellement bloqués.

## Suivi attendu

Comparer une cohorte pilote avec une cohorte témoin.

## Règles appliquées

`DIAGCOM-R01`, `DIAGCOM-R03`, `ALIGN-R03`, `STRATCOM-P01`, `KPI-P01`, `CHANGE-R03`.

---

# Cas 5 — Entreprise industrielle : prospection élevée, qualité des opportunités faible

## Contexte

Une PME industrielle augmente le nombre d'appels sortants. Le volume de rendez-vous augmente, mais peu de projets correspondent à sa capacité technique ou à sa marge cible.

## Demande initiale

Client : « Les commerciaux doivent appeler encore plus. »

## Données disponibles

- Appels : +40 %.
- Rendez-vous : +25 %.
- Propositions : +10 %.
- Signatures rentables : stables.
- Une grande part des demandes concerne de petites séries peu rentables.
- Les critères de qualification ne sont pas formalisés.

## Erreur à éviter

Confondre volume d'activité et résultat business.

## Cadrage

Objectif : augmenter de 20 % le nombre d'opportunités qualifiées et rentables sous un trimestre, sans augmenter le volume total d'appels.

## Diagnostic

Le problème est la qualité de qualification, non le volume d'acquisition.

## Vérification croisée

Des critères commerciaux doivent intégrer les contraintes industrielles : taille minimale de série, délai, complexité et marge.

## Recommandation prioritaire

Créer une grille de qualification de cinq critères et la tester sur les nouveaux rendez-vous.

## KPI

- Résultat : nombre de signatures répondant à la marge cible.
- Avancé : part des rendez-vous respectant les critères de qualification.
- Seuil : alerte si moins de 50 % des rendez-vous sont qualifiés.

## Risque d'adoption

Les commerciaux peuvent craindre une baisse du nombre de rendez-vous. Aligner la rémunération et le pilotage sur les opportunités qualifiées, pas uniquement sur le volume.

## Suivi attendu

Comparer le taux proposition → signature rentable avant et après la grille.

## Règles appliquées

`OBJ-R01`, `DIAGCOM-R03`, `ALIGN-R01`, `STRATCOM-R02`, `KPI-R01`, `CHANGE-R01`.

---

# Cas 6 — Réseau de points de vente : trop d'indicateurs, aucune décision

## Contexte

Un réseau de 15 points de vente dispose d'un tableau de bord comprenant 35 indicateurs. Les responsables le consultent chaque semaine, mais aucune action standard n'est liée aux écarts.

## Demande initiale

Client : « Nous avons toutes les données, mais les performances restent inégales. »

## Données disponibles

- 35 KPI suivis.
- Aucune distinction entre indicateur de résultat et indicateur avancé.
- Les réunions durent deux heures.
- Chaque magasin commente les chiffres sans engagement formalisé.
- Les écarts principaux concernent le taux de disponibilité des produits et le délai de réassort.

## Erreur à éviter

Ajouter de nouveaux indicateurs ou recommander un nouvel outil de BI.

## Cadrage

Objectif : réduire les ruptures et rendre la réunion hebdomadaire décisionnelle dans les six semaines.

## Diagnostic

Le problème n'est pas l'absence de données, mais l'absence de hiérarchie et de décisions associées.

## Recommandation prioritaire

Limiter le pilotage hebdomadaire à :
1. taux de rupture ;
2. délai de réassort ;
3. ventes perdues estimées.

Associer à chaque seuil une action et un responsable.

## KPI

Le dispositif lui-même utilise les trois indicateurs ci-dessus. Les autres restent disponibles comme contexte mensuel.

## Risque d'adoption

Les équipes peuvent résister à l'abandon apparent d'indicateurs. Préciser qu'ils ne sont pas supprimés, mais changent de fréquence et de rôle.

## Suivi attendu

Mesurer la durée des réunions et le nombre d'actions clôturées à la date prévue.

## Règles appliquées

`KPI-R01`, `KPI-R02`, `KPI-R03`, `ERROR-R05`, `ERROR-R08`, `CHANGE-R01`.

---

# Cas 7 — Déploiement CRM non adopté

## Contexte

Une entreprise de services a déployé un CRM depuis trois mois. Les commerciaux continuent à utiliser des fichiers personnels et la direction souhaite imposer davantage de contrôle.

## Demande initiale

Client : « Le CRM est bon, mais l'équipe refuse de l'utiliser. »

## Données disponibles

- Les champs obligatoires sont nombreux.
- Certaines informations sont saisies deux fois.
- Les commerciaux ne voient pas les rapports produits par le CRM.
- La direction utilise les données principalement pour contrôler l'activité.
- Aucun pilote ni atelier de conception n'a été organisé.

## Erreur à éviter

Attribuer la non-adoption à la résistance au changement sans vérifier l'utilité et la charge du processus.

## Cadrage

Objectif : atteindre 80 % d'opportunités correctement enregistrées sous huit semaines, tout en réduisant le temps de saisie.

## Diagnostic

La non-adoption combine surcharge de saisie, doublons et absence de valeur perçue.

## Recommandation prioritaire

Réduire le formulaire aux informations indispensables, supprimer une double saisie et créer un rapport directement utile aux commerciaux.

Commencer avec une équipe pilote.

## KPI

- Résultat : part d'opportunités correctement enregistrées.
- Avancés : temps moyen de saisie ; taux d'utilisation du rapport commercial.
- Fréquence : hebdomadaire.

## Risque d'adoption

Une communication uniquement descendante renforcerait la résistance. Associer les utilisateurs du pilote à la simplification.

## Suivi attendu

Comparer adoption, qualité des données et temps de saisie avant/après pilote.

## Règles appliquées

`PROC-R02`, `CHANGE-R02`, `CHANGE-R03`, `KPI-P01`, `ERROR-R09`.

---

# Cas 8 — Restaurant multi-sites : visibilité en hausse, qualité de service en baisse

## Contexte

Un restaurant de trois sites mène une campagne locale efficace. Les réservations progressent, mais les avis récents mentionnent des délais et des erreurs de commande.

## Demande initiale

Client : « Il faut continuer la campagne pour profiter de la demande. »

## Données disponibles

- Réservations : +35 %.
- Délai moyen de service : 18 min → 31 min.
- Avis moyens : 4,5 → 3,8.
- Taux d'erreur de commande : +60 %.
- Deux sites concentrent la plupart des problèmes.
- Les plannings n'ont pas été adaptés au nouveau volume.

## Erreur à éviter

Augmenter encore l'acquisition sans vérifier la capacité de service.

## Cadrage

Objectif : maintenir le niveau de réservations actuel tout en ramenant le délai moyen sous 22 minutes et la note au-dessus de 4,2 sous six semaines.

## Diagnostic

Le marketing fonctionne, mais la capacité organisationnelle n'a pas suivi.

## Vérification croisée

La poursuite de l'accélération commerciale risque de détériorer durablement la réputation.

## Recommandation prioritaire

Stabiliser les deux sites en difficulté : ajuster les plannings sur les périodes de pointe et limiter temporairement les promotions sur les créneaux saturés.

## KPI

- Résultat : note moyenne et taux de réservation maintenu.
- Avancés : délai de service ; taux d'erreur ; occupation par créneau.
- Fréquence : quotidienne pour les opérations, hebdomadaire pour la synthèse.

## Risque d'adoption

L'équipe marketing peut craindre de perdre de la croissance. Présenter la limitation comme une protection de la réputation et un séquençage temporaire.

## Suivi attendu

Réactiver progressivement les promotions uniquement lorsque les seuils opérationnels sont respectés.

## Règles appliquées

`ALIGN-R03`, `DIAGORG-P01`, `STRATCOM-R03`, `KPI-R04`, `CHANGE-R01`.

---

# Cas 9 — Suivi : recommandation appliquée sans effet mesurable

## Contexte

Une entreprise a mis en place la recommandation précédente : relancer les propositions sous 48 heures. L'équipe respecte la règle, mais le taux de signature ne progresse pas.

## Demande initiale lors du suivi

Client : « On a fait ce qui était prévu, mais cela ne change rien. »

## Données disponibles

- 92 % des relances sont réalisées sous 48 heures.
- Le taux de signature reste stable.
- Les motifs de refus ne sont pas enregistrés.
- Le prix et le périmètre de l'offre sont souvent renégociés.

## Erreur à éviter

Demander davantage de relances ou conclure que l'équipe exécute mal l'action.

## Traitement attendu

1. reconnaître que l'action a été correctement adoptée ;
2. constater que l'hypothèse initiale n'est pas confirmée ;
3. collecter les motifs de refus ;
4. réouvrir le diagnostic sur la proposition de valeur, le prix ou le périmètre.

## Nouvelle action prioritaire

Coder les motifs de perte sur les vingt prochaines propositions afin d'identifier la cause dominante.

## KPI

- Taux de complétude des motifs de perte.
- Répartition des motifs.
- Taux de signature par motif ou segment si pertinent.

## Suivi attendu

Revenir sur le diagnostic après vingt cas documentés.

## Règles appliquées

`METHOD-P02`, `METHOD-R03`, `CHANGE-P01`, `ERROR-R02`, `DIAGCOM-R02`.

---

# Cas 10 — Suivi : recommandation non appliquée faute de clarté

## Contexte

Le Coach IA avait recommandé de clarifier les rôles dans le traitement des demandes clients. Lors du suivi, rien n'a changé.

## Demande initiale lors du suivi

Client : « Nous n'avons pas eu le temps de faire la cartographie. »

## Données disponibles

- Le client associe « cartographie » à un projet complexe.
- Le problème concerne principalement le premier tri des demandes.
- Trois personnes interviennent, mais personne n'en est explicitement responsable.

## Erreur à éviter

Répéter la même recommandation ou considérer le client comme non engagé.

## Traitement attendu

Réduire l'action :
1. lister les trois personnes impliquées ;
2. désigner un responsable du premier tri ;
3. tester cette règle pendant une semaine ;
4. mesurer le nombre de demandes sans propriétaire après quatre heures.

## KPI

- Part des demandes avec responsable sous quatre heures.
- Nombre de demandes oubliées.

## Risque d'adoption

Faible, car l'action est réduite et immédiatement testable.

## Règles appliquées

`CHANGE-R02`, `CHANGE-R03`, `METHOD-R05`, `DIAGORG-R02`.

---

## 4. Matrice de recherche rapide

| Signal du client | Cas de référence | Documents principaux |
|---|---:|---|
| Plus de prospects mais pas plus de ventes | Cas 1 | DIAGCOM, DIAGORG, ALIGN |
| Plus de trafic mais conversion en baisse | Cas 2 | DIAGCOM, ALIGN, KPI |
| Tout passe par le dirigeant | Cas 3 | DIAGORG, PROC, CHANGE |
| Essais nombreux mais activation faible | Cas 4 | DIAGCOM, STRATCOM, KPI |
| Beaucoup d'appels, peu d'affaires rentables | Cas 5 | OBJ, DIAGCOM, ALIGN |
| Beaucoup de KPI, peu de décisions | Cas 6 | KPI, ERROR |
| Outil déployé mais non adopté | Cas 7 | PROC, CHANGE |
| Acquisition réussie mais service dégradé | Cas 8 | ALIGN, DIAGORG, KPI |
| Action appliquée sans effet | Cas 9 | METHOD, CHANGE, DIAGCOM |
| Action non appliquée car trop complexe | Cas 10 | METHOD, CHANGE, DIAGORG |

---

## 5. Checklist d'utilisation d'un cas

Avant d'utiliser un cas comme référence, le Coach IA vérifie :

- Le symptôme est-il réellement comparable ?
- Le point de friction se situe-t-il à la même étape ?
- Le modèle économique et le cycle de vente sont-ils compatibles ?
- Les contraintes organisationnelles sont-elles similaires ?
- Les données du client confirment-elles l'analogie ?
- La capacité et les ressources sont-elles comparables ?
- La recommandation du cas reste-t-elle réversible dans le contexte actuel ?
- Les différences avec le cas ont-elles été signalées ?

Si plusieurs réponses sont négatives, le cas ne doit pas guider la recommandation.

---

## 6. Format de sortie attendu lors de l'utilisation d'un cas

Lorsque le Coach IA s'appuie sur un cas, il formule :

1. **Analogie identifiée :** le point commun précis avec le cas.
2. **Différences importantes :** secteur, capacité, données ou cycle.
3. **Hypothèse transférable :** le raisonnement à tester, non la solution copiée.
4. **Question de validation :** la donnée nécessaire pour confirmer l'analogie.
5. **Niveau de confiance :** faible, moyen ou élevé.

Exemple :

> « Votre situation ressemble au Cas 1 sur un point précis : le volume de demandes augmente alors que les signatures stagnent. La différence est que votre délai d'envoi des propositions n'est pas encore connu. Avant de transférer l'hypothèse du goulot de validation, quel est ce délai aujourd'hui ? Confiance actuelle : faible. »

---

## 7. Knowledge Cards

**Knowledge Card — Cas analogue**
Quand l'utiliser : lorsqu'un scénario du corpus partage le même symptôme et le même point de friction probable.
Erreur typique : copier la recommandation finale sans vérifier les données du client.
Action : transférer l'hypothèse et la méthode de diagnostic, pas la solution.

**Knowledge Card — Cas intégré**
Quand l'utiliser : lorsqu'une situation combine commercial, organisationnel, KPI et adoption.
Erreur typique : ne récupérer qu'une partie du cas et ignorer les dépendances croisées.
Action : suivre la chaîne complète du raisonnement.

**Knowledge Card — Cas de suivi**
Quand l'utiliser : une action a été appliquée sans effet ou n'a pas été appliquée.
Cas associés : Cas 9 et Cas 10.
Erreur typique : répéter la recommandation sans réviser l'hypothèse ou l'adoption.

---

## 8. Résumé de récupération RAG

Mission : ce document fournit des cas types intégrés montrant comment appliquer le cycle complet du Coach IA sans copier mécaniquement une solution.

Concepts clés : analogie contrôlée, cas composite, symptôme, diagnostic croisé, action prioritaire, KPI, adoption, suivi.

Cas disponibles :
- Cas 1 : volume de demandes en hausse, signatures stagnantes ;
- Cas 2 : trafic en hausse, conversion et service dégradés ;
- Cas 3 : goulot de validation autour du dirigeant ;
- Cas 4 : activation SaaS faible ;
- Cas 5 : prospection élevée, qualification insuffisante ;
- Cas 6 : tableau de bord surchargé ;
- Cas 7 : CRM non adopté ;
- Cas 8 : croissance commerciale au-delà de la capacité ;
- Cas 9 : action appliquée sans effet ;
- Cas 10 : action non appliquée car trop complexe.

Livrable attendu : analogie, différences, hypothèse à tester, question de validation et niveau de confiance.
