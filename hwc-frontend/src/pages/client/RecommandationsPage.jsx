import { ArrowLeft, CalendarDays, CheckCircle2, Target } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import { getDiagnosticPlanAction } from "../../api/diagnosticApi.js";

const horizonLabels = {
  courtTerme: "Court terme",
  moyenTerme: "Moyen terme",
  longTerme: "Long terme",
};

const horizonDescriptions = {
  courtTerme: "Actions prioritaires a lancer rapidement.",
  moyenTerme: "Chantiers de structuration sur les prochains mois.",
  longTerme: "Transformations de fond a planifier.",
};

export default function RecommandationsPage() {
  const { diagnosticId } = useParams();
  const navigate = useNavigate();
  const [plan, setPlan] = useState(null);

  useEffect(() => {
    getDiagnosticPlanAction(diagnosticId)
      .then(setPlan)
      .catch(() => toast.error("Impossible de charger les recommandations"));
  }, [diagnosticId]);

  const groups = useMemo(() => {
    if (!plan) {
      return [];
    }
    return ["courtTerme", "moyenTerme", "longTerme"].map((key) => ({
      key,
      items: plan[key] ?? [],
    }));
  }, [plan]);

  const total = groups.reduce((sum, group) => sum + group.items.length, 0);

  return (
    <main className="min-h-screen bg-muted/30 px-4 py-8">
      <div className="mx-auto max-w-6xl space-y-6">
        <div className="flex flex-wrap justify-between gap-3">
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-lg border border-border bg-card px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
            onClick={() => navigate(`/client/diagnostic/${diagnosticId}/resultat`)}
          >
            <ArrowLeft className="h-4 w-4" />
            Retour aux resultats
          </button>
        </div>

        <section className="rounded-2xl border border-border bg-card p-6 shadow-elevated md:p-8">
          <div className="mb-6 flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
            <Target className="h-7 w-7 text-primary" />
          </div>
          <p className="mb-3 text-sm font-semibold uppercase tracking-wider text-primary">
            Plan d'action HWC
          </p>
          <h1 className="font-display text-4xl font-bold text-foreground">
            Vos recommandations prioritaires
          </h1>
          <p className="mt-4 max-w-3xl text-sm leading-6 text-muted-foreground md:text-base">
            Les recommandations sont generees automatiquement a partir des scores faibles ou perfectibles
            de votre diagnostic.
          </p>
          <div className="mt-6 grid gap-3 sm:grid-cols-2">
            <div className="rounded-xl border border-border bg-muted/40 p-4">
              <p className="text-sm text-muted-foreground">Recommandations</p>
              <p className="mt-1 text-3xl font-bold text-foreground">{total}</p>
            </div>
            <div className="rounded-xl border border-border bg-muted/40 p-4">
              <p className="text-sm text-muted-foreground">Impact</p>
              <p className="mt-1 text-lg font-bold text-primary">{plan?.impactTotal ?? "-"}</p>
            </div>
          </div>
        </section>

        {groups.map((group) => (
          <section key={group.key} className="rounded-2xl border border-border bg-card p-6 shadow-card">
            <div className="mb-5 flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-secondary/10">
                <CalendarDays className="h-5 w-5 text-secondary" />
              </div>
              <div>
                <h2 className="font-display text-2xl font-bold">{horizonLabels[group.key]}</h2>
                <p className="text-sm text-muted-foreground">{horizonDescriptions[group.key]}</p>
              </div>
            </div>

            {group.items.length === 0 ? (
              <p className="rounded-xl border border-dashed border-border p-4 text-sm text-muted-foreground">
                Aucune recommandation sur cet horizon.
              </p>
            ) : (
              <div className="grid gap-4 lg:grid-cols-2">
                {group.items.map((recommandation) => (
                  <article key={recommandation.id} className="rounded-xl border border-border p-5">
                    <div className="mb-3 flex items-start justify-between gap-3">
                      <h3 className="text-lg font-bold text-foreground">{recommandation.titre}</h3>
                      <span className="rounded-full bg-primary/10 px-2.5 py-1 text-xs font-bold text-primary">
                        P{recommandation.priorite}
                      </span>
                    </div>
                    <p className="text-sm leading-6 text-muted-foreground">{recommandation.description}</p>
                    {recommandation.impactEstime ? (
                      <p className="mt-4 rounded-lg bg-muted/60 px-3 py-2 text-sm font-semibold text-primary">
                        {recommandation.impactEstime}
                      </p>
                    ) : null}
                    {recommandation.kpis?.length ? (
                      <div className="mt-4 space-y-2">
                        {recommandation.kpis.map((kpi) => (
                          <p key={kpi} className="flex items-center gap-2 text-sm text-foreground/80">
                            <CheckCircle2 className="h-4 w-4 shrink-0 text-primary" />
                            {kpi}
                          </p>
                        ))}
                      </div>
                    ) : null}
                  </article>
                ))}
              </div>
            )}
          </section>
        ))}
      </div>
    </main>
  );
}
