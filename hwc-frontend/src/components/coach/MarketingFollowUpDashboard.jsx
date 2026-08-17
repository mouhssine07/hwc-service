import { Bot, CalendarDays, CheckCircle2, Circle, FileCheck2, ListChecks } from "lucide-react";
import MarketingKpiDashboard from "./MarketingKpiDashboard.jsx";

export default function MarketingFollowUpDashboard({ dashboard, onToggleAction, onUpdateAction, onAddKpi, onOpenCoach, checkInsEnabled, onToggleCheckIns, loading }) {
  const actions = dashboard?.actions ?? [];
  const completed = actions.filter((action) => action.status === "COMPLETED").length;

  return <div className="space-y-6">
    <section className="rounded-2xl border border-primary/20 bg-primary/5 p-5">
      <p className="text-xs font-bold uppercase tracking-wider text-primary">Priorité proposée</p>
      <p className="mt-2 text-base font-bold leading-7 text-[#173f46]">{dashboard?.kpis?.nextPriority ?? dashboard?.immediatePriority}</p>
    </section>

    <section className="rounded-2xl border border-[#dbe8e8] p-5">
      <div className="flex items-center justify-between gap-3">
        <div><h3 className="flex items-center gap-2 font-extrabold"><ListChecks className="h-5 w-5 text-primary" /> Plan d’action</h3><p className="mt-1 text-xs text-slate-500">{completed} action{completed > 1 ? "s" : ""} terminée{completed > 1 ? "s" : ""} sur {actions.length}</p></div>
        <span className="rounded-full bg-primary/10 px-3 py-1 text-xs font-bold text-primary">{actions.length ? Math.round((completed / actions.length) * 100) : 0}%</span>
      </div>
      <div className="mt-4 space-y-3">{actions.map((action) => <article key={action.id} className="rounded-xl border border-[#e2ecec] p-4">
        <div className="flex items-start gap-3">
          <button type="button" disabled={loading} onClick={() => onToggleAction(action)} aria-label={action.status === "COMPLETED" ? "Rouvrir l’action" : "Terminer l’action"} className="mt-0.5 text-primary">{action.status === "COMPLETED" ? <CheckCircle2 className="h-5 w-5" /> : <Circle className="h-5 w-5" />}</button>
          <div className="min-w-0 flex-1"><p className={`text-sm font-bold ${action.status === "COMPLETED" ? "text-slate-400 line-through" : "text-[#173f46]"}`}>{action.title}</p><p className="mt-1 text-xs text-slate-400">Semaine {action.weekNumber}</p></div>
        </div>
        <div className="mt-3 grid gap-2 pl-8 sm:grid-cols-2">
          <label className="flex items-center gap-2 rounded-lg bg-slate-50 px-3 py-2 text-xs text-slate-500"><CalendarDays className="h-4 w-4" /><input type="date" value={action.dueDate ?? ""} onChange={(event) => onUpdateAction(action, { dueDate: event.target.value })} className="min-w-0 bg-transparent outline-none" /></label>
          <label className="flex items-center gap-2 rounded-lg bg-slate-50 px-3 py-2 text-xs text-slate-500"><FileCheck2 className="h-4 w-4" /><input defaultValue={action.proofReference ?? ""} onBlur={(event) => onUpdateAction(action, { proofReference: event.target.value })} placeholder="Lien ou référence de preuve" className="min-w-0 flex-1 bg-transparent outline-none" /></label>
        </div>
      </article>)}</div>
    </section>

    <section className="rounded-2xl border border-[#dbe8e8] p-5"><h3 className="mb-4 font-extrabold">Suivi des KPI</h3><MarketingKpiDashboard dashboard={dashboard?.kpis} onAdd={onAddKpi} checkInsEnabled={checkInsEnabled} onToggleCheckIns={onToggleCheckIns} loading={loading} /></section>

    <section className="flex items-center justify-between gap-4 rounded-2xl border border-dashed border-primary/30 p-5"><div><h3 className="flex items-center gap-2 font-extrabold"><Bot className="h-5 w-5 text-primary" /> Accès rapide au Coach</h3><p className="mt-1 text-xs text-slate-500">Posez une question ponctuelle sans quitter votre tableau de suivi.</p></div><button type="button" onClick={onOpenCoach} className="shrink-0 rounded-xl bg-primary px-4 py-2 text-sm font-bold text-white">Ouvrir</button></section>
  </div>;
}
