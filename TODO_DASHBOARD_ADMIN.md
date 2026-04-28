# TODO Dashboard Admin HWC

Checklist opérationnelle pour continuer le développement du back-office admin.

Référence détaillée : `hwc-frontend/PLAN_DASHBOARD_ADMIN.md`

---

## 1. Préparer Le Backend Sécurisé

- [x] Ajouter `spring-boot-starter-security` dans `pom.xml`
- [x] Ajouter `spring-boot-starter-validation` dans `pom.xml`
- [x] Ajouter les dépendances JWT `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- [x] Créer l'entité `User`
- [x] Créer l'entité `Role`
- [x] Créer la relation `user_roles`
- [x] Créer `UserRepository`
- [x] Créer `RoleRepository`
- [x] Créer les DTOs `LoginRequestDTO`, `RegisterRequestDTO`, `AuthResponseDTO`, `UserDTO`
- [x] Créer `CustomUserDetailsService`
- [x] Créer `JwtUtil`
- [x] Créer `JwtAuthenticationFilter`
- [x] Créer `SecurityConfig`
- [x] Configurer CORS pour le frontend Vite
- [x] Configurer BCrypt
- [x] Ajouter `jwt.secret` et `jwt.expiration` dans `application.properties`
- [x] Créer `AuthController`
- [x] Créer `DataInitializer` avec admin par défaut
- [x] Tester `POST /api/auth/login`
- [x] Tester qu'une route `/api/admin/**` sans token retourne `401`
- [x] Tester qu'une route `/api/admin/**` avec token fonctionne

---

## 2. Créer Les APIs Admin

- [x] Créer les endpoints admin `/api/admin/services`
- [x] Créer les endpoints admin `/api/admin/sous-services`
- [x] Créer les endpoints admin `/api/admin/etiquettes`
- [x] Créer les endpoints admin `/api/admin/temoignages`
- [x] Créer les endpoints admin `/api/admin/certifications`
- [x] Créer les endpoints admin `/api/admin/pays`
- [x] Créer les endpoints admin `/api/admin/chiffres-cles`
- [x] Créer les endpoints admin `/api/admin/clients-confiance`
- [x] Créer les endpoints admin `/api/admin/demandes-contact`
- [x] Créer les endpoints admin `/api/admin/service-fonctionnalites`
- [x] Créer les endpoints admin `/api/admin/service-images`
- [x] Créer les endpoints admin `/api/admin/sous-service-fonctionnalites`
- [x] Créer les endpoints admin `/api/admin/sous-service-avantages`
- [x] Créer les endpoints admin `/api/admin/sous-service-etapes`
- [x] Créer les endpoints admin `/api/admin/sous-service-faqs`
- [x] Créer les endpoints admin `/api/admin/accompagnements`
- [x] Ajouter pagination avec `Pageable` sur les listes
- [x] Ajouter validation `@Valid` sur les créations/modifications
- [x] Ajouter une gestion globale d'erreurs avec `@ControllerAdvice`
- [ ] Tester chaque CRUD avec Postman ou Thunder Client

---

## 3. Créer Les APIs Publiques

- [x] Créer `GET /api/public/services`
- [x] Créer `GET /api/public/services/{id}`
- [x] Créer `GET /api/public/sous-services/{id}`
- [x] Créer `GET /api/public/temoignages`
- [x] Créer `GET /api/public/certifications`
- [x] Créer `GET /api/public/pays`
- [x] Créer `GET /api/public/chiffres-cles`
- [x] Créer `GET /api/public/clients-confiance`
- [x] Créer `POST /api/public/demandes-contact`
- [x] Vérifier que toutes les routes publiques fonctionnent sans token

---

## 4. Préparer Le Frontend Admin

- [x] Installer `axios`
- [x] Installer `react-hook-form`
- [x] Installer `zod`
- [x] Installer `@hookform/resolvers`
- [x] Installer `react-hot-toast`
- [x] Installer `zustand`
- [x] Créer `hwc-frontend/.env`
- [x] Ajouter `VITE_API_BASE_URL=http://localhost:8080/api`
- [x] Créer `src/api/`
- [x] Créer `src/store/`
- [x] Créer `src/utils/`
- [x] Créer `src/components/ui/`
- [x] Créer `src/components/admin/layout/`
- [x] Créer `src/components/admin/forms/`
- [x] Créer `src/pages/auth/`
- [x] Créer `src/pages/admin/`

---

## 5. Authentification Frontend

- [x] Créer `src/api/axiosInstance.js`
- [x] Ajouter l'intercepteur token JWT dans Axios
- [x] Ajouter la redirection automatique vers `/login` en cas de `401`
- [x] Créer `src/api/authApi.js`
- [x] Créer `src/store/authStore.js`
- [x] Persister le token dans `localStorage`
- [x] Créer `LoginPage.jsx`
- [x] Ajouter validation Zod du formulaire login
- [x] Afficher les erreurs avec toast
- [x] Créer `ProtectedRoute.jsx`
- [x] Ajouter la route `/login`
- [x] Ajouter les routes protégées `/admin/*`
- [x] Tester login depuis le frontend
- [x] Tester logout depuis le frontend

---

## 6. Layout Dashboard

- [x] Créer `Sidebar.jsx`
- [x] Créer `Topbar.jsx`
- [x] Créer `DashboardLayout.jsx`
- [x] Créer `DashboardHome.jsx`
- [x] Ajouter navigation admin avec `NavLink`
- [x] Ajouter état actif dans la sidebar
- [x] Ajouter déconnexion dans le layout
- [x] Rendre la sidebar responsive mobile
- [x] Créer endpoint `GET /api/admin/dashboard/stats`
- [x] Afficher les stats dans `DashboardHome`

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

- [x] Commencer Phase 1 : ajouter Spring Security + JWT dans le backend
- [ ] Commencer Phase 7 : créer le UI Kit Admin
