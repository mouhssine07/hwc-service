import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function ServiceFonctionnaliteForm(props) {
  return <AdminEntityForm config={crudConfigs.serviceFonctionnalites} {...props} />;
}
