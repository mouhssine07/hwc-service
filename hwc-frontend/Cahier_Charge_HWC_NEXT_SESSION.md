# Guide prochaine session - HWC Phase 2

Document cree pour guider la prochaine session de travail.
Le fichier `Cahier_Charge_HWC.pdf` reste la reference cahier des charges originale. Il n'a pas ete modifie directement car c'est un PDF, pas un fichier Markdown editable proprement.

## Etat Git a conserver

- Branche de travail: `feature/new-functionality`
- Ne pas merger dans `main` pour l'instant.
- Toujours faire les commits et push sur `origin/feature/new-functionality`.
- Dernier etat connu avant ce document: tout etait pousse, avec les fonctionnalites client/admin/recommandation/diagnostic.

Commandes utiles:

```powershell
git status --short --branch
git log --oneline -5
git push origin feature/new-functionality
```

## Fonctionnalites deja terminees

- Authentification client: inscription, connexion, profil connecte.
- Parcours diagnostic client: demarrage, questions, score, resultat.
- Historique client avec suppression d'un diagnostic.
- Boutons visibles dans le site vitrine pour acceder au diagnostic.
- Administration des clients.
- Desactivation client cote admin.
- Deconnexion automatique/rejet des sessions client quand un admin desactive le compte.
- Module recommandations:
  - generation de recommandations apres finalisation du diagnostic;
  - plan d'action client;
  - page client recommandations;
  - gestion admin des regles de recommandation.
- Seed demo pour tester l'admin:
  - services;
  - sous-services;
  - etiquettes;
  - avantages;
  - etapes;
  - FAQ;
  - chiffres cles;
  - temoignage;
  - certification;
  - clients/pays;
  - regles de recommandation.
- Navigation admin avec affichage type accordions pour les sous-elements.

## Tests deja valides

Commandes utilisees:

```powershell
.\mvnw.cmd test
cd hwc-frontend
npm run build
```

Dernier resultat connu:

- Backend: tests OK.
- Frontend: build OK.
- Fonctionnalites precedentes testees manuellement par le client/utilisateur.

## Prochaine etape recommandee

### Module 5 - Dashboard decisionnel client

Objectif: ajouter un tableau de bord client apres le diagnostic pour transformer les scores et recommandations en vue claire, visuelle et actionnable.

Route cible:

- Frontend: `/client/dashboard`
- Backend: `GET /api/client/dashboard`

Liens a ajouter:

- Depuis la page resultat diagnostic.
- Depuis la page recommandations.
- Depuis la page de demarrage/historique diagnostic.
- Eventuellement depuis le bouton diagnostic du site vitrine apres connexion.

## Backend a implementer

Creer une API dashboard client qui retourne une vue consolidee basee sur les diagnostics du client connecte.

Fichiers probables:

- `src/main/java/hwc_backend/controller/DashboardClientController.java`
- `src/main/java/hwc_backend/service/DashboardClientService.java`
- `src/main/java/hwc_backend/service/impl/DashboardClientServiceImpl.java`
- `src/main/java/hwc_backend/dto/dashboard/DashboardClientDTO.java`
- `src/main/java/hwc_backend/dto/dashboard/ScoreCategorieDTO.java`
- `src/main/java/hwc_backend/dto/dashboard/HistoriqueDiagnosticDTO.java`
- `src/main/java/hwc_backend/dto/dashboard/AlerteCritiqueDTO.java`
- `src/main/java/hwc_backend/dto/dashboard/RecommandationResumeDTO.java`

Donnees minimum a retourner:

- dernier diagnostic finalise;
- score global;
- niveau global;
- scores par categorie;
- historique des scores;
- categories faibles;
- alertes critiques;
- recommandations principales;
- plan d'action resume;
- services HWC recommandes;
- statistiques simples: nombre de diagnostics, date du dernier diagnostic, progression entre deux diagnostics si disponible.

Regles metier proposees:

- Si aucun diagnostic finalise: retourner un dashboard vide avec un message/action pour lancer un diagnostic.
- Score critique: categorie avec score inferieur ou egal a 40.
- Priorite haute: recommandation liee a une categorie critique ou score global faible.
- Progression: comparer les deux derniers diagnostics finalises du client.

Tests backend a ajouter/adapter:

- Client sans diagnostic finalise.
- Client avec un diagnostic finalise.
- Client avec deux diagnostics finalises pour verifier la progression.
- Rejet si non connecte.
- Isolation: un client ne voit pas les donnees d'un autre client.

## Frontend a implementer

Verifier d'abord les dependances. Si `recharts` n'existe pas dans `package.json`, l'installer:

```powershell
cd hwc-frontend
npm install recharts
```

Fichiers probables:

- `hwc-frontend/src/api/dashboardApi.js`
- `hwc-frontend/src/pages/client/DashboardClientPage.jsx`
- `hwc-frontend/src/components/dashboard/ScoreGlobalDonut.jsx`
- `hwc-frontend/src/components/dashboard/KpiCards.jsx`
- `hwc-frontend/src/components/dashboard/RadarChartScores.jsx`
- `hwc-frontend/src/components/dashboard/HistoriqueBarChart.jsx`
- `hwc-frontend/src/components/dashboard/CategoryDetailCards.jsx`
- `hwc-frontend/src/components/dashboard/PlanActionTimeline.jsx`
- `hwc-frontend/src/components/dashboard/AlertesCritiques.jsx`
- `hwc-frontend/src/components/dashboard/ServicesRecommandes.jsx`
- `hwc-frontend/src/components/dashboard/BenchmarkSecteur.jsx`

Interface attendue:

- Page dense, professionnelle, style SaaS/BI.
- Pas de landing page.
- Score global visible en premier.
- Cartes KPI compactes.
- Graphiques lisibles sur mobile et desktop.
- Alertes critiques bien visibles mais sans surcharge.
- Plan d'action sous forme de timeline ou liste priorisee.
- Bouton visible pour relancer un diagnostic.

Etats UI a prevoir:

- Chargement.
- Erreur API.
- Aucun diagnostic disponible.
- Dashboard avec donnees.
- Dashboard avec recommandations absentes.

## Points d'integration importants

- Utiliser `clientAxiosInstance`.
- Respecter `ClientProtectedRoute`.
- Ajouter la route dans `hwc-frontend/src/App.jsx`.
- Ajouter les liens depuis:
  - `DiagnosticResultatPage.jsx`;
  - `RecommandationsPage.jsx`;
  - `DiagnosticStartPage.jsx`.
- Ne pas casser les pages existantes.

## Verification avant commit

Backend:

```powershell
.\mvnw.cmd test
```

Frontend:

```powershell
cd hwc-frontend
npm run build
```

Git:

```powershell
git status --short --branch
git add .
git commit -m "feat: add client dashboard"
git push origin feature/new-functionality
```

## Etape suivante apres le dashboard

Apres validation du Module 5, passer au Module 6:

- generation PDF du diagnostic;
- rapport client telechargeable;
- integration OpenAI uniquement via variable d'environnement;
- historique des rapports.

Ne pas commencer le Module 6 avant que le dashboard client soit teste et valide.
