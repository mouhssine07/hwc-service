import clientApi from "./clientAxiosInstance.js";
import useClientAuthStore from "../store/clientAuthStore.js";

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

export async function getMarketingProactivePriority() {
  const response = await clientApi.get("/client/coach/marketing/sessions/proactive-priority");
  return response.data || null;
}

export async function deleteMarketingSession(sessionId) {
  await clientApi.delete(`/client/coach/marketing/sessions/${sessionId}`);
}

export async function sendMarketingMessage(sessionId, message, image, structuredInput) {
  const body = image ? new FormData() : { message, ...(structuredInput ? { structuredInput } : {}) };
  if (image) {
    body.append("message", message);
    body.append("image", image);
  }
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/messages`, body);
  return response.data;
}

export async function getMarketingInputSpec(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/input-spec`);
  return response.data;
}

export async function reactToMarketingMessage(sessionId, reaction) {
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/reactions`, { reaction });
  return response.data;
}

export async function streamMarketingMessage(sessionId, message, handlers = {}) {
  const baseUrl = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";
  const token = useClientAuthStore.getState().clientToken;
  const response = await fetch(`${baseUrl}/client/coach/marketing/sessions/${sessionId}/messages/stream`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "text/event-stream",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify({ message }),
  });
  if (!response.ok || !response.body) {
    const error = new Error("Le streaming du Coach a échoué.");
    error.status = response.status;
    throw error;
  }

  const reader = response.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";
  let currentEvent = "message";
  const dispatch = (event, data) => {
    if (event === "token") handlers.onToken?.(data);
    else if (event === "meta") handlers.onMeta?.(JSON.parse(data));
    else if (event === "done") handlers.onDone?.(JSON.parse(data));
    else if (event === "error") throw new Error(JSON.parse(data).message ?? "Le streaming du Coach a échoué.");
  };
  while (true) {
    const { done, value } = await reader.read();
    buffer += decoder.decode(value ?? new Uint8Array(), { stream: !done }).replace(/\r\n/g, "\n");
    let boundary;
    while ((boundary = buffer.indexOf("\n\n")) >= 0) {
      const block = buffer.slice(0, boundary);
      buffer = buffer.slice(boundary + 2);
      const dataParts = [];
      currentEvent = "message";
      block.split("\n").forEach((line) => {
        if (line.startsWith("event:")) currentEvent = line.slice(6).trim();
        if (line.startsWith("data:")) dataParts.push(line.slice(5).trimStart());
      });
      const data = dataParts.join("\n");
      if (data) dispatch(currentEvent, data);
    }
    if (done) break;
  }
}

export async function getMarketingSession(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}`);
  return response.data;
}

export async function correctMarketingState(sessionId, path, value) {
  const response = await clientApi.patch(`/client/coach/marketing/sessions/${sessionId}/state`, { path, value });
  return response.data;
}

export async function getMarketingCorrections(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/corrections`);
  return response.data;
}

export async function researchMarketingPublicPresence(sessionId) {
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/public-presence`);
  return response.data;
}

export async function decideMarketingPublicFinding(sessionId, findingIndex, accepted) {
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/public-presence/decision`, {
    findingIndex,
    accepted,
  });
  return response.data;
}

export async function getMarketingKpiDashboard(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/kpi-dashboard`);
  return response.data;
}

export async function addMarketingKpiMeasurement(sessionId, measurement) {
  const response = await clientApi.post(`/client/coach/marketing/sessions/${sessionId}/kpi-measurements`, measurement);
  return response.data;
}

export async function getMarketingFollowUp(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/follow-up`);
  return response.data;
}

export async function updateMarketingAction(sessionId, actionId, changes) {
  const response = await clientApi.patch(`/client/coach/marketing/sessions/${sessionId}/actions/${actionId}`, changes);
  return response.data;
}

export async function setMarketingCheckIns(sessionId, enabled) {
  const response = await clientApi.patch(`/client/coach/marketing/sessions/${sessionId}/check-ins`, { enabled });
  return response.data;
}

export async function getMarketingCheckIns(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/check-ins`);
  return response.data;
}

export async function getMarketingMessages(sessionId) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/messages`);
  return response.data;
}

export async function getMarketingFollowUpMessages(sessionId, limit = 20) {
  const response = await clientApi.get(`/client/coach/marketing/sessions/${sessionId}/follow-up/messages`, {
    params: { limit },
  });
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
