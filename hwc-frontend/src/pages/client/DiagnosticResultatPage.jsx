import { ArrowLeft, BarChart3, ListChecks, RefreshCcw, Trophy } from "lucide-react";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import { getDiagnostic } from "../../api/diagnosticApi.js";

const levelTone = {
  CRITIQUE: "text-red-600 bg-red-50 border-red-200",
  FAIBLE: "text-orange-600 bg-orange-50 border-orange-200",
  MOYEN: "text-yellow-700 bg-yellow-50 border-yellow-200",
  BON: "text-green-700 bg-green-50 border-green-200",
  EXCELLENT: "text-primary bg-primary/10 border-primary/20",
};

export default function DiagnosticResultatPage() {
  const { diagnosticId } = useParams();
  const navigate = useNavigate();
  const [diagnostic, setDiagnostic] = useState(null);

  useEffect(() => {
    getDiagnostic(diagnosticId)
      .then(setDiagnostic)
      .catch(() => toast.error("Impossible de charger le resultat"));
  }, [diagnosticId]);

  if (!diagnostic) {
    return (
      <main className="min-h-screen bg-muted/30 px-4 py-10">
        <div className="mx-auto max-w-5xl rounded-2xl border border-border bg-card p-8 shadow-card">
          Chargement du resultat...
        </div>
      </main>
    );
  }

  return (
    <main className="min-h-screen bg-muted/30 px-4 py-8">
      <div className="mx-auto max-w-6xl">
        <div className="mb-6 flex flex-wrap justify-between gap-3">
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-lg border border-border bg-card px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
            onClick={() => navigate("/client/diagnostic")}
          >
            <ArrowLeft className="h-4 w-4" />
            Espace diagnostic
          </button>
          <button className="btn btn-outline-hero" type="button" onClick={() => navigate("/client/diagnostic")}>
            <RefreshCcw className="mr-2 h-4 w-4" />
            Nouveau diagnostic
          </button>
        </div>

        <section className="grid gap-6 lg:grid-cols-[0.8fr_1.2fr]">
          <div className="rounded-2xl border border-border bg-card p-8 shadow-elevated">
            <div className="mb-6 flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
              <Trophy className="h-7 w-7 text-primary" />
            </div>
            <p className="mb-3 text-sm font-semibold uppercase tracking-wider text-primary">
              Resultat diagnostic
            </p>
            <div className="font-display text-6xl font-bold text-foreground">
              {diagnostic.scoreGlobal ?? "--"}
              <span className="text-2xl text-muted-foreground">/100</span>
            </div>
            <div
              className={`mt-5 inline-flex rounded-full border px-4 py-2 text-sm font-bold ${
                levelTone[diagnostic.niveauMaturite] ?? levelTone.MOYEN
              }`}
            >
              {diagnostic.niveauMaturite ?? "EN COURS"}
            </div>
            <p className="mt-6 text-sm leading-6 text-muted-foreground">
              Ce score resume votre maturite globale selon les categories ponderees du diagnostic HWC.
            </p>
            <button
              className="btn btn-hero mt-6 w-full"
              type="button"
              onClick={() => navigate(`/client/diagnostic/${diagnosticId}/recommandations`)}
            >
              <ListChecks className="mr-2 h-4 w-4" />
              Voir mes recommandations
            </button>
          </div>

          <div className="rounded-2xl border border-border bg-card p-6 shadow-card">
            <div className="mb-5 flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-secondary/10">
                <BarChart3 className="h-5 w-5 text-secondary" />
              </div>
              <div>
                <h2 className="font-display text-2xl font-bold">Scores par categorie</h2>
                <p className="text-sm text-muted-foreground">Lecture detaillee des axes prioritaires</p>
              </div>
            </div>

            <div className="space-y-4">
              {diagnostic.scores?.map((score) => (
                <div key={score.categorieId} className="rounded-xl border border-border p-4">
                  <div className="mb-3 flex items-center justify-between gap-4">
                    <span className="text-sm font-bold text-foreground">{score.categorieNom}</span>
                    <span className="text-sm font-bold text-primary">{score.score}/100</span>
                  </div>
                  <div className="h-2 overflow-hidden rounded-full bg-muted">
                    <div
                      className="h-full rounded-full bg-primary"
                      style={{ width: `${Math.min(100, Number(score.score ?? 0))}%` }}
                    />
                  </div>
                  <p className="mt-2 text-xs text-muted-foreground">
                    {score.pointsObtenus}/{score.pointsMax} points - {score.niveau}
                  </p>
                </div>
              ))}
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}
