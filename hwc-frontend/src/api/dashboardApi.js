import api from "./axiosInstance.js";
import clientApi from "./clientAxiosInstance.js";

export async function getDashboardStats() {
  const response = await api.get("/admin/dashboard/stats");
  return response.data;
}

export async function getClientDashboard() {
  const response = await clientApi.get("/client/dashboard");
  return response.data;
}
