import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import EtiquetteForm from "./EtiquetteForm.jsx";

export default function EtiquettesPage() {
  return <AdminCrudPage config={crudConfigs.etiquettes} formComponent={EtiquetteForm} />;
}
