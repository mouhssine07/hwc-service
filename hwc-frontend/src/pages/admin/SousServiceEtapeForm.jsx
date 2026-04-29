import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function SousServiceEtapeForm(props) {
  return <AdminEntityForm config={crudConfigs.sousServiceEtapes} {...props} />;
}
