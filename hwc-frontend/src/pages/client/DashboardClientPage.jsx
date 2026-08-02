import { ArrowRight, Download, Loader2, LogOut, RefreshCcw } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { getClientDashboard } from "../../api/dashboardApi.js";
import { downloadDiagnosticReport, downloadExistingReport, getRapportsHistory, savePdfBlob } from "../../api/rapportApi.js";
import AlertesCritiques from "../../components/dashboard/AlertesCritiques.jsx";
import BenchmarkSecteur from "../../components/dashboard/BenchmarkSecteur.jsx";
import CategoryDetailCards from "../../components/dashboard/CategoryDetailCards.jsx";
import HistoriqueBarChart from "../../components/dashboard/HistoriqueBarChart.jsx";
import KpiCards from "../../components/dashboard/KpiCards.jsx";
import PlanActionTimeline from "../../components/dashboard/PlanActionTimeline.jsx";
import RadarChartScores from "../../components/dashboard/RadarChartScores.jsx";
import ScoreGlobalDonut from "../../components/dashboard/ScoreGlobalDonut.jsx";
import ServicesRecommandes from "../../components/dashboard/ServicesRecommandes.jsx";
import useClientAuthStore from "../../store/clientAuthStore.js";

export default function DashboardClientPage() {
  const navigate = useNavigate();
  const clientLogout = useClientAuthStore((state) => state.clientLogout);
  const [dashboard, setDashboard] = useState(null);
  const [rapports, setRapports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [downloading, setDownloading] = useState(false);

  useEffect(() => {
    Promise.all([getClientDashboard(), getRapportsHistory()])
      .then(([dashboardData, rapportsData]) => {
        setDashboard(dashboardData);
        setRapports(rapportsData);
      })
      .catch(() => toast.error("Impossible de charger le dashboard client"))
      .finally(() => setLoading(false));
  }, []);

  const handleLogout = () => {
    clientLogout();
    navigate("/client/login", { replace: true });
  };

  const handleDownloadReport = async () => {
    setDownloading(true);
    try {
      const blob = await downloadDiagnosticReport(dashboard.dernierDiagnosticId);
      savePdfBlob(blob, `rapport-hwc-diagnostic-${dashboard.dernierDiagnosticId}.pdf`);
      setRapports(await getRapportsHistory());
      toast.success("Rapport PDF genere.");
    } catch (error) {
      toast.error(error.message);
    } finally {
      setDownloading(false);
    }
  };

  const handleDownloadExistingReport = async (rapport) => {
    setDownloading(true);
    try {
      const blob = await downloadExistingReport(rapport.id);
      savePdfBlob(blob, rapport.fileName ?? `rapport-hwc-${rapport.id}.pdf`);
    } catch (error) {
      toast.error(error.message);
    } finally {
      setDownloading(false);
    }
  };

  if (loading) {
    return (
      <main className="min-h-screen bg-muted/30 px-4 py-10">
        <div className="mx-auto flex max-w-6xl items-center gap-3 rounded-lg border border-border bg-card p-6 shadow-card">
          <Loader2 className="h-5 w-5 animate-spin text-primary" />
          <span className="text-sm font-semibold text-muted-foreground">Chargement du dashboard...</span>
        </div>
      </main>
    );
  }

  if (!dashboard?.disponible) {
    return (
      <main className="min-h-screen bg-muted/30">
        <ClientTopbar onLogout={handleLogout} />
        <section className="mx-auto max-w-4xl px-4 py-10">
          <div className="rounded-lg border border-border bg-card p-8 shadow-elevated">
            <p className="text-sm font-semibold uppercase tracking-wider text-primary">Dashboard client</p>
            <h1 className="mt-3 font-display text-3xl font-bold text-foreground">Aucun diagnostic finalise</h1>
            <p className="mt-4 text-sm leading-6 text-muted-foreground">
              {dashboard?.message ?? "Lancez un diagnostic pour generer votre tableau de bord."}
            </p>
            <button className="btn btn-hero mt-6" type="button" onClick={() => navigate("/client/diagnostic")}>
              Lancer un diagnostic
              <ArrowRight className="ml-2 h-4 w-4" />
            </button>
          </div>
        </section>
      </main>
    );
  }

  return (
    <main className="min-h-screen bg-muted/30">
      <ClientTopbar onLogout={handleLogout} />
      <div className="mx-auto max-w-7xl space-y-5 px-4 py-6">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-lg border border-border bg-card px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
            onClick={() => navigate("/client/diagnostic")}
          >
            Espace diagnostic
          </button>
          <button className="btn btn-hero" type="button" onClick={() => navigate("/client/diagnostic")}>
            <RefreshCcw className="mr-2 h-4 w-4" />
            Relancer un diagnostic
          </button>
          <button className="btn btn-outline-hero" type="button" disabled={downloading} onClick={handleDownloadReport}>
            <Download className="mr-2 h-4 w-4" />
            {downloading ? "Generation..." : "Rapport PDF"}
          </button>
        </div>

        <ScoreGlobalDonut score={dashboard.scoreGlobal} niveau={dashboard.niveauGlobal} />
        <KpiCards dashboard={dashboard} />

        <section className="grid gap-5 xl:grid-cols-[1fr_1fr]">
          <RadarChartScores scores={dashboard.scoresParCategorie} />
          <HistoriqueBarChart historique={dashboard.historiqueScores} />
        </section>

        <section className="grid gap-5 xl:grid-cols-[1.3fr_0.7fr]">
          <CategoryDetailCards scores={dashboard.scoresParCategorie} />
          <div className="space-y-5">
            <AlertesCritiques alertes={dashboard.alertesCritiques} />
            <BenchmarkSecteur benchmark={dashboard.benchmarkSecteur} score={dashboard.scoreGlobal} />
          </div>
        </section>

        <section>
          <PlanActionTimeline actions={dashboard.planActionResume} />
        </section>

        <ServicesRecommandes />

        <section className="rounded-lg border border-border bg-card p-5 shadow-card">
          <div className="mb-4">
            <h2 className="font-display text-xl font-bold text-foreground">Historique des rapports PDF</h2>
            <p className="text-sm text-muted-foreground">Rapports generes depuis vos diagnostics finalises</p>
          </div>
          {rapports.length ? (
            <div className="grid gap-3 md:grid-cols-2">
              {rapports.slice(0, 4).map((rapport) => (
                <article key={rapport.id} className="flex items-center justify-between gap-3 rounded-lg border border-border p-4">
                  <div className="min-w-0">
                    <h3 className="truncate text-sm font-bold text-foreground">{rapport.fileName}</h3>
                    <p className="mt-1 text-xs text-muted-foreground">
                      Diagnostic #{rapport.diagnosticId} -{" "}
                      {rapport.dateGeneration ? new Date(rapport.dateGeneration).toLocaleDateString("fr-FR") : "--"}
                    </p>
                  </div>
                  <button
                    type="button"
                    className="inline-flex h-9 shrink-0 items-center gap-2 rounded-lg border border-border px-3 text-xs font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
                    disabled={downloading}
                    onClick={() => handleDownloadExistingReport(rapport)}
                  >
                    <Download className="h-4 w-4" />
                    PDF
                  </button>
                </article>
              ))}
            </div>
          ) : (
            <p className="rounded-lg border border-dashed border-border p-4 text-sm text-muted-foreground">
              Aucun rapport PDF genere pour le moment.
            </p>
          )}
        </section>
      </div>
    </main>
  );
}

function ClientTopbar({ onLogout }) {
  return (
    <header className="border-b border-border bg-card">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4">
        <img src="/images/Logo_HWC-J-9-1DQr.png" alt="Harmony Works Consulting" className="h-10 w-auto" />
        <button
          type="button"
          className="inline-flex h-10 items-center gap-2 rounded-lg border border-border px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
          onClick={onLogout}
        >
          <LogOut className="h-4 w-4" />
          Deconnexion
        </button>
      </div>
    </header>
  );
}
