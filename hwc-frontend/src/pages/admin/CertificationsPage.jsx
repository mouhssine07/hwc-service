import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import CertificationForm from "./CertificationForm.jsx";

export default function CertificationsPage() {
  return <AdminCrudPage config={crudConfigs.certifications} formComponent={CertificationForm} />;
}
