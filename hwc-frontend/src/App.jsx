import { Route, Routes, useLocation } from "react-router-dom";
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
import LoginPage from "./pages/auth/LoginPage.jsx";
import DashboardHome from "./pages/admin/DashboardHome.jsx";
import AdminPlaceholder from "./pages/admin/AdminPlaceholder.jsx";
import DashboardLayout from "./components/admin/layout/DashboardLayout.jsx";
import ProtectedRoute from "./components/admin/layout/ProtectedRoute.jsx";

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
  const isStandaloneRoute = location.pathname.startsWith("/admin") || location.pathname === "/login";

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
          <Route path="/hwc-method" element={<HwcMethod />} />
          <Route path="/methode-hwc-360" element={<HwcMethod />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/admin" element={<ProtectedRoute />}>
            <Route element={<DashboardLayout />}>
              <Route index element={<DashboardHome />} />
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
