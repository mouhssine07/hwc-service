import { CalendarDays, CheckCircle2 } from "lucide-react";

export default function CoachHistory({ history, currentWeekId }) {
  const previousWeeks = history.filter((week) => week.id !== currentWeekId);
  if (!previousWeeks.length) {
    return null;
  }
  return (
    <section className="rounded-xl border border-border bg-card p-5 shadow-card">
      <div className="mb-4 flex items-center gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-secondary/10 text-secondary"><CalendarDays className="h-5 w-5" /></div>
        <div><h2 className="font-display text-xl font-bold">Historique du Coach</h2><p className="text-sm text-muted-foreground">Vos semaines precedentes</p></div>
      </div>
      <div className="space-y-3">
        {previousWeeks.slice(0, 6).map((week) => (
          <article key={week.id} className="flex items-center justify-between gap-3 rounded-lg border border-border p-4">
            <div><p className="font-semibold text-foreground">Semaine du {new Date(`${week.semaineDebut}T12:00:00`).toLocaleDateString("fr-FR")}</p><p className="mt-1 text-xs text-muted-foreground">{week.objectifsCompletes}/{week.objectifsTotal} objectifs realises</p></div>
            <span className="inline-flex items-center gap-1 rounded-full bg-primary/10 px-2.5 py-1 text-xs font-bold text-primary"><CheckCircle2 className="h-3.5 w-3.5" /> {week.objectifsTotal ? Math.round((week.objectifsCompletes / week.objectifsTotal) * 100) : 0}%</span>
          </article>
        ))}
      </div>
    </section>
  );
}
