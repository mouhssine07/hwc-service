import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import SousServiceFonctionnaliteForm from "./SousServiceFonctionnaliteForm.jsx";

export default function SousServiceFonctionnalitesPage() {
  return <AdminCrudPage config={crudConfigs.sousServiceFonctionnalites} formComponent={SousServiceFonctionnaliteForm} />;
}
