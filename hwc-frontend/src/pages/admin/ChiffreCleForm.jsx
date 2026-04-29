import AdminEntityForm from "./crud/AdminEntityForm.jsx";
import { crudConfigs } from "./crud/crudConfigs.js";

export default function ChiffreCleForm(props) {
  return <AdminEntityForm config={crudConfigs.chiffresCles} {...props} />;
}
