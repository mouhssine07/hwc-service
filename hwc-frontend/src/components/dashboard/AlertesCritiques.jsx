import { AlertTriangle } from "lucide-react";

export default function AlertesCritiques({ alertes }) {
  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4 flex items-start gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-red-50">
          <AlertTriangle className="h-5 w-5 text-red-600" />
        </div>
        <div>
          <h2 className="font-display text-xl font-bold text-foreground">Alertes critiques</h2>
          <p className="text-sm text-muted-foreground">Categories avec score inferieur ou egal a 40</p>
        </div>
      </div>
      {alertes?.length ? (
        <div className="space-y-3">
          {alertes.map((alerte) => (
            <article key={alerte.categorieId} className="rounded-lg border border-red-200 bg-red-50 p-4">
              <div className="flex items-center justify-between gap-3">
                <h3 className="text-sm font-bold text-red-800">{alerte.categorieNom}</h3>
                <span className="text-sm font-bold text-red-700">{alerte.score}/100</span>
              </div>
              <p className="mt-2 text-sm leading-6 text-red-700">{alerte.message}</p>
            </article>
          ))}
        </div>
      ) : (
        <p className="rounded-lg border border-dashed border-border p-4 text-sm text-muted-foreground">
          Aucun axe critique detecte sur le dernier diagnostic.
        </p>
      )}
    </section>
  );
}
