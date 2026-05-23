import api from "./axiosInstance.js";

export async function listAdminClients(params = {}) {
  const response = await api.get("/admin/users/clients", { params });
  return response.data;
}

export async function updateAdminClientStatus(id, actif) {
  const response = await api.patch(`/admin/users/clients/${id}/status`, { actif });
  return response.data;
}
