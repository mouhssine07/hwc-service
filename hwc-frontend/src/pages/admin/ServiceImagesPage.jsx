import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import ServiceImageForm from "./ServiceImageForm.jsx";

export default function ServiceImagesPage() {
  return <AdminCrudPage config={crudConfigs.serviceImages} formComponent={ServiceImageForm} />;
}
