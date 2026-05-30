import { BriefcaseBusiness } from "lucide-react";

export default function ServicesRecommandes({ services }) {
  return (
    <section className="rounded-lg border border-border bg-card p-5 shadow-card">
      <div className="mb-4 flex items-start gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-secondary/10">
          <BriefcaseBusiness className="h-5 w-5 text-secondary" />
        </div>
        <div>
          <h2 className="font-display text-xl font-bold text-foreground">Services HWC recommandes</h2>
          <p className="text-sm text-muted-foreground">Offres associees aux priorites du diagnostic</p>
        </div>
      </div>
      {services?.length ? (
        <div className="space-y-3">
          {services.map((service) => (
            <article key={`${service.serviceHwcNom}-${service.sousServiceHwcNom}`} className="rounded-lg border border-border p-4">
              <h3 className="text-sm font-bold text-foreground">{service.serviceHwcNom}</h3>
              {service.sousServiceHwcNom ? (
                <p className="mt-1 text-sm text-muted-foreground">{service.sousServiceHwcNom}</p>
              ) : null}
              {service.impactEstime ? <p className="mt-3 text-sm font-semibold text-primary">{service.impactEstime}</p> : null}
            </article>
          ))}
        </div>
      ) : (
        <p className="rounded-lg border border-dashed border-border p-4 text-sm text-muted-foreground">
          Aucun service associe aux recommandations actuelles.
        </p>
      )}
    </section>
  );
}
