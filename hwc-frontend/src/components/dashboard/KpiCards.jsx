import { Activity, CalendarDays, ListChecks, TrendingUp } from "lucide-react";

const formatProgression = (value) => {
  if (value === null || value === undefined) {
    return "--";
  }
  const number = Number(value);
  return `${number > 0 ? "+" : ""}${number.toFixed(2)} pts`;
};

export default function KpiCards({ dashboard }) {
  const items = [
    {
      label: "Diagnostics finalises",
      value: dashboard.nombreDiagnostics ?? 0,
      icon: Activity,
    },
    {
      label: "Dernier diagnostic",
      value: dashboard.dateDernierDiagnostic
        ? new Date(dashboard.dateDernierDiagnostic).toLocaleDateString("fr-FR")
        : "--",
      icon: CalendarDays,
    },
    {
      label: "Progression",
      value: formatProgression(dashboard.progressionDepuisDernier),
      icon: TrendingUp,
    },
    {
      label: "Actions prioritaires",
      value: dashboard.planActionResume?.length ?? 0,
      icon: ListChecks,
    },
  ];

  return (
    <section className="grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
      {items.map((item) => {
        const Icon = item.icon;
        return (
          <article key={item.label} className="rounded-lg border border-border bg-card p-4 shadow-card">
            <div className="mb-4 flex h-9 w-9 items-center justify-center rounded-lg bg-secondary/10">
              <Icon className="h-4 w-4 text-secondary" />
            </div>
            <p className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">{item.label}</p>
            <p className="mt-2 text-2xl font-bold text-foreground">{item.value}</p>
          </article>
        );
      })}
    </section>
  );
}
