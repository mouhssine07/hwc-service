import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function PaysForm(props) {
  return <AdminEntityForm config={crudConfigs.pays} {...props} />;
}
