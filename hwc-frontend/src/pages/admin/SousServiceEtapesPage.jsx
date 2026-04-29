import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import SousServiceEtapeForm from "./SousServiceEtapeForm.jsx";

export default function SousServiceEtapesPage() {
  return <AdminCrudPage config={crudConfigs.sousServiceEtapes} formComponent={SousServiceEtapeForm} />;
}
