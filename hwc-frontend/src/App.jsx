import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Header from "./components/Header.jsx";
import Hero from "./components/Hero.jsx";
import Services from "./components/Services.jsx";
import Lab from "./components/Lab.jsx";
import Tools from "./components/Tools.jsx";
import About from "./components/About.jsx";
import Footer from "./components/Footer.jsx";
import FloatingContact from "./components/FloatingContact.jsx";
import RegieOutsourcing from "./pages/RegieOutsourcing.jsx";
import LeadershipCorporateEvents from "./pages/LeadershipCorporateEvents.jsx";
import HwcMethod from "./pages/HwcMethod.jsx";
import MarketingDigital from "./pages/MarketingDigital.jsx";
import ProspectionCommerciale from "./pages/ProspectionCommerciale.jsx";
import SupportAdministratif from "./pages/SupportAdministratif.jsx";
import AuditStrategie from "./pages/AuditStrategie.jsx";
import AcquisitionLeadGeneration from "./pages/AcquisitionLeadGeneration.jsx";
import ContenuReseauxSociaux from "./pages/ContenuReseauxSociaux.jsx";
import SitesWebApplications from "./pages/SitesWebApplications.jsx";
import SeoSea from "./pages/SeoSea.jsx";
import IaAutomatisations from "./pages/IaAutomatisations.jsx";
import PerformanceSousPression from "./pages/PerformanceSousPression.jsx";
import PublicServicePage, { PublicSousServicePage } from "./pages/PublicServicePage.jsx";
import LoginPage from "./pages/auth/LoginPage.jsx";
import DashboardHome from "./pages/admin/DashboardHome.jsx";
import AdminPlaceholder from "./pages/admin/AdminPlaceholder.jsx";
import AccompagnementsPage from "./pages/admin/AccompagnementsPage.jsx";
import CertificationsPage from "./pages/admin/CertificationsPage.jsx";
import ChiffresClesPage from "./pages/admin/ChiffresClesPage.jsx";
import ClientsConfiancePage from "./pages/admin/ClientsConfiancePage.jsx";
import ClientsPage from "./pages/admin/ClientsPage.jsx";
import DemandesContactPage from "./pages/admin/DemandesContactPage.jsx";
import EtiquettesPage from "./pages/admin/EtiquettesPage.jsx";
import PaysPage from "./pages/admin/PaysPage.jsx";
import ReglesRecommandationPage from "./pages/admin/ReglesRecommandationPage.jsx";
import ServiceFonctionnalitesPage from "./pages/admin/ServiceFonctionnalitesPage.jsx";
import ServiceImagesPage from "./pages/admin/ServiceImagesPage.jsx";
import ServicesPage from "./pages/admin/ServicesPage.jsx";
import SousServiceAvantagesPage from "./pages/admin/SousServiceAvantagesPage.jsx";
import SousServiceEtapesPage from "./pages/admin/SousServiceEtapesPage.jsx";
import SousServiceFaqsPage from "./pages/admin/SousServiceFaqsPage.jsx";
import SousServiceFonctionnalitesPage from "./pages/admin/SousServiceFonctionnalitesPage.jsx";
import SousServicesPage from "./pages/admin/SousServicesPage.jsx";
import TemoignagesPage from "./pages/admin/TemoignagesPage.jsx";
import DashboardLayout from "./components/admin/layout/DashboardLayout.jsx";
import ProtectedRoute from "./components/admin/layout/ProtectedRoute.jsx";
import ClientLoginPage from "./pages/client/ClientLoginPage.jsx";
import ClientRegisterPage from "./pages/client/ClientRegisterPage.jsx";
import ClientProtectedRoute from "./pages/client/ClientProtectedRoute.jsx";
import DiagnosticStartPage from "./pages/client/DiagnosticStartPage.jsx";
import DiagnosticQuestionsPage from "./pages/client/DiagnosticQuestionsPage.jsx";
import DiagnosticResultatPage from "./pages/client/DiagnosticResultatPage.jsx";
import RecommandationsPage from "./pages/client/RecommandationsPage.jsx";

function Home() {
  return (
    <>
      <Hero />
      <About />
      <Services />
      <Lab />
      <Tools />
    </>
  );
}

