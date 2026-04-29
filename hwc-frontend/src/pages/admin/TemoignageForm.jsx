import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function TemoignageForm(props) {
  return <AdminEntityForm config={crudConfigs.temoignages} {...props} />;
}
