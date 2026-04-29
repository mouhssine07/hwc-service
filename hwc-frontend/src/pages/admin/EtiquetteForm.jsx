import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function EtiquetteForm(props) {
  return <AdminEntityForm config={crudConfigs.etiquettes} {...props} />;
}
