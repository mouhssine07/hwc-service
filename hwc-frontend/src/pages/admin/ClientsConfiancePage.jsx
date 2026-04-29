import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import ClientConfianceForm from "./ClientConfianceForm.jsx";

export default function ClientsConfiancePage() {
  return <AdminCrudPage config={crudConfigs.clientsConfiance} formComponent={ClientConfianceForm} />;
}
