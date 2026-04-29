import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import TemoignageForm from "./TemoignageForm.jsx";

export default function TemoignagesPage() {
  return <AdminCrudPage config={crudConfigs.temoignages} formComponent={TemoignageForm} />;
}
