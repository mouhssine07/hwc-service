# Plan Dashboard Admin HWC

## Objectif

Créer un back-office administrateur pour gérer le contenu du site vitrine Harmony Works Consulting.

Ce plan couvre uniquement la phase 1 :

- site vitrine existant
- back-office de gestion de contenu
- authentification admin
- CRUD des entités existantes
- upload d'images

Hors périmètre phase 1 :

- IA
- scoring
- recommandations
- BI / analytics avancés
- refresh token avancé

---

## État Actuel

### Frontend déjà en place

Stack :

- React 18
- Vite
- Tailwind CSS
- React Router DOM
- Lucide React

Pages vitrine déjà présentes :

- Accueil
- Régie & Outsourcing
- Marketing Digital
- Prospection Commerciale
- Support Administratif
- Leadership & Corporate Events
- Performance Sous Pression
- Méthode HWC 360
- pages sous-services marketing

### Backend déjà en place

Stack :

- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Lombok

Entités métier déjà présentes :

- Services
- SousServices
- Etiquettes
- ServiceFonctionnalites
- ServiceImages
- SousServiceFonctionnalites
- SousServiceAvantages
- SousServiceEtapes
- SousServiceFaqs
- Accompagnements
- ClientsConfiance
- ChiffresCles
- Certifications
- Temoignages
- Pays
- DemandesContact

---

## Décision D'Architecture

Le dashboard admin sera intégré dans le projet React existant.

Zones de navigation :

- `/` et routes publiques : site vitrine
- `/login` : connexion admin
- `/admin/*` : dashboard protégé

API backend :

- `/api/public/**` : endpoints publics pour le site vitrine
- `/api/auth/**` : authentification
- `/api/admin/**` : endpoints protégés pour le dashboard

Principe important :

- ne pas casser les pages vitrine existantes
- développer le dashboard progressivement
- une ressource CRUD à la fois
- tester le backend avant de connecter le frontend

---

## Structure Frontend Cible

