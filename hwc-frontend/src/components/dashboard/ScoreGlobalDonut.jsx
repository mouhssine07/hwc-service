import { Cell, Pie, PieChart, ResponsiveContainer } from "recharts";

const COLORS = ["#16a34a", "#e5e7eb"];

export default function ScoreGlobalDonut({ score, niveau }) {
  const value = Math.max(0, Math.min(100, Number(score ?? 0)));
  const data = [
    { name: "Score", value },
    { name: "Reste", value: 100 - value },
  ];

  return (
    <section className="rounded-lg border border-border bg-card p-6 shadow-card">
      <div className="grid gap-5 sm:grid-cols-[190px_1fr] sm:items-center">
        <div className="relative h-44 min-w-0">
          <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
            <PieChart>
              <Pie data={data} dataKey="value" innerRadius={58} outerRadius={78} startAngle={90} endAngle={-270}>
                {data.map((entry, index) => (
                  <Cell key={entry.name} fill={COLORS[index]} />
                ))}
              </Pie>
            </PieChart>
          </ResponsiveContainer>
          <div className="absolute inset-0 flex flex-col items-center justify-center">
            <span className="text-4xl font-bold text-foreground">{value}</span>
            <span className="text-xs font-semibold text-muted-foreground">/100</span>
          </div>
        </div>
        <div>
          <p className="text-sm font-semibold uppercase tracking-wider text-primary">Score global</p>
          <h1 className="mt-2 font-display text-3xl font-bold text-foreground md:text-4xl">
            Dashboard decisionnel
          </h1>
          <p className="mt-3 text-sm leading-6 text-muted-foreground">
            Vue consolidee de votre maturite, des axes critiques et du plan d'action HWC.
          </p>
          <span className="mt-5 inline-flex rounded-full border border-primary/20 bg-primary/10 px-4 py-2 text-sm font-bold text-primary">
            {niveau ?? "NIVEAU INDISPONIBLE"}
          </span>
        </div>
      </div>
    </section>
  );
}
