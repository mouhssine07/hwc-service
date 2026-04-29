import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function ServiceForm(props) {
  return <AdminEntityForm config={crudConfigs.services} {...props} />;
}
