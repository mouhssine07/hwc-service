import api from "./axiosInstance.js";

export async function getPublicClientsConfiance() {
  const response = await api.get("/public/clients-confiance");
  return response.data;
}

export async function getPublicChiffresCles() {
  const response = await api.get("/public/chiffres-cles");
  return response.data;
}

export async function getPublicPays() {
  const response = await api.get("/public/pays");
  return response.data;
}

export async function getPublicCertifications() {
  const response = await api.get("/public/certifications");
  return response.data;
}

export async function getPublicTemoignages() {
  const response = await api.get("/public/temoignages");
  return response.data;
}

export async function getPublicServices() {
  const response = await api.get("/public/services");
  return response.data;
}

export async function getPublicService(id) {
  const response = await api.get(`/public/services/${id}`);
  return response.data;
}

export async function getPublicServiceImages() {
  const response = await api.get("/public/service-images");
  return response.data;
}

export async function getPublicSousService(id) {
  const response = await api.get(`/public/sous-services/${id}`);
  return response.data;
}

export async function createPublicDemandeContact(email) {
  const response = await api.post("/public/demandes-contact", { email });
  return response.data;
}
