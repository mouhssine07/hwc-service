import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function ClientConfianceForm(props) {
  return <AdminEntityForm config={crudConfigs.clientsConfiance} {...props} />;
}
