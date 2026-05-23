import api from "./axiosInstance.js";

export async function listRecommendationRules() {
  const response = await api.get("/admin/regles-recommandation");
  return response.data;
}

export async function createRecommendationRule(payload) {
  const response = await api.post("/admin/regles-recommandation", payload);
  return response.data;
}

export async function updateRecommendationRule(id, payload) {
  const response = await api.put(`/admin/regles-recommandation/${id}`, payload);
  return response.data;
}

export async function deleteRecommendationRule(id) {
  await api.delete(`/admin/regles-recommandation/${id}`);
}

export async function listDiagnosticCategories() {
  const response = await api.get("/admin/regles-recommandation/categories");
  return response.data;
}
