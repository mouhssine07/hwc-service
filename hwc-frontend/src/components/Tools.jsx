import {
  ArrowRight,
  Calculator,
  Download,
  FileCheck,
  FileText,
  MapPin,
  Presentation,
  Search,
  Table,
  UserRound,
} from "lucide-react";
import SectionHeading from "./SectionHeading.jsx";

const resources = [
  {
    id: 1,
    icon: FileText,
    title: "Template de Brief Marketing",
    description: "Un modèle complet pour cadrer vos projets marketing et briefer vos prestataires.",
    category: "Template",
    format: "PDF",
    downloadUrl: "#",
  },
  {
    id: 2,
    icon: Calculator,
    title: "Calculateur de ROI Marketing",
    description: "Évaluez le retour sur investissement de vos campagnes marketing digitales.",
    category: "Outil",
    format: "Excel",
    downloadUrl: "#",
  },
  {
    id: 3,
    icon: Search,
    title: "Guide SEO 2025",
    description: "Les meilleures pratiques SEO pour améliorer votre visibilité sur Google.",
    category: "Guide",
    format: "PDF",
    downloadUrl: "#",
  },
  {
    id: 4,
    icon: Presentation,
    title: "Présentation Méthode Krav Maga",
    description: "Découvrez notre approche unique du marketing inspirée du Krav Maga.",
    category: "Présentation",
    format: "PDF",
    downloadUrl: "#",
  },
  {
    id: 5,
    icon: FileCheck,
    title: "Checklist Audit Digital",
    description: "Une liste exhaustive pour auditer votre présence digitale en autonomie.",
    category: "Checklist",
    format: "PDF",
    downloadUrl: "#",
  },
  {
    id: 6,
    icon: Table,
    title: "Planificateur de Contenu",
    description: "Un calendrier éditorial pour organiser vos publications sur 12 mois.",
    category: "Template",
    format: "Excel",
    downloadUrl: "#",
  },
  {
    id: 7,
    icon: Search,
    title: "Guide LinkedIn B2B",
    description: "Maîtrisez LinkedIn pour générer des leads qualifiés en B2B.",
    category: "Guide",
    format: "PDF",
    downloadUrl: "#",
  },
  {
    id: 8,
    icon: UserRound,
    title: "Kit de Persona Marketing",
    description: "Créez des personas détaillés pour mieux cibler votre audience.",
    category: "Template",
    format: "PDF",
    downloadUrl: "#",
  },
];

const categoryStyles = {
  Template: "bg-primary/20 text-primary",
  Outil: "bg-secondary/20 text-secondary",
  Guide: "bg-accent/40 text-accent-foreground",
  Présentation: "bg-primary/20 text-primary",
  Checklist: "bg-secondary/20 text-secondary",
};

const locations = [
  ["Maroc", "Casablanca", "MA"],
  ["France", "Paris", "FR"],
  ["Luxembourg", "Luxembourg", "LU"],
  ["Îles Maurice", "Port-Louis", "MU"],
];

export default function Tools({ page = false }) {
  if (page) return <ToolsPage />;

  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Une présence internationale" subtitle="Nous accompagnons nos clients à travers le monde" />
        <div className="relative">
          <div className="relative mx-auto aspect-[2/1] max-w-4xl">
            <div className="absolute inset-0 overflow-hidden rounded-3xl border border-border bg-card">
              <div className="absolute inset-0 opacity-20">
                {Array.from({ length: 10 }).map((_, index) => (
                  <div className="absolute h-px w-full bg-border" style={{ top: `${(index + 1) * 10}%` }} key={`h-${index}`} />
                ))}
                {Array.from({ length: 20 }).map((_, index) => (
                  <div className="absolute h-full w-px bg-border" style={{ left: `${(index + 1) * 5}%` }} key={`v-${index}`} />
                ))}
              </div>
              <div className="absolute left-[35%] top-[20%] h-[40%] w-[30%] rounded-[40%] bg-secondary/10 blur-xl" />
              <div className="absolute left-[15%] top-[30%] h-[25%] w-[15%] rounded-[30%] bg-accent/10 blur-lg" />
            </div>
            {[
              ["Paris, France", "top-[35%] left-[40%]", "bg-secondary"],
              ["Luxembourg", "top-[45%] left-[38%]", "bg-accent"],
              ["Casablanca, Maroc", "top-[55%] left-[35%]", "bg-primary"],
              ["Port-Louis, Îles Maurice", "top-[66%] left-[58%]", "bg-secondary"],
            ].map(([label, position, color], index) => (
              <div className={`group absolute ${position}`} key={label}>
                <div className="relative">
                  <div className={`absolute h-4 w-4 animate-ping rounded-full ${color}`} style={{ animationDelay: `${index * 0.5}s` }} />
                  <div className={`relative flex h-4 w-4 items-center justify-center rounded-full ${color}`}>
                    {index === 0 ? <MapPin className="h-3 w-3 text-white" /> : null}
                  </div>
                  <div className="glass absolute -top-12 left-1/2 -translate-x-1/2 whitespace-nowrap rounded-lg px-3 py-2 opacity-0 transition-opacity group-hover:opacity-100">
                    <p className="text-sm font-medium">{label}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <div className="mt-12 grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {locations.map(([country, city, code]) => (
              <div className="card-elevated group p-6 text-center transition-all hover:border-secondary/50" key={country}>
                <span className="mb-4 block text-2xl font-bold text-secondary transition-transform group-hover:scale-110">{code}</span>
                <h4 className="font-semibold text-foreground">{city}</h4>
                <p className="text-sm text-muted-foreground">{country}</p>
              </div>
            ))}
          </div>
        </div>
        <CTA />
      </div>
    </section>
  );
}

