import clientApi from "./clientAxiosInstance.js";

export async function getCurrentCoachWeek(diagnosticId) {
  const response = await clientApi.get("/client/coach/current-week", { params: diagnosticId ? { diagnosticId } : undefined });
  return response.data;
}

export async function completeCoachObjective(objectiveId) {
  const response = await clientApi.patch(`/client/coach/objectifs/${objectiveId}/complete`);
  return response.data;
}

export async function updateCoachObjectiveProgress(objectiveId, progression) {
  const response = await clientApi.patch(`/client/coach/objectifs/${objectiveId}/progress`, progression);
  return response.data;
}

export async function getCoachHistory(diagnosticId) {
  const response = await clientApi.get("/client/coach/history", { params: diagnosticId ? { diagnosticId } : undefined });
  return response.data;
}
