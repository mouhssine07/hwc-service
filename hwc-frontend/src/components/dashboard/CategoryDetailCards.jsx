export default function CategoryDetailCards({ scores }) {
  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4">
        <h2 className="font-display text-xl font-bold text-foreground">Details categories</h2>
        <p className="text-sm text-muted-foreground">Scores, niveaux et points obtenus</p>
      </div>
      <div className="grid gap-3 md:grid-cols-2">
        {(scores ?? []).map((score) => {
          const value = Math.max(0, Math.min(100, Number(score.score ?? 0)));
          return (
            <article key={score.categorieId} className="rounded-lg border border-border p-4">
              <div className="mb-3 flex items-start justify-between gap-3">
                <h3 className="text-sm font-bold text-foreground">{score.categorieNom}</h3>
                <span className="shrink-0 rounded-full bg-muted px-2.5 py-1 text-xs font-bold text-primary">
                  {score.niveau}
                </span>
              </div>
              <div className="h-2 overflow-hidden rounded-full bg-muted">
                <div className="h-full rounded-full bg-primary" style={{ width: `${value}%` }} />
              </div>
              <p className="mt-3 text-sm font-semibold text-foreground">{value}/100</p>
              <p className="text-xs text-muted-foreground">
                {score.pointsObtenus}/{score.pointsMax} points
              </p>
            </article>
          );
        })}
      </div>
    </section>
  );
}
