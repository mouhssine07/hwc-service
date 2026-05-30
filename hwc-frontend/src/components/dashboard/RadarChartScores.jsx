import { PolarAngleAxis, PolarGrid, Radar, RadarChart, ResponsiveContainer, Tooltip } from "recharts";

export default function RadarChartScores({ scores }) {
  const data = (scores ?? []).map((score) => ({
    categorie: score.categorieNom,
    score: Number(score.score ?? 0),
  }));

  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4">
        <h2 className="font-display text-xl font-bold text-foreground">Maturite par axe</h2>
        <p className="text-sm text-muted-foreground">Lecture comparative des categories</p>
      </div>
      <div className="h-72 min-w-0">
        <ResponsiveContainer width="100%" height="100%" minWidth={1} minHeight={1}>
          <RadarChart data={data}>
            <PolarGrid stroke="#e5e7eb" />
            <PolarAngleAxis dataKey="categorie" tick={{ fontSize: 11, fill: "#64748b" }} />
            <Tooltip />
            <Radar dataKey="score" stroke="#16a34a" fill="#16a34a" fillOpacity={0.22} />
          </RadarChart>
        </ResponsiveContainer>
      </div>
    </section>
  );
}