export default function App() {
  const location = useLocation();
  const isStandaloneRoute =
    location.pathname.startsWith("/admin") ||
    location.pathname.startsWith("/client") ||
    location.pathname === "/login";

  return (
    <div className="min-h-screen bg-background text-foreground">
      {!isStandaloneRoute ? <Header /> : null}
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/lab" element={<Lab page />} />
          <Route path="/lab/:articleId" element={<Lab article />} />
          <Route path="/outils" element={<Tools page />} />
          <Route path="/a-propos" element={<About page />} />
          <Route path="/offres/regie-outsourcing" element={<RegieOutsourcing />} />
          <Route path="/offres/regie-outsourcing/marketing-digital" element={<MarketingDigital />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/audit-strategie" element={<AuditStrategie />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/acquisition-lead-generation" element={<AcquisitionLeadGeneration />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/contenu-reseaux-sociaux" element={<ContenuReseauxSociaux />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/sites-web-applications" element={<SitesWebApplications />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/referencement-seo-sea" element={<SeoSea />} />
          <Route path="/offres/regie-outsourcing/marketing-digital/ia-automatisations" element={<IaAutomatisations />} />
          <Route path="/offres/regie-outsourcing/prospection-commerciale" element={<ProspectionCommerciale />} />
          <Route path="/offres/regie-outsourcing/support-client" element={<SupportAdministratif />} />
          <Route path="/offres/leadership-corporate-events" element={<LeadershipCorporateEvents />} />
          <Route path="/offres/performance-sous-pression" element={<PerformanceSousPression />} />
          <Route path="/offres/services/:serviceId" element={<PublicServicePage />} />
          <Route path="/offres/sous-services/:sousServiceId" element={<PublicSousServicePage />} />
          <Route path="/hwc-method" element={<HwcMethod />} />
          <Route path="/methode-hwc-360" element={<HwcMethod />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/client/login" element={<ClientLoginPage />} />
          <Route path="/client/register" element={<ClientRegisterPage />} />
          <Route path="/client" element={<ClientProtectedRoute />}>
            <Route index element={<Navigate to="/client/diagnostic" replace />} />
            <Route path="diagnostic" element={<DiagnosticStartPage />} />
            <Route path="diagnostic/:diagnosticId/questions" element={<DiagnosticQuestionsPage />} />
            <Route path="diagnostic/:diagnosticId/resultat" element={<DiagnosticResultatPage />} />
            <Route path="diagnostic/:diagnosticId/recommandations" element={<RecommandationsPage />} />
          </Route>
          <Route path="/admin" element={<ProtectedRoute />}>
            <Route element={<DashboardLayout />}>
              <Route index element={<DashboardHome />} />
              <Route path="services" element={<ServicesPage />} />
              <Route path="sous-services" element={<SousServicesPage />} />
              <Route path="etiquettes" element={<EtiquettesPage />} />
              <Route path="temoignages" element={<TemoignagesPage />} />
              <Route path="certifications" element={<CertificationsPage />} />
              <Route path="pays" element={<PaysPage />} />
              <Route path="chiffres-cles" element={<ChiffresClesPage />} />
              <Route path="clients-confiance" element={<ClientsConfiancePage />} />
              <Route path="clients" element={<ClientsPage />} />
              <Route path="regles-recommandation" element={<ReglesRecommandationPage />} />
              <Route path="demandes-contact" element={<DemandesContactPage />} />
              <Route path="service-fonctionnalites" element={<ServiceFonctionnalitesPage />} />
              <Route path="service-images" element={<ServiceImagesPage />} />
              <Route path="sous-service-fonctionnalites" element={<SousServiceFonctionnalitesPage />} />
              <Route path="sous-service-avantages" element={<SousServiceAvantagesPage />} />
              <Route path="sous-service-etapes" element={<SousServiceEtapesPage />} />
              <Route path="sous-service-faqs" element={<SousServiceFaqsPage />} />
              <Route path="accompagnements" element={<AccompagnementsPage />} />
              <Route path="*" element={<AdminPlaceholder />} />
            </Route>
          </Route>
          <Route path="*" element={<Home />} />
        </Routes>
      </main>
      {!isStandaloneRoute ? <Footer /> : null}
      {!isStandaloneRoute ? <FloatingContact /> : null}
      <Toaster position="top-right" />
    </div>
  );
}
