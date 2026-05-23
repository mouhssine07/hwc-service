import { ArrowRight, ClipboardList, History, Loader2, LogOut, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { deleteDiagnostic, getDiagnosticHistory, startDiagnostic } from "../../api/diagnosticApi.js";
import useClientAuthStore from "../../store/clientAuthStore.js";

export default function DiagnosticStartPage() {
  const navigate = useNavigate();
  const clientUser = useClientAuthStore((state) => state.clientUser);
  const clientLogout = useClientAuthStore((state) => state.clientLogout);
  const [loading, setLoading] = useState(false);
  const [history, setHistory] = useState([]);
  const [deletingId, setDeletingId] = useState(null);

  const loadHistory = () => {
    return getDiagnosticHistory()
      .then(setHistory)
      .catch(() => setHistory([]));
  };

  useEffect(() => {
    loadHistory();
  }, []);

  const handleStart = async () => {
    setLoading(true);

    try {
      const diagnostic = await startDiagnostic();
      navigate(`/client/diagnostic/${diagnostic.diagnosticId}/questions`);
    } catch {
      toast.error("Impossible de demarrer le diagnostic");
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    clientLogout();
    navigate("/client/login", { replace: true });
  };

  const handleDelete = async (diagnosticId) => {
    const confirmed = window.confirm("Supprimer ce diagnostic de votre historique ?");
    if (!confirmed) {
      return;
    }

    setDeletingId(diagnosticId);
    try {
      await deleteDiagnostic(diagnosticId);
      toast.success("Diagnostic supprime.");
      await loadHistory();
    } catch {
      toast.error("Impossible de supprimer ce diagnostic.");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <main className="min-h-screen bg-muted/30">
      <header className="border-b border-border bg-card">
        <div className="mx-auto flex h-20 max-w-6xl items-center justify-between px-4">
          <img src="/images/Logo_HWC-J-9-1DQr.png" alt="Harmony Works Consulting" className="h-11 w-auto" />
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-lg border border-border px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
            onClick={handleLogout}
          >
            <LogOut className="h-4 w-4" />
            Deconnexion
          </button>
        </div>
      </header>

      <section className="mx-auto grid max-w-6xl gap-8 px-4 py-10 lg:grid-cols-[1.15fr_0.85fr]">
        <div className="rounded-2xl border border-border bg-card p-8 shadow-card">
          <div className="mb-6 flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
            <ClipboardList className="h-7 w-7 text-primary" />
          </div>
          <p className="mb-3 text-sm font-semibold uppercase tracking-wider text-primary">
            Diagnostic HWC 360
          </p>
          <h1 className="font-display text-4xl font-bold leading-tight text-foreground md:text-5xl">
            Bonjour {clientUser?.prenom ?? "client"}, mesurez la maturite de votre entreprise.
          </h1>
          <p className="mt-5 max-w-2xl text-base leading-7 text-muted-foreground">
            Repondez a 25 questions reparties sur 5 categories. Le resultat calcule votre score global,
            vos scores par axe et votre niveau de maturite.
          </p>

          <div className="mt-8 grid gap-3 sm:grid-cols-3">
            {["25 questions", "5 categories", "Score instantane"].map((item) => (
              <div key={item} className="rounded-xl border border-border bg-muted/40 p-4 text-sm font-semibold">
                {item}
              </div>
            ))}
          </div>

          <button className="btn btn-hero mt-8 h-12 text-base" type="button" onClick={handleStart} disabled={loading}>
            {loading ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : null}
            Lancer le diagnostic
            {!loading ? <ArrowRight className="ml-2 h-5 w-5" /> : null}
          </button>
        </div>

        <aside className="rounded-2xl border border-border bg-card p-6 shadow-card">
          <div className="mb-5 flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-secondary/10">
              <History className="h-5 w-5 text-secondary" />
            </div>
            <div>
              <h2 className="font-display text-2xl font-bold">Historique</h2>
              <p className="text-sm text-muted-foreground">Vos derniers diagnostics</p>
            </div>
          </div>

          <div className="space-y-3">
            {history.length === 0 ? (
              <p className="rounded-xl border border-dashed border-border p-4 text-sm text-muted-foreground">
                Aucun diagnostic pour le moment.
              </p>
            ) : (
              history.slice(0, 5).map((diagnostic) => (
                <div
                  key={diagnostic.diagnosticId}
                  className="flex items-center justify-between gap-3 rounded-xl border border-border p-4"
                >
                  <button
                    type="button"
                    className="min-w-0 flex-1 text-left"
                    onClick={() => navigate(`/client/diagnostic/${diagnostic.diagnosticId}/resultat`)}
                  >
                    <span className="block text-sm font-semibold">Diagnostic #{diagnostic.diagnosticId}</span>
                    <span className="text-xs text-muted-foreground">{diagnostic.statut}</span>
                  </button>
                  <div className="flex shrink-0 items-center gap-2">
                    <span className="text-sm font-bold text-primary">
                      {diagnostic.scoreGlobal ?? "--"}/100
                    </span>
                    <button
                      type="button"
                      className="flex h-9 w-9 items-center justify-center rounded-lg border border-red-200 text-red-600 transition-colors hover:bg-red-50"
                      aria-label="Supprimer le diagnostic"
                      disabled={deletingId === diagnostic.diagnosticId}
                      onClick={() => handleDelete(diagnostic.diagnosticId)}
                    >
                      {deletingId === diagnostic.diagnosticId ? (
                        <Loader2 className="h-4 w-4 animate-spin" />
                      ) : (
                        <Trash2 className="h-4 w-4" />
                      )}
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </aside>
      </section>
    </main>
  );
}
