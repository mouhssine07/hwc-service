import clientApi from "./clientAxiosInstance.js";

export async function downloadDiagnosticReport(diagnosticId) {
  try {
    const response = await clientApi.get(`/client/diagnostics/${diagnosticId}/pdf`, {
      responseType: "blob",
    });
    return response.data;
  } catch (error) {
    throw new Error(await readApiBlobError(error));
  }
}

export async function getRapportsHistory() {
  const response = await clientApi.get("/client/rapports");
  return response.data;
}

export async function downloadExistingReport(rapportId) {
  try {
    const response = await clientApi.get(`/client/rapports/${rapportId}/download`, {
      responseType: "blob",
    });
    return response.data;
  } catch (error) {
    throw new Error(await readApiBlobError(error));
  }
}

export function savePdfBlob(blob, fileName) {
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

async function readApiBlobError(error) {
  const fallback = "Impossible de generer le rapport PDF.";
  const data = error?.response?.data;

  if (data instanceof Blob) {
    try {
      const text = await data.text();
      const json = JSON.parse(text);
      return json.message ?? fallback;
    } catch {
      return fallback;
    }
  }

  return error?.response?.data?.message ?? fallback;
}
