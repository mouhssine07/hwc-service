# TODO Dashboard Admin HWC

Checklist opérationnelle pour continuer le développement du back-office admin.

Référence détaillée : `hwc-frontend/PLAN_DASHBOARD_ADMIN.md`

---

## 1. Préparer Le Backend Sécurisé

- [ ] Ajouter `spring-boot-starter-security` dans `pom.xml`
- [ ] Ajouter `spring-boot-starter-validation` dans `pom.xml`
- [ ] Ajouter les dépendances JWT `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- [ ] Créer l'entité `User`
- [ ] Créer l'entité `Role`
- [ ] Créer la relation `user_roles`
- [ ] Créer `UserRepository`
- [ ] Créer `RoleRepository`
- [ ] Créer les DTOs `LoginRequestDTO`, `RegisterRequestDTO`, `AuthResponseDTO`, `UserDTO`
- [ ] Créer `CustomUserDetailsService`
- [ ] Créer `JwtUtil`
- [ ] Créer `JwtAuthenticationFilter`
- [ ] Créer `SecurityConfig`
- [ ] Configurer CORS pour le frontend Vite
- [ ] Configurer BCrypt
- [ ] Ajouter `jwt.secret` et `jwt.expiration` dans `application.properties`
- [ ] Créer `AuthController`
- [ ] Créer `DataInitializer` avec admin par défaut
- [ ] Tester `POST /api/auth/login`
- [ ] Tester qu'une route `/api/admin/**` sans token retourne `401`
- [ ] Tester qu'une route `/api/admin/**` avec token fonctionne

---

## 2. Créer Les APIs Admin

- [ ] Créer les endpoints admin `/api/admin/services`
- [ ] Créer les endpoints admin `/api/admin/sous-services`
- [ ] Créer les endpoints admin `/api/admin/etiquettes`
- [ ] Créer les endpoints admin `/api/admin/temoignages`
- [ ] Créer les endpoints admin `/api/admin/certifications`
- [ ] Créer les endpoints admin `/api/admin/pays`
- [ ] Créer les endpoints admin `/api/admin/chiffres-cles`
- [ ] Créer les endpoints admin `/api/admin/clients-confiance`
- [ ] Créer les endpoints admin `/api/admin/demandes-contact`
- [ ] Créer les endpoints admin `/api/admin/service-fonctionnalites`
- [ ] Créer les endpoints admin `/api/admin/service-images`
- [ ] Créer les endpoints admin `/api/admin/sous-service-fonctionnalites`
- [ ] Créer les endpoints admin `/api/admin/sous-service-avantages`
- [ ] Créer les endpoints admin `/api/admin/sous-service-etapes`
- [ ] Créer les endpoints admin `/api/admin/sous-service-faqs`
- [ ] Créer les endpoints admin `/api/admin/accompagnements`
- [ ] Ajouter pagination avec `Pageable` sur les listes
- [ ] Ajouter validation `@Valid` sur les créations/modifications
- [ ] Ajouter une gestion globale d'erreurs avec `@ControllerAdvice`
- [ ] Tester chaque CRUD avec Postman ou Thunder Client

---

## 3. Créer Les APIs Publiques

- [ ] Créer `GET /api/public/services`
- [ ] Créer `GET /api/public/services/{id}`
- [ ] Créer `GET /api/public/sous-services/{id}`
- [ ] Créer `GET /api/public/temoignages`
- [ ] Créer `GET /api/public/certifications`
- [ ] Créer `GET /api/public/pays`
- [ ] Créer `GET /api/public/chiffres-cles`
- [ ] Créer `GET /api/public/clients-confiance`
- [ ] Créer `POST /api/public/demandes-contact`
- [ ] Vérifier que toutes les routes publiques fonctionnent sans token

---

## 4. Préparer Le Frontend Admin

- [ ] Installer `axios`
- [ ] Installer `react-hook-form`
- [ ] Installer `zod`
- [ ] Installer `@hookform/resolvers`
- [ ] Installer `react-hot-toast`
- [ ] Installer `zustand`
- [ ] Créer `hwc-frontend/.env`
- [ ] Ajouter `VITE_API_BASE_URL=http://localhost:8080/api`
- [ ] Créer `src/api/`
- [ ] Créer `src/store/`
- [ ] Créer `src/utils/`
- [ ] Créer `src/components/ui/`
- [ ] Créer `src/components/admin/layout/`
- [ ] Créer `src/components/admin/forms/`
- [ ] Créer `src/pages/auth/`
- [ ] Créer `src/pages/admin/`

---

## 5. Authentification Frontend

- [ ] Créer `src/api/axiosInstance.js`
- [ ] Ajouter l'intercepteur token JWT dans Axios
- [ ] Ajouter la redirection automatique vers `/login` en cas de `401`
- [ ] Créer `src/api/authApi.js`
- [ ] Créer `src/store/authStore.js`
- [ ] Persister le token dans `localStorage`
- [ ] Créer `LoginPage.jsx`
- [ ] Ajouter validation Zod du formulaire login
- [ ] Afficher les erreurs avec toast
- [ ] Créer `ProtectedRoute.jsx`
- [ ] Ajouter la route `/login`
- [ ] Ajouter les routes protégées `/admin/*`
- [ ] Tester login depuis le frontend
- [ ] Tester logout depuis le frontend

---

## 6. Layout Dashboard

- [ ] Créer `Sidebar.jsx`
- [ ] Créer `Topbar.jsx`
- [ ] Créer `DashboardLayout.jsx`
- [ ] Créer `DashboardHome.jsx`
- [ ] Ajouter navigation admin avec `NavLink`
- [ ] Ajouter état actif dans la sidebar
- [ ] Ajouter déconnexion dans le layout
- [ ] Rendre la sidebar responsive mobile
- [ ] Créer endpoint `GET /api/admin/dashboard/stats`
- [ ] Afficher les stats dans `DashboardHome`

---

## 7. UI Kit Admin

- [ ] Créer `Button.jsx`
- [ ] Créer `Input.jsx`
- [ ] Créer `Textarea.jsx`
- [ ] Créer `Select.jsx`
- [ ] Créer `Modal.jsx`
- [ ] Créer `ConfirmDialog.jsx`
- [ ] Créer `Table.jsx`
- [ ] Créer `Pagination.jsx`
- [ ] Créer `Card.jsx`
- [ ] Créer `Badge.jsx`
- [ ] Créer `Spinner.jsx`
- [ ] Créer `EmptyState.jsx`
- [ ] Vérifier la cohérence responsive des composants

---

## 8. Pages CRUD Frontend

- [ ] Créer `EtiquettesPage.jsx` + `EtiquetteForm.jsx`
- [ ] Créer `PaysPage.jsx` + `PaysForm.jsx`
- [ ] Créer `ChiffresClesPage.jsx` + `ChiffreCleForm.jsx`
- [ ] Créer `ServicesPage.jsx` + `ServiceForm.jsx`
- [ ] Créer `SousServicesPage.jsx` + `SousServiceForm.jsx`
- [ ] Créer `ClientsConfiancePage.jsx` + `ClientConfianceForm.jsx`
- [ ] Créer `TemoignagesPage.jsx` + `TemoignageForm.jsx`
- [ ] Créer `CertificationsPage.jsx` + `CertificationForm.jsx`
- [ ] Créer `ServiceFonctionnalitesPage.jsx` + `ServiceFonctionnaliteForm.jsx`
- [ ] Créer `ServiceImagesPage.jsx` + `ServiceImageForm.jsx`
- [ ] Créer `SousServiceFonctionnalitesPage.jsx` + `SousServiceFonctionnaliteForm.jsx`
- [ ] Créer `SousServiceAvantagesPage.jsx` + `SousServiceAvantageForm.jsx`
- [ ] Créer `SousServiceEtapesPage.jsx` + `SousServiceEtapeForm.jsx`
- [ ] Créer `SousServiceFaqsPage.jsx` + `SousServiceFaqForm.jsx`
- [ ] Créer `AccompagnementsPage.jsx` + `AccompagnementForm.jsx`
- [ ] Créer `DemandesContactPage.jsx`
- [ ] Ajouter recherche sur les pages principales
- [ ] Ajouter pagination sur les tableaux
- [ ] Ajouter confirmation suppression
- [ ] Ajouter toasts succès/erreur
- [ ] Ajouter loading states
- [ ] Ajouter empty states

---

## 9. Upload Images

- [ ] Créer `FileUploadController`
- [ ] Créer endpoint `POST /api/admin/upload/image`
- [ ] Créer dossier backend `uploads/images`
- [ ] Générer les noms de fichiers avec UUID
- [ ] Valider formats `jpg`, `png`, `webp`
- [ ] Limiter taille fichier à `5MB`
- [ ] Exposer `/uploads/**`
- [ ] Créer `src/api/uploadApi.js`
- [ ] Créer `ImageUpload.jsx`
- [ ] Intégrer upload dans `ClientConfianceForm`
- [ ] Intégrer upload dans `TemoignageForm`
- [ ] Intégrer upload dans `CertificationForm`
- [ ] Intégrer upload dans `ServiceImageForm`
- [ ] Tester upload depuis le dashboard

---

## 10. Connexion Progressive Du Site Vitrine À L'API

- [ ] Connecter les chiffres clés à `/api/public/chiffres-cles`
- [ ] Connecter les clients de confiance à `/api/public/clients-confiance`
- [ ] Connecter les pays à `/api/public/pays`
- [ ] Connecter les certifications à `/api/public/certifications`
- [ ] Connecter les témoignages à `/api/public/temoignages`
- [ ] Connecter les services à `/api/public/services`
- [ ] Connecter les sous-services à `/api/public/sous-services/{id}`
- [ ] Connecter les images services
- [ ] Connecter le formulaire contact à `/api/public/demandes-contact`
- [ ] Ajouter fallback statique si l'API ne répond pas
- [ ] Tester que le site vitrine reste fonctionnel API éteinte

---

## 11. Validation Finale

- [ ] Tester build frontend avec `npm.cmd run build`
- [ ] Tester backend avec `mvn test`
- [ ] Tester login admin complet
- [ ] Tester chaque page CRUD
- [ ] Tester upload images
- [ ] Tester responsive dashboard mobile
- [ ] Tester responsive site vitrine
- [ ] Vérifier que le dashboard n'affiche pas header/footer vitrine
- [ ] Vérifier que les routes publiques restent accessibles
- [ ] Vérifier que les routes admin sont protégées
- [ ] Changer le mot de passe admin par défaut

---

## Prochaine Tâche À Faire Maintenant

- [ ] Commencer Phase 1 : ajouter Spring Security + JWT dans le backend
