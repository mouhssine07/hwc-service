import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import AccompagnementForm from "./AccompagnementForm.jsx";

export default function AccompagnementsPage() {
  return <AdminCrudPage config={crudConfigs.accompagnements} formComponent={AccompagnementForm} />;
}
