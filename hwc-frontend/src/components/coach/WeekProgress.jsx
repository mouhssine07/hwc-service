import { CalendarDays, CheckCircle2 } from "lucide-react";

export default function WeekProgress({ week }) {
  const weeklyPercentage = week.objectifsTotal ? Math.round((week.objectifsCompletes / week.objectifsTotal) * 100) : 0;
  const programmePercentage = week.progressionProgramme ?? 0;
  const formatDate = (value) => (value ? new Date(`${value}T12:00:00`).toLocaleDateString("fr-FR", { day: "numeric", month: "long" }) : "--");

  return (
    <section className="rounded-2xl bg-primary p-6 text-primary-foreground shadow-elevated md:p-8">
      <div className="flex flex-wrap items-start justify-between gap-5">
        <div>
          <p className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-primary-foreground/75">
            <CalendarDays className="h-4 w-4" /> Semaine {week.numeroSemaine ?? 1}/{week.dureeProgrammeSemaines ?? 12} · du {formatDate(week.semaineDebut)} au {formatDate(week.semaineFin)}
          </p>
          <h1 className="mt-3 font-display text-3xl font-bold md:text-4xl">Votre plan d&apos;action de la semaine</h1>
          <p className="mt-3 max-w-2xl text-sm leading-6 text-primary-foreground/85">{week.conseilSemaine}</p>
        </div>
        <div className="rounded-xl bg-white/10 p-4 text-center">
          <p className="text-3xl font-bold">{programmePercentage}%</p>
          <p className="mt-1 text-xs font-semibold text-primary-foreground/75">programme global</p>
        </div>
      </div>
      <div className="mt-6 h-3 overflow-hidden rounded-full bg-white/20">
        <div className="h-full rounded-full bg-secondary transition-all" style={{ width: `${programmePercentage}%` }} />
      </div>
      <p className="mt-3 flex items-center gap-2 text-sm font-semibold text-primary-foreground/90">
        <CheckCircle2 className="h-4 w-4" /> Cette semaine : {week.objectifsCompletes}/{week.objectifsTotal} objectif{week.objectifsTotal > 1 ? "s" : ""} terminé{week.objectifsCompletes > 1 ? "s" : ""} ({weeklyPercentage}%)
      </p>
    </section>
  );
}
