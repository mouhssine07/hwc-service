import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import SousServiceForm from "./SousServiceForm.jsx";

export default function SousServicesPage() {
  return <AdminCrudPage config={crudConfigs.sousServices} formComponent={SousServiceForm} />;
}
