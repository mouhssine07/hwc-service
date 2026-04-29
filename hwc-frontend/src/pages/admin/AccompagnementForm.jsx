import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function AccompagnementForm(props) {
  return <AdminEntityForm config={crudConfigs.accompagnements} {...props} />;
}
