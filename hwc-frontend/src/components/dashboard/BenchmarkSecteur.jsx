export default function BenchmarkSecteur({ score }) {
  const value = Math.max(0, Math.min(100, Number(score ?? 0)));
  const benchmark = 70;

  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4">
        <h2 className="font-display text-xl font-bold text-foreground">Benchmark cible</h2>
        <p className="text-sm text-muted-foreground">Reference interne de maturite operationnelle</p>
      </div>
      <div className="space-y-4">
        <div>
          <div className="mb-2 flex items-center justify-between text-sm">
            <span className="font-semibold text-foreground">Votre score</span>
            <span className="font-bold text-primary">{value}/100</span>
          </div>
          <div className="h-2 overflow-hidden rounded-full bg-muted">
            <div className="h-full rounded-full bg-primary" style={{ width: `${value}%` }} />
          </div>
        </div>
        <div>
          <div className="mb-2 flex items-center justify-between text-sm">
            <span className="font-semibold text-foreground">Cible HWC</span>
            <span className="font-bold text-secondary">{benchmark}/100</span>
          </div>
          <div className="h-2 overflow-hidden rounded-full bg-muted">
            <div className="h-full rounded-full bg-secondary" style={{ width: `${benchmark}%` }} />
          </div>
        </div>
      </div>
    </section>
  );
}
