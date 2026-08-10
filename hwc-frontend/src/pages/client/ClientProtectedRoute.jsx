import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useEffect } from "react";
import { getCurrentClient } from "../../api/clientAuthApi.js";
import ChatbotButton from "../../components/chat/ChatbotButton.jsx";
import useClientAuthStore from "../../store/clientAuthStore.js";

export default function ClientProtectedRoute() {
  const isClientAuthenticated = useClientAuthStore((state) => state.isClientAuthenticated);
  const clientLogout = useClientAuthStore((state) => state.clientLogout);
  const location = useLocation();
  const showChatbot = !location.pathname.startsWith("/client/coach");

  useEffect(() => {
    if (!isClientAuthenticated) {
      return undefined;
    }

    const validateSession = async () => {
      try {
        await getCurrentClient();
      } catch {
        clientLogout();
      }
    };

    const intervalId = window.setInterval(validateSession, 30000);
    return () => window.clearInterval(intervalId);
  }, [clientLogout, isClientAuthenticated]);

  if (!isClientAuthenticated) {
    return <Navigate to="/client/login" replace state={{ from: location }} />;
  }

  return (
    <>
      <Outlet />
      {showChatbot ? <ChatbotButton /> : null}
    </>
  );
}
