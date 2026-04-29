import AdminCrudPage from "./crud/AdminCrudPage.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";
import ChiffreCleForm from "./ChiffreCleForm.jsx";

export default function ChiffresClesPage() {
  return <AdminCrudPage config={crudConfigs.chiffresCles} formComponent={ChiffreCleForm} />;
}
