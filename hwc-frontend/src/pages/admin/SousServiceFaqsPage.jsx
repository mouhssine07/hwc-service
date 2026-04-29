import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import SousServiceFaqForm from "./SousServiceFaqForm.jsx";

export default function SousServiceFaqsPage() {
  return <AdminCrudPage config={crudConfigs.sousServiceFaqs} formComponent={SousServiceFaqForm} />;
}
