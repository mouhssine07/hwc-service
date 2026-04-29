import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import ServiceForm from "./ServiceForm.jsx";

export default function ServicesPage() {
  return <AdminCrudPage config={crudConfigs.services} formComponent={ServiceForm} />;
}
