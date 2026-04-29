import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import PaysForm from "./PaysForm.jsx";

export default function PaysPage() {
  return <AdminCrudPage config={crudConfigs.pays} formComponent={PaysForm} />;
}
