import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import ServiceFonctionnaliteForm from "./ServiceFonctionnaliteForm.jsx";

export default function ServiceFonctionnalitesPage() {
  return <AdminCrudPage config={crudConfigs.serviceFonctionnalites} formComponent={ServiceFonctionnaliteForm} />;
}
