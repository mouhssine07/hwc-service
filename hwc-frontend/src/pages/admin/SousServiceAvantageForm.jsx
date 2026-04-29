import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function SousServiceAvantageForm(props) {
  return <AdminEntityForm config={crudConfigs.sousServiceAvantages} {...props} />;
}
