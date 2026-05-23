import { Navigate, Outlet, useLocation } from "react-router-dom";
import useClientAuthStore from "../../store/clientAuthStore.js";

export default function ClientProtectedRoute() {
  const isClientAuthenticated = useClientAuthStore((state) => state.isClientAuthenticated);
  const location = useLocation();

  if (!isClientAuthenticated) {
    return <Navigate to="/client/login" replace state={{ from: location }} />;
  }

  return <Outlet />;
}
