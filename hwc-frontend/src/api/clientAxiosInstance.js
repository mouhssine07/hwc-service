import axios from "axios";
import useClientAuthStore from "../store/clientAuthStore.js";

const clientApi = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

clientApi.interceptors.request.use((config) => {
  const token = useClientAuthStore.getState().clientToken;

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

clientApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useClientAuthStore.getState().clientLogout();

      if (window.location.pathname !== "/client/login") {
        window.location.assign("/client/login");
      }
    }

    return Promise.reject(error);
  },
);

export default clientApi;
