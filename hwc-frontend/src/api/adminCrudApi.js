import api from "./axiosInstance.js";

export async function listAdminResource(resource, params = {}) {
  const response = await api.get(`/admin/${resource}`, { params });
  return response.data;
}

export async function getAdminResource(resource, id) {
  const response = await api.get(`/admin/${resource}/${id}`);
  return response.data;
}

export async function createAdminResource(resource, payload) {
  const response = await api.post(`/admin/${resource}`, payload);
  return response.data;
}

export async function updateAdminResource(resource, id, payload) {
  const response = await api.put(`/admin/${resource}/${id}`, payload);
  return response.data;
}

export async function deleteAdminResource(resource, id) {
  await api.delete(`/admin/${resource}/${id}`);
}
