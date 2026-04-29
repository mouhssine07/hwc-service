import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function ServiceImageForm(props) {
  return <AdminEntityForm config={crudConfigs.serviceImages} {...props} />;
}
