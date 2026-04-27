import { ArrowRight, BarChart3, Brain, Layers, Megaphone, Shield, Target, Users } from "lucide-react";
import { Link } from "react-router-dom";
import SectionHeading from "./SectionHeading.jsx";

const offers = [
  {
    icon: Megaphone,
    title: "Marketing Digital",
    subtitle: "Développer votre moteur de croissance",
    description:
      "Structuration de votre stratégie d'acquisition, génération de leads qualifiés, optimisation SEO et automatisation intelligente pour accélérer votre développement commercial avec des résultats mesurables.",
    href: "/offres/regie-outsourcing/marketing-digital",
    color: "from-primary to-secondary",
    features: ["Lead generation", "SEO et SEA", "Création du contenu", "Marketing automation", "Intelligence artificielle"],
  },
  {
    icon: Users,
    title: "Leadership & performance humaine",
    subtitle: "Développer un leadership responsable",
    description:
      "Accompagnement stratégique et comportemental des dirigeants, managers et équipes pour développer un leadership conscient, une communication impactante et une performance collective durable.",
    href: "/offres/leadership-corporate-events",
    color: "from-secondary to-accent",
    features: ["Coaching Dirigeants", "Analyse Comportementale", "Efficacité Commerciale", "Posture & Communication"],
  },
  {
    icon: Shield,
    title: "Performance sous pression",
    subtitle: "Forger des leaders résilients",
    description:
      "Programmes expérientiels par le Krav Maga pour renforcer la prise de décision en situation de stress, la cohésion d'équipe et la gestion constructive des tensions.",
    href: "/offres/leadership-corporate-events",
    color: "from-primary to-accent",
    features: ["Leadership sous stress", "Team building & Cohésion", "Gestion des conflits", "Communication assertive"],
  },
];

const methodSteps = [
  {
    icon: Target,
    title: "DIAGNOSTIC",
    description: "Analyse stratégique 360° de votre écosystème",
  },
  {
    icon: Layers,
    title: "STRUCTURATION",
    description: "Co-construction de votre moteur de croissance harmonieux",
  },
  {
    icon: ArrowRight,
    title: "ACTIVATION",
    description: "Mise en œuvre opérationnelle de vos leviers de croissance",
  },
  {
    icon: BarChart3,
    title: "OPTIMISATION",
    description: "Amélioration continue et ancrage des transformations",
  },
];

export default function Services() {
  return (
    <section id="services" className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="3 domaines d'expertise à votre service"
          subtitle="Une approche 360° qui aligne stratégie de croissance, excellence managériale et performance humaine pour des résultats durables et mesurables."
        />
        <div className="grid gap-8 lg:grid-cols-3">
          {offers.map((offer) => {
            const Icon = offer.icon;
            return (
              <Link
                to={offer.href}
                className="group card-elevated card-3d p-8 transition-all hover:border-secondary/50"
                key={offer.title}
              >
                <div
                  className={`mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br ${offer.color} transition-transform group-hover:scale-110`}
                >
                  <Icon className="h-8 w-8 text-primary-foreground" />
                </div>
                <p className="mb-2 text-xs font-medium uppercase tracking-wider text-secondary">{offer.subtitle}</p>
                <h3 className="mb-4 font-display text-2xl font-bold transition-colors group-hover:text-secondary">
                  {offer.title}
                </h3>
                <p className="mb-6 text-sm leading-relaxed text-muted-foreground">{offer.description}</p>
                <div className="mb-6 flex flex-wrap gap-2">
                  {offer.features.map((feature) => (
                    <span className="rounded-full bg-muted px-3 py-1 text-xs text-muted-foreground" key={feature}>
                      {feature}
                    </span>
                  ))}
                </div>
                <div className="flex items-center font-medium text-secondary">
                  Découvrir
                  <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-2" />
                </div>
              </Link>
            );
          })}
        </div>
      </div>

      <div className="relative mt-20 overflow-hidden bg-background py-4">
        <div className="section-container">
          <div className="mx-auto mb-12 max-w-3xl text-center">
            <h2 className="mb-4 font-display text-3xl font-bold leading-tight text-primary md:text-4xl lg:text-5xl">
              Notre méthode éprouvée HWC 360°
            </h2>
            <p className="mx-auto max-w-2xl text-lg leading-relaxed text-muted-foreground">
              Une approche intégrale qui aligne diagnostic stratégique, activation opérationnelle et optimisation continue
              pour transformer vos enjeux en résultats mesurables.
            </p>
          </div>

          <div className="grid items-center gap-12 lg:grid-cols-2">
            <div className="space-y-5">
              {methodSteps.map((step, index) => {
                const Icon = step.icon;
                return (
                  <div className="flex items-start gap-4" key={step.title}>
                    <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-primary text-primary-foreground shadow-card">
                      <Icon className="h-6 w-6" />
                    </div>
                    <div>
                      <h3 className="mb-1 font-display text-lg font-bold leading-tight text-foreground">
                        Étape {index + 1} : {step.title}
                      </h3>
                      <p className="text-sm leading-relaxed text-muted-foreground">{step.description}</p>
                    </div>
                  </div>
                );
              })}

              <Link className="btn btn-hero h-12 rounded-xl px-7 text-base shadow-elevated" to="/hwc-method">
                Découvrir la méthode HWC 360°
                <ArrowRight className="ml-3 h-5 w-5" />
              </Link>
            </div>

            <MethodOrbit />
          </div>
        </div>
      </div>
    </section>
  );
}

function MethodOrbit() {
  const orbitIcons = [
    { icon: Target, className: "left-1/2 top-[7%] -translate-x-1/2" },
    { icon: Brain, className: "bottom-[19%] left-[9%]" },
    { icon: BarChart3, className: "bottom-[19%] right-[9%]" },
  ];

  return (
    <div className="relative min-h-[330px] lg:min-h-[400px]">
      <div className="absolute inset-x-0 top-8 mx-auto h-[78%] max-w-[520px] rounded-[48%] border border-secondary/25" />
      <div className="absolute inset-x-[4%] top-2 mx-auto h-[90%] max-w-[520px] animate-rotate-slow rounded-[46%] border-2 border-dashed border-secondary/20" />
      <div className="absolute left-1/2 top-1/2 h-[220px] w-[76%] max-w-[470px] -translate-x-1/2 -translate-y-1/2 rounded-full border-[34px] border-secondary/20 bg-background shadow-[0_30px_90px_-55px_hsl(168_35%_45%/.45)] md:h-[260px] md:border-[42px] lg:h-[285px] lg:border-[48px]" />

      <div className="absolute left-1/2 top-1/2 flex -translate-x-1/2 -translate-y-1/2 flex-col items-center text-center">
        <Layers className="mb-2 h-8 w-8 text-primary" />
        <p className="font-display text-2xl font-bold text-primary">HWC</p>
        <p className="mt-0.5 text-sm font-medium tracking-wide text-muted-foreground">360°</p>
      </div>

      {orbitIcons.map((item, index) => {
        const Icon = item.icon;
        return (
          <div
            className={`absolute flex h-11 w-11 items-center justify-center rounded-full bg-secondary text-secondary-foreground shadow-card ${item.className}`}
            key={index}
          >
            <Icon className="h-5 w-5" />
          </div>
        );
      })}
    </div>
  );
}
