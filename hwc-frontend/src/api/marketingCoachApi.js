import clientApi from "./clientAxiosInstance.js";

export async function createMarketingSession() {
  const response = await clientApi.post("/client/coach/marketing/sessions", {
    serviceId: "MARKETING_STRATEGY",
  });
  return response.data;
}

export async function listMarketingSessions() {
  const response = await clientApi.get("/client/coach/marketing/sessions");
  return response.data;
}

export async function deleteMarketingSession(sessionId) {
  await clientApi.delete(`/client/coach/marketing/sessions/${sessionId}`);
}

export async function sendMarketingMessage(sessionId, message, image) {
  const body = image ? new FormData() : { message };
  if (image) {
    body.append("message", message);
    body.append("image", image);
  }
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/messages`, body);
  return response.data;
}

export async function getMarketingSession(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}`);
  return response.data;
}

export async function getMarketingMessages(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/messages`);
  return response.data;
}

export async function finalizeMarketingSession(sessionId) {
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/finalize`);
  return response.data;
}

export async function getMarketingDeliverable(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/deliverable`, {
    responseType: "text",
  });
  return response.data;
}
