import api from "./axiosInstance.js";

export async function getDashboardStats() {
  const response = await api.get("/admin/dashboard/stats");
  return response.data;
}