function ToolsPage() {
  return (
    <>
      <section className="relative overflow-hidden pb-16 pt-32">
        <div className="absolute inset-0 bg-gradient-to-b from-primary/10 via-transparent to-transparent" />
        <div className="section-container relative z-10">
          <div className="mx-auto max-w-3xl text-center">
            <div className="glass mb-8 inline-flex items-center gap-2 rounded-full px-4 py-2">
              <Download className="h-4 w-4 text-secondary" />
              <span className="text-sm text-muted-foreground">Ressources gratuites</span>
            </div>
            <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
              Boîte à <span className="text-gradient">outils</span>
            </h1>
            <p className="mb-8 text-xl text-muted-foreground">
              Templates, guides et outils gratuits pour booster votre stratégie marketing.
            </p>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24">
        <div className="section-container">
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {resources.map((resource) => {
              const Icon = resource.icon;
              return (
                <div
                  className="card-elevated group rounded-xl p-6 transition-all hover:border-secondary/50"
                  key={resource.id}
                >
                  <div className="mb-4 flex items-start justify-between">
                    <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-secondary transition-transform group-hover:scale-110">
                      <Icon className="h-6 w-6 text-primary-foreground" />
                    </div>
                    <span className={`rounded-full px-2 py-1 text-xs ${categoryStyles[resource.category]}`}>
                      {resource.category}
                    </span>
                  </div>
                  <h3 className="mb-2 text-lg font-semibold transition-colors group-hover:text-secondary">
                    {resource.title}
                  </h3>
                  <p className="mb-4 line-clamp-2 text-sm text-muted-foreground">{resource.description}</p>
                  <div className="flex items-center justify-between gap-3">
                    <span className="text-xs text-muted-foreground">Format: {resource.format}</span>
                    <a className="btn btn-outline-hero h-9 px-3 text-sm" href={resource.downloadUrl} download>
                      <Download className="mr-2 h-4 w-4" />
                      Télécharger
                    </a>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      <section className="bg-card py-16 md:py-24">
        <div className="section-container">
          <div className="mx-auto max-w-2xl text-center">
            <h2 className="mb-6 font-display text-3xl font-bold md:text-4xl">
              Besoin d'outils <span className="text-gradient">sur-mesure</span> ?
            </h2>
            <p className="mb-8 text-muted-foreground">
              Nous pouvons créer des templates et outils personnalisés adaptés à vos besoins spécifiques.
            </p>
            <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
              Discutons de votre besoin
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

function CTA() {
  return (
    <div className="mx-auto mt-20 max-w-2xl rounded-3xl bg-gradient-to-br from-primary/20 via-secondary/10 to-accent/30 p-8 text-center md:p-12">
      <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">
        Parlons de votre <span className="text-gradient">projet</span>
      </h2>
      <p className="mb-8 text-muted-foreground">
        Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.
      </p>
      <form className="mx-auto flex max-w-md flex-col gap-4 sm:flex-row">
        <input
          className="h-12 flex-1 rounded-lg border border-border bg-card px-4 outline-none transition-colors focus:border-secondary"
          placeholder="Votre adresse email"
          type="email"
        />
        <button className="btn btn-hero h-12" type="button">
          Envoyer
          <ArrowRight className="ml-2 h-5 w-5" />
        </button>
      </form>
      <p className="mt-4 text-xs text-muted-foreground">
        En soumettant ce formulaire, vous acceptez d'être contacté par notre équipe.
      </p>
    </div>
  );
}
