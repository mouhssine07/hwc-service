import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function DemandesContactPage() {
  return <AdminCrudPage config={crudConfigs.demandesContact} />;
}
