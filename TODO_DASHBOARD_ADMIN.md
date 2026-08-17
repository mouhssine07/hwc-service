# TODO Dashboard Admin HWC

Checklist opérationnelle pour continuer le développement du back-office admin.

Référence détaillée : `hwc-frontend/PLAN_DASHBOARD_ADMIN.md`

---

## 1. Préparer Le Backend Sécurisé - Terminé

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

## 2. Créer Les APIs Admin - Terminé

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
- [x] Tester chaque CRUD avec Postman ou Thunder Client

---

## 3. Créer Les APIs Publiques - Terminé

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

## 4. Préparer Le Frontend Admin - Terminé

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

## 5. Authentification Frontend - Terminé

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

## 6. Layout Dashboard - Terminé

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

## 7. UI Kit Admin - Terminé

- [x] Créer `Button.jsx`
- [x] Créer `Input.jsx`
- [x] Créer `Textarea.jsx`
- [x] Créer `Select.jsx`
- [x] Créer `Modal.jsx`
- [x] Créer `ConfirmDialog.jsx`
- [x] Créer `Table.jsx`
- [x] Créer `Pagination.jsx`
- [x] Créer `Card.jsx`
- [x] Créer `Badge.jsx`
- [x] Créer `Spinner.jsx`
- [x] Créer `EmptyState.jsx`
- [x] Vérifier la cohérence responsive des composants

---

## 8. Pages CRUD Frontend - Terminé

- [x] Créer `EtiquettesPage.jsx` + `EtiquetteForm.jsx`
- [x] Créer `PaysPage.jsx` + `PaysForm.jsx`
- [x] Créer `ChiffresClesPage.jsx` + `ChiffreCleForm.jsx`
- [x] Créer `ServicesPage.jsx` + `ServiceForm.jsx`
- [x] Créer `SousServicesPage.jsx` + `SousServiceForm.jsx`
- [x] Créer `ClientsConfiancePage.jsx` + `ClientConfianceForm.jsx`
- [x] Créer `TemoignagesPage.jsx` + `TemoignageForm.jsx`
- [x] Créer `CertificationsPage.jsx` + `CertificationForm.jsx`
- [x] Créer `ServiceFonctionnalitesPage.jsx` + `ServiceFonctionnaliteForm.jsx`
- [x] Créer `ServiceImagesPage.jsx` + `ServiceImageForm.jsx`
- [x] Créer `SousServiceFonctionnalitesPage.jsx` + `SousServiceFonctionnaliteForm.jsx`
- [x] Créer `SousServiceAvantagesPage.jsx` + `SousServiceAvantageForm.jsx`
- [x] Créer `SousServiceEtapesPage.jsx` + `SousServiceEtapeForm.jsx`
- [x] Créer `SousServiceFaqsPage.jsx` + `SousServiceFaqForm.jsx`
- [x] Créer `AccompagnementsPage.jsx` + `AccompagnementForm.jsx`
- [x] Créer `DemandesContactPage.jsx`
- [x] Ajouter recherche sur les pages principales
- [x] Ajouter pagination sur les tableaux
- [x] Ajouter confirmation suppression
- [x] Ajouter toasts succès/erreur
- [x] Ajouter loading states
- [x] Ajouter empty states

---

## 9. Upload Images - Terminé

- [x] Créer `FileUploadController`
- [x] Créer endpoint `POST /api/admin/upload/image`
- [x] Créer dossier backend `uploads/images`
- [x] Générer les noms de fichiers avec UUID
- [x] Valider formats `jpg`, `png`, `webp`
- [x] Limiter taille fichier à `5MB`
- [x] Exposer `/uploads/**`
- [x] Créer `src/api/uploadApi.js`
- [x] Créer `ImageUpload.jsx`
- [x] Intégrer upload dans `ClientConfianceForm`
- [x] Intégrer upload dans `TemoignageForm`
- [x] Intégrer upload dans `CertificationForm`
- [x] Intégrer upload dans `ServiceImageForm`
- [x] Tester upload depuis le dashboard

---

## 10. Connexion Progressive Du Site Vitrine À L'API - Terminé

- [x] Connecter les chiffres clés à `/api/public/chiffres-cles`
- [x] Connecter les clients de confiance à `/api/public/clients-confiance`
- [x] Connecter les pays à `/api/public/pays`
- [x] Connecter les certifications à `/api/public/certifications`
- [x] Connecter les témoignages à `/api/public/temoignages`
- [x] Connecter les services à `/api/public/services`
- [x] Connecter les sous-services à `/api/public/sous-services/{id}`
- [x] Connecter les images services
- [x] Connecter le formulaire contact à `/api/public/demandes-contact`
- [x] Ajouter fallback statique si l'API ne répond pas
- [x] Tester que le site vitrine reste fonctionnel API éteinte

---

## 11. Validation Finale - Terminée sauf mot de passe admin

- [x] Tester build frontend avec `npm.cmd run build`
- [x] Tester backend avec `mvn test`
- [x] Tester login admin complet
- [x] Tester chaque page CRUD
- [x] Tester upload images
- [x] Tester responsive dashboard mobile
- [x] Tester responsive site vitrine
- [x] Vérifier que le dashboard n'affiche pas header/footer vitrine
- [x] Vérifier que les routes publiques restent accessibles
- [x] Vérifier que les routes admin sont protégées
- [ ] Changer le mot de passe admin par défaut

---

## 12. Phase 2 - Module 7A Chatbot IA - Terminé

- [x] Créer les entités `ChatConversation` et `ChatMessage`
- [x] Créer `ChatConversationRepository` et `ChatMessageRepository`
- [x] Créer les DTOs chat
- [x] Créer `ChatService` et `ChatServiceImpl`
- [x] Créer `ChatController`
- [x] Ajouter les endpoints client `/api/client/chat/message`, `/api/client/chat/conversations`, `/api/client/chat/conversations/{id}/messages`
- [x] Intégrer Ollama local avec `OLLAMA_BASE_URL` et `OLLAMA_MODEL`
- [x] Utiliser `llama3.2:latest` comme modèle local par défaut
- [x] Ajouter contexte client + diagnostic + scores + recommandations
- [x] Empêcher le mélange entre clients/diagnostics dans le prompt
- [x] Forcer une réponse structurée moderne et lisible
- [x] Ajouter mémoire conversationnelle avec historique sauvegardé
- [x] Recharger la dernière conversation quand la fenêtre chatbot est fermée/réouverte
- [x] Limiter la mémoire envoyée au LLM avec `CHAT_MEMORY_MAX_MESSAGES`
- [x] Ajouter nettoyage automatique des conversations anciennes avec `CHAT_RETENTION_DAYS`
- [x] Ajouter l'interface frontend `ChatbotButton`, `ChatbotWindow`, `ChatMessage`, `ChatInput`, `ChatSuggestions`
- [x] Intégrer le bouton Assistant IA dans les routes client protégées
- [x] Tester backend avec `mvn test`
- [x] Tester frontend avec `npm.cmd run build`

---

## Prochaine Tâche À Faire Maintenant

- [x] Commencer Phase 1 : ajouter Spring Security + JWT dans le backend
- [x] Commencer Phase 7 : créer le UI Kit Admin
- [x] Commencer Phase 8 : créer les pages CRUD Frontend
- [x] Commencer Phase 9 : upload images
- [x] Commencer Phase 10 : connecter le site vitrine à l'API
- [x] Commencer et terminer Module 7A : Assistant IA Chatbot avec Ollama local
- [ ] Commencer Module 7B : Coach IA hebdomadaire
