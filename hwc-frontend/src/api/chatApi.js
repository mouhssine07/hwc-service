import clientApi from "./clientAxiosInstance.js";

export async function sendChatMessage({ conversationId, diagnosticId, coachObjectifId, message }) {
  const response = await clientApi.post("/client/chat/message", {
    conversationId,
    diagnosticId,
    coachObjectifId,
    message,
  });
  return response.data;
}

export async function listChatConversations() {
  const response = await clientApi.get("/client/chat/conversations");
  return response.data;
}

export async function listChatMessages(conversationId) {
  const response = await clientApi.get(`/client/chat/conversations/${conversationId}/messages`);
  return response.data;
}
