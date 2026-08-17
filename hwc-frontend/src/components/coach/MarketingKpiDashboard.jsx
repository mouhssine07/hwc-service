import { Bell, BellOff, Loader2, Plus, TrendingDown, TrendingUp } from "lucide-react";
import { useState } from "react";

function Sparkline({ points }) {
  if (!points?.length) return null;
  const values = points.map((point) => Number(point.value));
  const min = Math.min(...values); const max = Math.max(...values); const range = max - min || 1;
  const coords = values.map((value, index) => `${(index / Math.max(1, values.length - 1)) * 100},${36 - ((value - min) / range) * 30}`).join(" ");
  return <svg viewBox="0 0 100 40" className="h-20 w-full overflow-visible" role="img" aria-label="Tendance KPI"><polyline points={coords} fill="none" stroke="currentColor" strokeWidth="2.5" vectorEffect="non-scaling-stroke" className="text-primary" />{values.map((value, index) => <circle key={`${value}-${index}`} cx={(index / Math.max(1, values.length - 1)) * 100} cy={36 - ((value - min) / range) * 30} r="2" className="fill-primary" />)}</svg>;
}

export default function MarketingKpiDashboard({ dashboard, onAdd, checkInsEnabled, onToggleCheckIns, loading }) {
  const [name, setName] = useState(""); const [value, setValue] = useState(""); const [unit, setUnit] = useState("");
  const submit = async (event) => { event.preventDefault(); await onAdd({ name, value: Number(value), unit: unit || null }); setValue(""); };
  return <div className="space-y-4">
    <div className="rounded-2xl border border-primary/20 bg-primary/5 p-4"><p className="text-xs font-bold uppercase tracking-wide text-primary">Prochaine priorité</p><p className="mt-2 text-sm font-semibold leading-6 text-slate-700">{dashboard?.nextPriority ?? "Priorité proposée : enregistrer votre première mesure KPI."}</p></div>
    <div className="flex items-center justify-between rounded-xl border p-3 text-xs"><span className="flex items-center gap-2">{checkInsEnabled ? <Bell className="h-4 w-4 text-primary" /> : <BellOff className="h-4 w-4 text-slate-400" />} Relance après 7 jours sans activité</span><button type="button" onClick={onToggleCheckIns} className="font-bold text-primary">{checkInsEnabled ? "Désactiver" : "Activer"}</button></div>
    <form onSubmit={submit} className="grid gap-2 rounded-xl border p-3 sm:grid-cols-[1fr_110px_90px_auto]"><input required value={name} onChange={(event) => setName(event.target.value)} placeholder="KPI" className="rounded-lg border px-3 py-2 text-xs" /><input required type="number" step="any" value={value} onChange={(event) => setValue(event.target.value)} placeholder="Valeur" className="rounded-lg border px-3 py-2 text-xs" /><input value={unit} onChange={(event) => setUnit(event.target.value)} placeholder="Unité" className="rounded-lg border px-3 py-2 text-xs" /><button disabled={loading} className="flex items-center justify-center rounded-lg bg-primary px-3 py-2 text-white">{loading ? <Loader2 className="h-4 w-4 animate-spin" /> : <Plus className="h-4 w-4" />}</button></form>
    <p className="rounded-xl bg-slate-50 p-3 text-xs leading-5 text-slate-600">{dashboard?.comment}</p>
    <div className="grid gap-3 md:grid-cols-2">{dashboard?.series?.map((series) => <article key={series.name} className="rounded-xl border p-4"><div className="flex items-center justify-between"><div><h4 className="text-sm font-bold">{series.name}</h4><p className="text-xs text-slate-400">Dernière valeur : {series.points.at(-1)?.value} {series.unit ?? ""}</p></div>{series.trend === "DOWN" ? <TrendingDown className="text-red-500" /> : series.trend === "UP" ? <TrendingUp className="text-emerald-500" /> : null}</div><Sparkline points={series.points} /></article>)}</div>
  </div>;
}
