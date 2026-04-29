import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function CertificationForm(props) {
  return <AdminEntityForm config={crudConfigs.certifications} {...props} />;
}
