import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import SousServiceAvantageForm from "./SousServiceAvantageForm.jsx";

export default function SousServiceAvantagesPage() {
  return <AdminCrudPage config={crudConfigs.sousServiceAvantages} formComponent={SousServiceAvantageForm} />;
}
