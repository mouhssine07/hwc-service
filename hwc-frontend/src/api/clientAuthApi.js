import clientApi from "./clientAxiosInstance.js";

export async function clientLogin(email, password) {
  const response = await clientApi.post("/client/auth/login", { email, password });
  return response.data;
}

export async function clientRegister(data) {
  const response = await clientApi.post("/client/auth/register", data);
  return response.data;
}

export async function getCurrentClient() {
  const response = await clientApi.get("/client/auth/me");
  return response.data;
}
