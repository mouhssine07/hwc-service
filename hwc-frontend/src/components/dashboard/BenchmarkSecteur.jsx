import { BarChart3, Database } from "lucide-react";

function normalizeScore(score) {
  return Math.max(0, Math.min(100, Number(score ?? 0)));
}

function ScoreRow({ label, score, color }) {
  const value = normalizeScore(score);
  return (
    <div className="grid gap-2 sm:grid-cols-[9rem_3.5rem_1fr] sm:items-center">
      <span className="text-sm font-semibold text-foreground">{label}</span>
      <span className="flex h-10 items-center justify-center rounded-lg border border-border bg-background text-lg font-bold text-foreground">
        {Math.round(value)}
      </span>
      <div className="h-3 overflow-hidden rounded-full bg-muted" aria-label={`${label} : ${Math.round(value)} sur 100`}>
        <div className={`h-full rounded-full ${color}`} style={{ width: `${value}%` }} />
      </div>
    </div>
  );
}

export default function BenchmarkSecteur({ benchmark, score }) {
  const clientScore = normalizeScore(benchmark?.scoreClient ?? score);

  if (!benchmark?.disponible) {
    return (
      <section className="rounded-lg border border-border bg-card p-5 shadow-card">
        <div className="flex items-start gap-3">
          <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-secondary/10">
            <Database className="h-5 w-5 text-secondary" />
          </div>
          <div>
            <h2 className="font-display text-xl font-bold text-foreground">Comparaison avec le secteur</h2>
            <p className="mt-1 text-sm text-muted-foreground">
              {benchmark?.secteur ? `Secteur ${benchmark.secteur}` : "Benchmark sectoriel"}
            </p>
          </div>
        </div>
        <div className="mt-5">
          <ScoreRow label={`Vous${benchmark?.prenomClient ? ` (${benchmark.prenomClient})` : ""}`} score={clientScore} color="bg-primary" />
        </div>
        <div className="mt-5 rounded-lg border border-dashed border-border bg-muted/30 p-4">
          <p className="text-sm font-semibold text-foreground">Données sectorielles insuffisantes</p>
          <p className="mt-1 text-sm leading-6 text-muted-foreground">
            {benchmark?.message ?? "Au moins 10 entreprises comparables sont nécessaires pour publier une moyenne fiable."}
          </p>
          {benchmark?.nombreEntreprises > 0 ? (
            <p className="mt-2 text-xs font-semibold text-primary">
              Échantillon actuel : {benchmark.nombreEntreprises} entreprise{benchmark.nombreEntreprises > 1 ? "s" : ""}
            </p>
          ) : null}
        </div>
      </section>
    );
  }

  const moyenne = normalizeScore(benchmark.moyenneSecteur);
  const topDix = normalizeScore(benchmark.topDixPourcent);
  const ecartMoyenne = Math.round(clientScore - moyenne);
  const ecartTop = Math.max(0, Math.round(topDix - clientScore));

  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="flex items-start gap-3">
        <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-secondary/10">
          <BarChart3 className="h-5 w-5 text-secondary" />
        </div>
        <div>
          <h2 className="font-display text-xl font-bold text-foreground">
            Votre score vs secteur {benchmark.secteur}
          </h2>
          <p className="mt-1 text-xs text-muted-foreground">
            Derniers diagnostics de {benchmark.nombreEntreprises} entreprises actives HWC
          </p>
        </div>
      </div>

      <div className="mt-6 space-y-4">
        <ScoreRow label={`Vous${benchmark.prenomClient ? ` (${benchmark.prenomClient})` : ""}`} score={clientScore} color="bg-primary" />
        <ScoreRow label="Moyenne secteur" score={moyenne} color="bg-secondary" />
        <ScoreRow label="Top 10% secteur" score={topDix} color="bg-amber-500" />
      </div>

      <div className="mt-6 rounded-lg bg-muted/50 p-4 text-sm">
        <p className="font-semibold text-foreground">
          Vous êtes à {ecartMoyenne >= 0 ? "+" : ""}{ecartMoyenne} point{Math.abs(ecartMoyenne) > 1 ? "s" : ""} de la moyenne.
        </p>
        <p className="mt-1 text-muted-foreground">
          {ecartTop > 0
            ? `${ecartTop} point${ecartTop > 1 ? "s" : ""} pour atteindre le seuil du top 10%.`
            : "Votre score atteint le seuil du top 10% de votre secteur."}
        </p>
      </div>

      <div className="mt-5 border-t border-border pt-5">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <div>
            <h3 className="font-display text-lg font-bold text-foreground">Entreprises devant vous</h3>
            <p className="text-xs text-muted-foreground">Classement anonymisé pour protéger les données des autres clients</p>
          </div>
          <span className="rounded-full bg-primary/10 px-3 py-1 text-xs font-bold text-primary">
            Votre position : {benchmark.positionClient}/{benchmark.nombreEntreprises}
          </span>
        </div>

        {benchmark.concurrentsDevant?.length ? (
          <div className="mt-4 space-y-3">
            {benchmark.concurrentsDevant.map((concurrent, index) => (
              <div key={`${concurrent.score}-${index}`} className="grid grid-cols-[auto_1fr_auto] items-center gap-3 rounded-lg border border-border p-3">
                <span className="flex h-8 w-8 items-center justify-center rounded-full bg-secondary/10 text-xs font-bold text-secondary">
                  #{index + 1}
                </span>
                <div>
                  <p className="text-sm font-semibold text-foreground">Entreprise anonymisée {String.fromCharCode(65 + index)}</p>
                  <p className="text-xs text-muted-foreground">Même secteur d’activité</p>
                </div>
                <div className="text-right">
                  <p className="text-base font-bold text-foreground">{Math.round(Number(concurrent.score))}/100</p>
                  <p className="text-xs font-semibold text-amber-600">+{Math.round(Number(concurrent.ecartAvecClient))} pts</p>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="mt-4 rounded-lg border border-dashed border-primary/30 bg-primary/5 p-4 text-sm font-semibold text-primary">
            Aucune entreprise de l’échantillon n’obtient un score supérieur au vôtre.
          </p>
        )}
      </div>
    </section>
  );
}
