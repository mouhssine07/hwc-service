import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function SousServiceForm(props) {
  return <AdminEntityForm config={crudConfigs.sousServices} {...props} />;
}
