import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function SousServiceFaqForm(props) {
  return <AdminEntityForm config={crudConfigs.sousServiceFaqs} {...props} />;
}
