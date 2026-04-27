import { useState } from "react";
import { Link } from "react-router-dom";
import {
  ArrowRight,
  BarChart3,
  Brain,
  Check,
  ChevronLeft,
  ChevronRight,
  HeartHandshake,
  Lightbulb,
  MessageCircle,
  Quote,
  Shield,
  Sparkles,
  Target,
  Trophy,
  Users,
  Zap,
} from "lucide-react";
import SectionHeading from "../components/SectionHeading.jsx";

const approachBullets = [
  "Mieux comprendre son fonctionnement naturel",
  "Identifier ses forces et ses zones de vigilance",
  "Ajuster sa posture et sa communication selon les situations et les interlocuteurs",
  "Renforcer durablement son efficacité relationnelle et managériale",
];

const extraApproach = [
  "Cette approche s'appuie sur des référentiels comportementaux et psychométriques reconnus internationalement, intégrés comme des outils au service de votre transformation, jamais comme une finalité.",
];

const whyBehaviours = [
  "Décoder vos modes de communication et ceux de vos interlocuteurs",
  "Créer une vraie cohésion d'équipe et éliminer les malentendus",
  "Ajuster votre leadership à chaque situation et chaque profil",
  "Développer votre impact commercial grâce à une communication précise",
  "Construire des relations plus fluides et authentiques",
];

const accompagnements = [
  {
    title: "Accompagnement individuel",
    description: "Transformation de votre posture managériale et impact professionnel durable",
    icon: HeartHandshake,
  },
  {
    title: "Accompagnement des équipes",
    description: "Cohésion collective et efficacité renforcée pour équipes performantes",
    icon: Users,
  },
  {
    title: "Développement du leadership managérial",
    description: "Maîtrise des leviers du management moderne et inspirant",
    icon: Target,
  },
  {
    title: "Performance des forces de vente",
    description: "Excellence commerciale et techniques de closing avancées",
    icon: BarChart3,
  },
  {
    title: "Accompagnement des couples",
    description: "Harmonie relationnelle et équilibre entre vie personnelle et professionnelle",
    icon: HeartHandshake,
  },
];

const methodology = [
  {
    step: 1,
    icon: Lightbulb,
    title: "Analyse comportementale individuelle",
    description: "Compréhension claire du style, des forces et des zones de vigilance",
  },
  {
    step: 2,
    icon: Brain,
    title: "Ateliers pratiques et interactifs",
    description: "Exercices, mises en situation, retours d'expérience",
  },
  {
    step: 3,
    icon: Target,
    title: "Accompagnement stratégique ciblé",
    description: "Objectifs personnalisés, leviers de progression, plans d'action",
  },
  {
    step: 4,
    icon: Shield,
    title: "Suivi structuré",
    description: "Ancrage des apprentissages et transformation durable des comportements",
  },
];

const testimonials = [
  {
    quote:
      "J'ai transformé mon style de leadership et amélioré la communication avec mon équipe.",
    author: "Directeur d'équipe",
    role: "Secteur industriel",
  },
  {
    quote:
      "Approche pragmatique et résultats rapides sur la cohésion de notre équipe.",
    author: "Responsable commercial",
    role: "Secteur services",
  },
  {
    quote:
      "Les ateliers ont débloqué des situations de coopération qui trainaient depuis des mois.",
    author: "Dirigeante PME",
    role: "Secteur conseil",
  },
];

