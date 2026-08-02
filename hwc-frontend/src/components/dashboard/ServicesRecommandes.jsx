import { Bot, CalendarDays, Laptop, Loader2, Search, X } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { createPublicDemandeContact } from "../../api/publicContentApi.js";
import useClientAuthStore from "../../store/clientAuthStore.js";

const SERVICES_HWC = [
  {
    titre: "SEO & SEA",
    description: "Augmentez votre visibilité organique",
    presentation:
      "Améliorez votre positionnement sur Google grâce à un audit SEO, une stratégie de mots-clés et des campagnes publicitaires pilotées par des indicateurs clairs.",
    accompagnement: "Un expert HWC analyse votre présence digitale et prépare avec vous un plan d’acquisition mesurable.",
    scoreLie: "Marketing",
    type: "humain",
    icon: Search,
  },
  {
    titre: "Coaching Dirigeants",
    description: "Développez votre leadership",
    presentation:
      "Transformez les résultats de votre diagnostic en objectifs hebdomadaires concrets pour renforcer votre posture, vos priorités et votre communication managériale.",
    accompagnement: "Votre Coach IA génère et suit trois objectifs personnalisés chaque semaine.",
    scoreLie: "Leadership",
    type: "ia",
    icon: Bot,
  },
  {
    titre: "Refonte Site Web",
    description: "Modernisez votre présence digitale",
    presentation:
      "Faites évoluer votre site avec une expérience plus claire, rapide et adaptée au mobile afin de mieux présenter votre offre et convertir vos visiteurs.",
    accompagnement: "Un agent HWC étudie vos besoins, votre site actuel et les améliorations prioritaires.",
    scoreLie: "Maturité",
    type: "humain",
    icon: Laptop,
  },
];

export default function ServicesRecommandes() {
  const navigate = useNavigate();
  const [selectedService, setSelectedService] = useState(null);

  return (
    <section className="rounded-xl border border-border bg-card p-5 shadow-card md:p-6">
      <div className="mb-6">
        <h2 className="font-display text-2xl font-bold text-foreground">Services HWC recommandés</h2>
        <p className="mt-1 text-sm text-muted-foreground">
          Des solutions adaptées aux principaux axes de votre diagnostic
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        {SERVICES_HWC.map((service) => {
          const Icon = service.icon;
          const aiCoaching = service.type === "ia";
          return (
            <article
              key={service.titre}
              className="flex min-h-[26rem] flex-col rounded-xl border border-border bg-background p-5 transition-all hover:-translate-y-1 hover:border-primary/40 hover:shadow-card"
            >
              <div className="flex h-11 w-11 items-center justify-center rounded-lg bg-primary/10">
                <Icon className="h-5 w-5 text-primary" />
              </div>
              <h3 className="mt-4 font-display text-xl font-bold text-foreground">{service.titre}</h3>
              <p className="mt-2 text-sm font-semibold text-primary">{service.description}</p>
              <p className="mt-3 text-sm leading-6 text-muted-foreground">{service.presentation}</p>
              <div className="mt-4 rounded-lg bg-muted/60 p-3">
                <p className="text-xs font-bold uppercase tracking-wide text-foreground">
                  {aiCoaching ? "Suivi personnalisé" : "Accompagnement par un agent"}
                </p>
                <p className="mt-1 text-xs leading-5 text-muted-foreground">{service.accompagnement}</p>
              </div>
              <div className="mt-5">
                <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">Score lié</p>
                <p className="mt-1 text-sm font-bold text-primary">{service.scoreLie}</p>
              </div>
              <button
                type="button"
                className={aiCoaching ? "btn btn-hero mt-auto w-full" : "btn btn-outline-hero mt-auto w-full"}
                onClick={() => aiCoaching ? navigate("/client/coach") : setSelectedService(service)}
              >
                {aiCoaching ? "Démarrer mon coaching IA" : "Prendre rendez-vous avec un agent"}
              </button>
            </article>
          );
        })}
      </div>

      {selectedService ? <AppointmentDialog service={selectedService} onClose={() => setSelectedService(null)} /> : null}
    </section>
  );
}

function AppointmentDialog({ service, onClose }) {
  const clientUser = useClientAuthStore((state) => state.clientUser);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    nom: [clientUser?.prenom, clientUser?.nom].filter(Boolean).join(" "),
    email: clientUser?.email ?? "",
    telephone: "",
    message: "",
  });

  const update = (event) => setForm((current) => ({ ...current, [event.target.name]: event.target.value }));

  const submit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      await createPublicDemandeContact({ ...form, serviceDemande: service.titre });
      toast.success("Votre demande de rendez-vous a bien été transmise à un agent HWC.");
      onClose();
    } catch {
      toast.error("Impossible d'envoyer la demande de rendez-vous.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/55 p-4" role="dialog" aria-modal="true" aria-labelledby="appointment-title">
      <div className="max-h-[90vh] w-full max-w-lg overflow-y-auto rounded-2xl border border-border bg-card p-6 shadow-elevated">
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-sm font-bold uppercase tracking-wider text-primary">Accompagnement humain</p>
            <h2 id="appointment-title" className="mt-1 font-display text-2xl font-bold">Prendre rendez-vous</h2>
            <p className="mt-2 text-sm text-muted-foreground">
              Un agent HWC vous contactera pour le service « {service.titre} ».
            </p>
          </div>
          <button type="button" className="rounded-lg p-2 text-muted-foreground hover:bg-muted" onClick={onClose} aria-label="Fermer">
            <X className="h-5 w-5" />
          </button>
        </div>
        <form className="mt-6 space-y-4" onSubmit={submit}>
          <Field label="Nom complet" name="nom" value={form.nom} onChange={update} required />
          <Field label="Email" name="email" type="email" value={form.email} onChange={update} required />
          <Field label="Téléphone" name="telephone" type="tel" value={form.telephone} onChange={update} required />
          <div>
            <label htmlFor="appointment-message" className="mb-2 block text-sm font-semibold">
              Votre besoin ou vos disponibilités
            </label>
            <textarea id="appointment-message" name="message" rows="4" maxLength="1500" value={form.message} onChange={update} className="w-full rounded-lg border border-border bg-background px-3 py-2 text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20" />
          </div>
          <button className="btn btn-hero w-full" type="submit" disabled={submitting}>
            {submitting ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <CalendarDays className="mr-2 h-4 w-4" />}
            {submitting ? "Envoi..." : "Envoyer ma demande"}
          </button>
        </form>
      </div>
    </div>
  );
}

function Field({ label, name, type = "text", value, onChange, required }) {
  return (
    <div>
      <label htmlFor={`appointment-${name}`} className="mb-2 block text-sm font-semibold">{label}</label>
      <input id={`appointment-${name}`} name={name} type={type} value={value} onChange={onChange} required={required} maxLength="150" className="h-11 w-full rounded-lg border border-border bg-background px-3 text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20" />
    </div>
  );
}
