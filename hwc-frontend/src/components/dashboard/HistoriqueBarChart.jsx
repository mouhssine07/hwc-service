import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

export default function HistoriqueBarChart({ historique }) {
  const data = [...(historique ?? [])]
    .reverse()
    .map((item) => ({
      label: item.dateFin ? new Date(item.dateFin).toLocaleDateString("fr-FR", { day: "2-digit", month: "2-digit" }) : `#${item.diagnosticId}`,
      score: Number(item.scoreGlobal ?? 0),
    }));

  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4">
        <h2 className="font-display text-xl font-bold text-foreground">Historique des scores</h2>
        <p className="text-sm text-muted-foreground">Evolution des diagnostics finalises</p>
      </div>
      <div className="h-72 min-w-0">
        <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1} initialDimension={{ width: 560, height: 288 }}>
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" vertical={false} />
            <XAxis dataKey="label" tick={{ fontSize: 12 }} />
            <YAxis domain={[0, 100]} tick={{ fontSize: 12 }} />
            <Tooltip />
            <Bar dataKey="score" fill="#0f766e" radius={[6, 6, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </section>
  );
}
