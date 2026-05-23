import clientApi from "./clientAxiosInstance.js";

export async function getDiagnosticQuestions() {
  const response = await clientApi.get("/client/diagnostics/questions");
  return response.data;
}

export async function startDiagnostic() {
  const response = await clientApi.post("/client/diagnostics/start");
  return response.data;
}

export async function answerDiagnosticQuestion(diagnosticId, payload) {
  const response = await clientApi.post(`/client/diagnostics/${diagnosticId}/reponses`, payload);
  return response.data;
}

export async function finalizeDiagnostic(diagnosticId) {
  const response = await clientApi.post(`/client/diagnostics/${diagnosticId}/finalize`);
  return response.data;
}

export async function getDiagnostic(diagnosticId) {
  const response = await clientApi.get(`/client/diagnostics/${diagnosticId}`);
  return response.data;
}

export async function getDiagnosticHistory() {
  const response = await clientApi.get("/client/diagnostics/history");
  return response.data;
}
