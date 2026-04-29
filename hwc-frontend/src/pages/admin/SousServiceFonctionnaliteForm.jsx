import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function SousServiceFonctionnaliteForm(props) {
  return <AdminEntityForm config={crudConfigs.sousServiceFonctionnalites} {...props} />;
}