```txt
hwc-frontend/
├─ src/
│  ├─ api/
│  │  ├─ axiosInstance.js
│  │  ├─ authApi.js
│  │  ├─ servicesApi.js
│  │  ├─ sousServicesApi.js
│  │  ├─ etiquettesApi.js
│  │  ├─ temoignagesApi.js
│  │  ├─ certificationsApi.js
│  │  ├─ paysApi.js
│  │  ├─ chiffresClesApi.js
│  │  ├─ clientsConfianceApi.js
│  │  ├─ demandesContactApi.js
│  │  └─ uploadApi.js
│  │
│  ├─ components/
│  │  ├─ ui/
│  │  │  ├─ Button.jsx
│  │  │  ├─ Input.jsx
│  │  │  ├─ Textarea.jsx
│  │  │  ├─ Select.jsx
│  │  │  ├─ Modal.jsx
│  │  │  ├─ ConfirmDialog.jsx
│  │  │  ├─ Table.jsx
│  │  │  ├─ Pagination.jsx
│  │  │  ├─ Card.jsx
│  │  │  ├─ Badge.jsx
│  │  │  ├─ Spinner.jsx
│  │  │  ├─ EmptyState.jsx
│  │  │  └─ ImageUpload.jsx
│  │  │
│  │  └─ admin/
│  │     ├─ layout/
│  │     │  ├─ Sidebar.jsx
│  │     │  ├─ Topbar.jsx
│  │     │  ├─ DashboardLayout.jsx
│  │     │  └─ ProtectedRoute.jsx
│  │     │
│  │     └─ forms/
│  │        ├─ ServiceForm.jsx
│  │        ├─ SousServiceForm.jsx
│  │        ├─ EtiquetteForm.jsx
│  │        ├─ TemoignageForm.jsx
│  │        ├─ CertificationForm.jsx
│  │        ├─ PaysForm.jsx
│  │        ├─ ChiffreCleForm.jsx
│  │        ├─ ClientConfianceForm.jsx
│  │        ├─ ServiceFonctionnaliteForm.jsx
│  │        ├─ ServiceImageForm.jsx
│  │        ├─ SousServiceFonctionnaliteForm.jsx
│  │        ├─ SousServiceAvantageForm.jsx
│  │        ├─ SousServiceEtapeForm.jsx
│  │        ├─ SousServiceFaqForm.jsx
│  │        └─ AccompagnementForm.jsx
│  │
│  ├─ pages/
│  │  ├─ auth/
│  │  │  └─ LoginPage.jsx
│  │  │
│  │  └─ admin/
│  │     ├─ DashboardHome.jsx
│  │     ├─ ServicesPage.jsx
│  │     ├─ SousServicesPage.jsx
│  │     ├─ EtiquettesPage.jsx
│  │     ├─ TemoignagesPage.jsx
│  │     ├─ CertificationsPage.jsx
│  │     ├─ PaysPage.jsx
│  │     ├─ ChiffresClesPage.jsx
│  │     ├─ ClientsConfiancePage.jsx
│  │     ├─ DemandesContactPage.jsx
│  │     ├─ ServiceFonctionnalitesPage.jsx
│  │     ├─ ServiceImagesPage.jsx
│  │     ├─ SousServiceFonctionnalitesPage.jsx
│  │     ├─ SousServiceAvantagesPage.jsx
│  │     ├─ SousServiceEtapesPage.jsx
│  │     ├─ SousServiceFaqsPage.jsx
│  │     └─ AccompagnementsPage.jsx
│  │
│  ├─ store/
│  │  ├─ authStore.js
│  │  └─ uiStore.js
│  │
│  ├─ utils/
│  │  ├─ constants.js
│  │  └─ helpers.js
│  │
│  ├─ App.jsx
│  ├─ main.jsx
│  └─ styles.css
│
├─ .env
├─ package.json
└─ tailwind.config.js
```

---

## Dépendances Frontend À Ajouter

Déjà installées :

- `react-router-dom`
- `lucide-react`

À ajouter :

```bash
npm install axios
npm install react-hook-form zod @hookform/resolvers
npm install react-hot-toast
npm install zustand
```

Créer aussi :

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## Phase 1 - Sécurisation Backend

Priorité absolue avant le dashboard frontend.

### 1.1 Dépendances Backend

Ajouter dans `pom.xml` :

- `spring-boot-starter-security`
- `spring-boot-starter-validation`
- `jjwt-api`
- `jjwt-impl`
- `jjwt-jackson`

### 1.2 Authentification

Créer :

- `User`
- `Role`
- table `user_roles`
- `UserRepository`
- `RoleRepository`
- `CustomUserDetailsService`
- `JwtUtil`
- `JwtAuthenticationFilter`
- `SecurityConfig`
- DTOs auth
- `AuthController`
- `DataInitializer`

Endpoints auth :

- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/auth/me`

Admin par défaut :

- email : `admin@hwc.com`
- password initial : `Admin@2026`
- role : `ROLE_ADMIN`

À faire ensuite : changer le mot de passe après première connexion.

### 1.3 Sécurité HTTP

Routes publiques :

- `POST /api/auth/login`
- `GET /api/public/**`
- `POST /api/public/demandes-contact`

Routes protégées :

- `/api/admin/**` uniquement pour `ROLE_ADMIN`

Configuration :

- CSRF désactivé pour API REST
- sessions stateless
- CORS autorisé pour `http://localhost:5173`
- BCrypt pour les mots de passe

### 1.4 Tests Backend Auth

À valider avec Postman ou Thunder Client :

- login admin retourne un token
- `/api/admin/services` sans token retourne 401
- `/api/admin/services` avec token retourne 200
- `/api/public/services` fonctionne sans token

---

## Phase 2 - API Admin Et API Publique

### 2.1 Controllers Admin

Créer un controller admin par entité :

| Entité                     | Endpoint                                  |
| -------------------------- | ----------------------------------------- |
| Services                   | `/api/admin/services`                     |
| SousServices               | `/api/admin/sous-services`                |
| Etiquettes                 | `/api/admin/etiquettes`                   |
| Temoignages                | `/api/admin/temoignages`                  |
| Certifications             | `/api/admin/certifications`               |
| Pays                       | `/api/admin/pays`                         |
| ChiffresCles               | `/api/admin/chiffres-cles`                |
| ClientsConfiance           | `/api/admin/clients-confiance`            |
| DemandesContact            | `/api/admin/demandes-contact`             |
| ServiceFonctionnalites     | `/api/admin/service-fonctionnalites`      |
| ServiceImages              | `/api/admin/service-images`               |
| SousServiceFonctionnalites | `/api/admin/sous-service-fonctionnalites` |
| SousServiceAvantages       | `/api/admin/sous-service-avantages`       |
| SousServiceEtapes          | `/api/admin/sous-service-etapes`          |
| SousServiceFaqs            | `/api/admin/sous-service-faqs`            |
| Accompagnements            | `/api/admin/accompagnements`              |

Endpoints standards :

- `GET /`
- `GET /{id}`
- `POST /`
- `PUT /{id}`
- `DELETE /{id}`

Recommandation :

- utiliser `Pageable` pour les listes
- retourner des DTOs, jamais les entités directement
- ajouter validation backend avec annotations `@Valid`

### 2.2 Controllers Publics

Créer les endpoints publics nécessaires au site vitrine :

- `GET /api/public/services`
- `GET /api/public/services/{id}`
- `GET /api/public/sous-services/{id}`
- `GET /api/public/temoignages`
- `GET /api/public/certifications`
- `GET /api/public/pays`
- `GET /api/public/chiffres-cles`
- `GET /api/public/clients-confiance`
- `POST /api/public/demandes-contact`

Objectif :

- préparer la migration progressive des contenus statiques React vers l'API
- garder le site vitrine fonctionnel même pendant le développement admin

---

## Phase 3 - Setup Frontend Admin

### 3.1 Axios

Créer `src/api/axiosInstance.js`.

Responsabilités :

- base URL depuis `VITE_API_BASE_URL`
- ajouter le token JWT dans `Authorization: Bearer <token>`
- rediriger vers `/login` en cas de 401

### 3.2 Store Auth

Créer `src/store/authStore.js`.

State :

- `user`
- `token`
- `isAuthenticated`

Actions :

- `login(token, user)`
- `logout()`
- `loadFromStorage()`

### 3.3 API Auth

Créer `src/api/authApi.js`.

Fonctions :

- `login(email, password)`
- `register(data)`
- `getCurrentUser()`

### 3.4 Routes

Modifier `App.jsx` pour gérer :

- routes vitrine existantes
- `/login`
- `/admin/*`

Pattern :

```jsx
<Route path="/login" element={<LoginPage />} />

<Route path="/admin" element={<ProtectedRoute />}>
  <Route element={<DashboardLayout />}>
    <Route index element={<DashboardHome />} />
    <Route path="services" element={<ServicesPage />} />
  </Route>
</Route>
```

---

## Phase 4 - Auth Frontend

Créer :

- `LoginPage.jsx`
- `ProtectedRoute.jsx`

Login page :

- formulaire email/password
- validation Zod
- toast erreurs/succès
- redirection vers `/admin`
- logo HWC
- design cohérent avec le site vitrine

ProtectedRoute :

- si pas connecté : redirection `/login`
- sinon : affiche `<Outlet />`

---

## Phase 5 - Layout Dashboard

Créer :

- `Sidebar.jsx`
- `Topbar.jsx`
- `DashboardLayout.jsx`
- `DashboardHome.jsx`

Sidebar :

- logo HWC
- liens admin groupés
- lien déconnexion
- état actif avec `NavLink`
- responsive mobile

Topbar :

- titre de page
- bouton menu mobile
- nom utilisateur
- logout

DashboardHome :

- nombre de services
- nombre de témoignages
- nombre de demandes de contact
- nombre de pays
- dernières demandes de contact

Endpoint recommandé :

- `GET /api/admin/dashboard/stats`

---

## Phase 6 - Composants UI Réutilisables

Créer avant les CRUD :

- `Button`
- `Input`
- `Textarea`
- `Select`
- `Modal`
- `ConfirmDialog`
- `Table`
- `Pagination`
- `Card`
- `Badge`
- `Spinner`
- `EmptyState`

Objectif :

- ne pas dupliquer les styles
- rendre toutes les pages admin homogènes
- accélérer la création des CRUD

---

## Phase 7 - Pages CRUD

Pattern standard pour chaque page :

- header avec titre
- bouton Ajouter
- recherche
- filtres si utile
- tableau
- actions Voir / Modifier / Supprimer
- pagination
- modal création/modification
- confirmation suppression
- loading state
- empty state
- toast succès/erreur

### Ordre recommandé

Commencer par les entités simples, puis les entités liées.

1. `EtiquettesPage`
2. `PaysPage`
3. `ChiffresClesPage`
4. `ServicesPage`
5. `SousServicesPage`
6. `ClientsConfiancePage`
7. `TemoignagesPage`
8. `CertificationsPage`
9. `ServiceFonctionnalitesPage`
10. `ServiceImagesPage`
11. `SousServiceFonctionnalitesPage`
12. `SousServiceAvantagesPage`
13. `SousServiceEtapesPage`
14. `SousServiceFaqsPage`
15. `AccompagnementsPage`
16. `DemandesContactPage`

---

## Détail Des CRUD

| #   | Entité                     | Page                                 | Formulaire                          | Upload |
| --- | -------------------------- | ------------------------------------ | ----------------------------------- | ------ |
| 1   | Etiquettes                 | `EtiquettesPage.jsx`                 | `EtiquetteForm.jsx`                 | Non    |
| 2   | Pays                       | `PaysPage.jsx`                       | `PaysForm.jsx`                      | Non    |
| 3   | ChiffresCles               | `ChiffresClesPage.jsx`               | `ChiffreCleForm.jsx`                | Non    |
| 4   | Services                   | `ServicesPage.jsx`                   | `ServiceForm.jsx`                   | Non    |
| 5   | SousServices               | `SousServicesPage.jsx`               | `SousServiceForm.jsx`               | Non    |
| 6   | ClientsConfiance           | `ClientsConfiancePage.jsx`           | `ClientConfianceForm.jsx`           | Oui    |
| 7   | Temoignages                | `TemoignagesPage.jsx`                | `TemoignageForm.jsx`                | Oui    |
| 8   | Certifications             | `CertificationsPage.jsx`             | `CertificationForm.jsx`             | Oui    |
| 9   | ServiceFonctionnalites     | `ServiceFonctionnalitesPage.jsx`     | `ServiceFonctionnaliteForm.jsx`     | Non    |
| 10  | ServiceImages              | `ServiceImagesPage.jsx`              | `ServiceImageForm.jsx`              | Oui    |
| 11  | SousServiceFonctionnalites | `SousServiceFonctionnalitesPage.jsx` | `SousServiceFonctionnaliteForm.jsx` | Non    |
| 12  | SousServiceAvantages       | `SousServiceAvantagesPage.jsx`       | `SousServiceAvantageForm.jsx`       | Non    |
| 13  | SousServiceEtapes          | `SousServiceEtapesPage.jsx`          | `SousServiceEtapeForm.jsx`          | Non    |
| 14  | SousServiceFaqs            | `SousServiceFaqsPage.jsx`            | `SousServiceFaqForm.jsx`            | Non    |
| 15  | Accompagnements            | `AccompagnementsPage.jsx`            | `AccompagnementForm.jsx`            | Non    |
| 16  | DemandesContact            | `DemandesContactPage.jsx`            | Aucun                               | Non    |

---

## Phase 8 - Upload Images

### Backend

Créer `FileUploadController`.

Endpoint :

- `POST /api/admin/upload/image`

Réponse :

```json
{
  "url": "/uploads/images/uuid-file.jpg"
}
```

Contraintes :

- JPG, PNG, WebP
- taille max 5MB
- nom unique avec UUID
- dossier `./uploads/images/`
- exposer `/uploads/**` via `WebMvcConfigurer`

### Frontend

Créer `ImageUpload.jsx`.

Fonctions :

- sélection fichier
- drag and drop
- preview
- suppression
- progress upload
- validation taille/type

À intégrer dans :

- `ClientConfianceForm`
- `TemoignageForm`
- `CertificationForm`
- `ServiceImageForm`

---

## Phase 9 - Connexion Du Site Vitrine À L'API

Cette phase vient après le dashboard stable.

Priorité de migration :

1. chiffres clés
2. clients de confiance
3. pays
4. certifications
5. témoignages
6. services principaux
7. sous-services
8. images services
9. demandes de contact

Principe :

- garder des fallbacks statiques au début
- remplacer progressivement les données codées en dur
- ne pas bloquer le rendu vitrine si l'API ne répond pas

---

## Planning Recommandé

| Phase                    |     Durée | Livrable                 |
| ------------------------ | --------: | ------------------------ |
| 1. Sécurité backend      | 2-3 jours | JWT + admin protégé      |
| 2. API admin/public      | 3-4 jours | CRUD backend testés      |
| 3. Setup frontend admin  |    1 jour | Axios + store + routes   |
| 4. Auth frontend         |    1 jour | login fonctionnel        |
| 5. Layout dashboard      |    1 jour | shell admin responsive   |
| 6. UI kit admin          |    1 jour | composants réutilisables |
| 7. CRUD frontend         | 4-6 jours | pages admin complètes    |
| 8. Upload images         | 1-2 jours | upload intégré           |
| 9. Connexion vitrine API | 2-4 jours | contenu dynamique        |

---

## Checklist De Validation

### Backend

- [ ] dépendances security/JWT ajoutées
- [ ] login admin fonctionne
- [ ] token JWT valide
- [ ] `/api/admin/**` protégé
- [ ] `/api/public/**` accessible
- [ ] CORS configuré
- [ ] validation backend ajoutée
- [ ] gestion globale des erreurs
- [ ] CRUD admin testés
- [ ] upload image testé

### Frontend

- [ ] dépendances installées
- [ ] `.env` créé
- [ ] axios configuré
- [ ] auth store fonctionnel
- [ ] page login fonctionnelle
- [ ] routes admin protégées
- [ ] layout dashboard responsive
- [ ] composants UI prêts
- [ ] pages CRUD créées
- [ ] toasts intégrés
- [ ] loading states intégrés
- [ ] empty states intégrés
- [ ] upload image intégré

### Site Vitrine

- [ ] routes existantes non cassées
- [ ] header/footer toujours visibles sur routes publiques
- [ ] dashboard n'affiche pas le header/footer vitrine
- [ ] contenus publics migrés progressivement
- [ ] fallback en cas d'erreur API

---

## Prochaine Étape Immédiate

Commencer par le backend :

1. ajouter Spring Security + JWT dans `pom.xml`
2. créer `User`, `Role`, repositories et DTO auth
3. configurer `SecurityConfig`
4. créer `AuthController`
5. créer l'admin par défaut
6. tester `/api/auth/login`

Ne pas commencer le dashboard frontend avant que l'auth backend fonctionne.
