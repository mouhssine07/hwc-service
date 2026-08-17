import { ArrowRight, Bot, Clock3 } from "lucide-react";

export default function MarketingCoachPriority({ priority, onOpen }) {
  if (!priority) return null;
  return <section className="overflow-hidden rounded-lg border border-primary/20 bg-gradient-to-r from-primary/10 via-card to-card shadow-card">
    <div className="flex flex-col gap-4 p-5 sm:flex-row sm:items-center sm:justify-between">
      <div className="flex min-w-0 gap-4"><span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-primary text-white"><Bot className="h-5 w-5" /></span><div><p className="text-xs font-bold uppercase tracking-wider text-primary">Priorité proposée par votre Coach</p><h2 className="mt-2 text-base font-bold leading-6 text-foreground">{priority.priority}</h2>{priority.inactive ? <p className="mt-2 flex items-center gap-1.5 text-xs font-semibold text-amber-700"><Clock3 className="h-3.5 w-3.5" /> Aucun progrès enregistré depuis au moins 7 jours</p> : null}</div></div>
      <button type="button" onClick={onOpen} className="inline-flex shrink-0 items-center justify-center gap-2 rounded-lg bg-primary px-4 py-2.5 text-sm font-bold text-white">Ouvrir le suivi <ArrowRight className="h-4 w-4" /></button>
    </div>
  </section>;
}
