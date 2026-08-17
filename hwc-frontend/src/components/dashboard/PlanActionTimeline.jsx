const horizonLabels = {
  COURT_TERME: "Court terme",
  MOYEN_TERME: "Moyen terme",
  LONG_TERME: "Long terme",
};

export default function PlanActionTimeline({ actions }) {
  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-5">
        <h2 className="font-display text-xl font-bold text-foreground">Plan d'action resume</h2>
        <p className="text-sm text-muted-foreground">Actions priorisees selon vos scores</p>
      </div>
      {actions?.length ? (
        <div className="space-y-4">
          {actions.slice(0, 6).map((action) => (
            <article key={action.id} className="grid gap-3 rounded-lg border border-border p-4 sm:grid-cols-[120px_1fr]">
              <div>
                <span className="inline-flex rounded-full bg-primary/10 px-3 py-1 text-xs font-bold text-primary">
                  P{action.priorite}
                </span>
                <p className="mt-2 text-xs font-semibold text-muted-foreground">
                  {horizonLabels[action.horizon] ?? action.horizon}
                </p>
              </div>
              <div>
                <h3 className="text-sm font-bold text-foreground">{action.titre}</h3>
                <p className="mt-1 text-sm leading-6 text-muted-foreground">{action.description}</p>
              </div>
            </article>
          ))}
        </div>
      ) : (
        <p className="rounded-lg border border-dashed border-border p-4 text-sm text-muted-foreground">
          Aucune recommandation disponible pour le dernier diagnostic.
        </p>
      )}
    </section>
  );
}