export default function LeadershipCorporateEvents() {
  return (
    <>
      <Hero />
      <Approach />
      <Accompagnements />
      <Methodology />
      <Testimonials />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative flex min-h-[72vh] items-center overflow-hidden pb-16 pt-24">
      <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-secondary/5" />
      <div
        className="absolute inset-0 bg-cover bg-center opacity-15"
        style={{ backgroundImage: "url(https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=1600)" }}
      />
      <div className="section-container relative z-10">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
            <Sparkles className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">Formation, Leadership & Organisation</span>
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            Leadership et <span className="text-gradient">performance humaine</span>
          </h1>
          <p className="mx-auto mb-8 max-w-3xl text-xl text-muted-foreground">
            Libérez le potentiel humain pour des équipes performantes et des relations harmonieuses.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
              Développer mon leadership
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <a className="btn btn-outline-hero h-14 px-8 text-base" href="#accompagnements">
              Nos accompagnements
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

function Approach() {
  const [expanded, setExpanded] = useState(false);

  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <div className="grid items-start gap-12 lg:grid-cols-2">
          <div>
            <SectionHeading title="Notre approche" centered={false} />
            <p className="mb-6 text-muted-foreground">
              Nous aidons nos clients à transformer la compréhension des comportements humains en leviers concrets de
              leadership, de communication, de coopération et de performance collective.
            </p>
            <p className="mb-6 text-muted-foreground">
              À travers une démarche structurée et pragmatique, nous permettons à chacun de :
            </p>
            <ul className="space-y-3">
              {approachBullets.map((item) => (
                <li className="flex items-start gap-3" key={item}>
                  <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                  <span className="text-foreground">{item}</span>
                </li>
              ))}
            </ul>
            <button
              type="button"
              onClick={() => setExpanded((current) => !current)}
              className="btn btn-outline-hero mt-6 h-11 px-5 text-sm"
            >
              Voir plus
            </button>
            <div
              className={`grid transition-all duration-300 ease-out ${expanded ? "grid-rows-[1fr] opacity-100 mt-6" : "grid-rows-[0fr] opacity-0 mt-0"}`}
            >
              <div className="overflow-hidden">
                <p className="text-muted-foreground">{extraApproach[0]}</p>
                <h3 className="mb-4 mt-6 font-display text-2xl font-bold">
                  Pourquoi travailler sur les comportements humains ?
                </h3>
                <p className="mb-4 text-muted-foreground">
                  Dans un monde en perpétuel mouvement, comprendre les comportements humains n'est plus une option :
                  c'est un levier de réussite essentiel.
                </p>
                <ul className="space-y-3">
                  {whyBehaviours.map((item) => (
                    <li className="flex items-start gap-3" key={item}>
                      <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                      <span className="text-foreground">{item}</span>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </div>
          <div className="card-elevated rounded-2xl p-8">
            <div className="mb-4 flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-secondary to-accent">
                <Brain className="h-6 w-6 text-primary-foreground" />
              </div>
              <h3 className="text-lg font-semibold">Référentiels internationaux</h3>
            </div>
            <p className="text-sm text-muted-foreground">
              Outils comportementaux et psychométriques au service de votre transformation, jamais comme une finalité.
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}

function Accompagnements() {
  return (
    <section id="accompagnements" className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Nos accompagnements"
          subtitle="Des programmes adaptés pour chaque contexte et chaque besoin"
        />
        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          {accompagnements.map((item) => {
            const Icon = item.icon;
            return (
              <Link
                to="/offres/leadership-corporate-events"
                className="group card-elevated p-6 transition-all hover:border-secondary/50"
                key={item.title}
              >
                <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-secondary to-accent">
                  <Icon className="h-6 w-6 text-primary-foreground" />
                </div>
                <h3 className="mb-2 text-xl font-semibold transition-colors group-hover:text-secondary">{item.title}</h3>
                <p className="mb-4 text-sm text-muted-foreground">{item.description}</p>
                <div className="flex items-center text-sm font-medium text-secondary">
                  En savoir plus
                  <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-2" />
                </div>
              </Link>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function Methodology() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Méthodologie" subtitle="Chaque accompagnement repose sur une démarche structurée et éprouvée" />
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
          {methodology.map((item) => {
            const Icon = item.icon;
            return (
              <div className="card-elevated relative p-6" key={item.step}>
                <div className="absolute -left-4 -top-4 flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary font-bold text-primary-foreground">
                  {item.step}
                </div>
                <div className="mb-4 mt-2 flex h-12 w-12 items-center justify-center rounded-xl bg-primary/10">
                  <Icon className="h-6 w-6 text-primary" />
                </div>
                <h4 className="mb-2 font-semibold text-foreground">{item.title}</h4>
                <p className="text-sm text-muted-foreground">{item.description}</p>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function Testimonials() {
  const [active, setActive] = useState(0);
  const testimonial = testimonials[active];

  const previous = () => {
    setActive((current) => (current === 0 ? testimonials.length - 1 : current - 1));
  };

  const next = () => {
    setActive((current) => (current === testimonials.length - 1 ? 0 : current + 1));
  };

  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Témoignages" subtitle="Ce que nos clients disent de nos accompagnements" />
        <div className="relative mx-auto max-w-4xl rounded-3xl bg-card py-16">
          <button
            type="button"
            onClick={previous}
            className="absolute left-4 top-1/2 hidden h-11 w-11 -translate-y-1/2 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary md:flex"
            aria-label="Témoignage précédent"
          >
            <ChevronLeft className="h-5 w-5" />
          </button>

          <div className="mx-auto max-w-2xl px-12 text-center">
            <Quote className="mx-auto mb-6 h-12 w-12 text-secondary/30" />
            <p className="mb-8 font-display text-xl font-medium text-foreground md:text-2xl">{testimonial.quote}</p>
            <p className="font-semibold text-foreground">{testimonial.author}</p>
            <p className="text-muted-foreground">{testimonial.role}</p>
          </div>

          <button
            type="button"
            onClick={next}
            className="absolute right-4 top-1/2 hidden h-11 w-11 -translate-y-1/2 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary md:flex"
            aria-label="Témoignage suivant"
          >
            <ChevronRight className="h-5 w-5" />
          </button>

          <div className="mt-8 flex items-center justify-center gap-3">
            {testimonials.map((item, index) => (
              <button
                key={item.author}
                type="button"
                onClick={() => setActive(index)}
                className={`h-3 rounded-full transition-all ${
                  index === active ? "w-8 bg-secondary" : "w-3 bg-muted hover:bg-muted-foreground/30"
                }`}
                aria-label={`Voir le témoignage ${index + 1}`}
                aria-current={index === active}
              />
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

function FinalCta() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">
            Prêt à booster votre <span className="text-gradient">leadership</span> et votre performance humaine ?
          </h2>
          <p className="mb-3 text-lg font-medium text-muted-foreground">L'humain au cœur de la performance</p>
          <p className="mb-8 text-muted-foreground">
            L'humain au cœur de la performance
          </p>
          <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
            Parlons-en
            <ArrowRight className="ml-2 h-5 w-5" />
          </a>
        </div>
      </div>
    </section>
  );
}
