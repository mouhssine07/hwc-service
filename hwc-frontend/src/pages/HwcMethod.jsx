import {
  ArrowRight,
  BarChart3,
  Check,
  ClipboardList,
  Compass,
  Handshake,
  HeartHandshake,
  Layers,
  LineChart,
  Rocket,
  ShieldCheck,
  Sparkles,
  Target,
  Users,
} from "lucide-react";
import { Link } from "react-router-dom";
import SectionHeading from "../components/SectionHeading.jsx";

const stages = [
  {
    number: "01",
    icon: ClipboardList,
    label: "DIAGNOSTIC",
    verb: "Comprendre",
    title: "Analyse stratégique 360° de votre écosystème",
    description:
      "Diagnostic approfondi de vos leviers de croissance actuels, de votre maturité managériale et de vos dynamiques humaines pour identifier opportunités et blocages.",
    caption: "Ce que nous analysons :",
    bullets: [
      "Positionnement commercial et stratégie d'acquisition",
      "Maturité du leadership et pratiques managériales",
      "Performance collective et dynamiques d'équipe",
      "Alignement entre ambitions et moyens",
    ],
  },
  {
    number: "02",
    icon: Layers,
    label: "STRUCTURATION",
    verb: "Aligner",
    title: "Co-construction de votre moteur de croissance harmonieux",
    description:
      "Définition claire de votre vision, de vos objectifs mesurables et de votre feuille de route intégrée articulant stratégie commerciale, développement du leadership et performance humaine.",
    caption: "Ce que nous structurons :",
    bullets: [
      "Stratégie d'acquisition multicanale",
      "Plan de développement managérial",
      "Programmes de cohésion et résilience",
      "Indicateurs de performance (KPIs)",
    ],
  },
  {
    number: "03",
    icon: Rocket,
    label: "ACTIVATION",
    verb: "Transformer",
    title: "Mise en œuvre opérationnelle de vos leviers de croissance",
    description:
      "Déploiement concret des actions : campagnes digitales, coaching dirigeants, formations managériales, programmes expérientiels et accompagnement comportemental.",
    caption: "Ce que nous activons :",
    bullets: [
      "Génération de leads et conversion",
      "Coaching exécutif et managérial",
      "Team building et cohésion sous pression",
      "Communication assertive et gestion des tensions",
    ],
  },
  {
    number: "04",
    icon: BarChart3,
    label: "OPTIMISATION",
    verb: "Pérenniser",
    title: "Amélioration continue et ancrage des transformations",
    description:
      "Mesure rigoureuse des résultats, ajustements tactiques, consolidation des acquis et développement de l'autonomie pour garantir une performance durable.",
    caption: "Ce que nous optimisons :",
    bullets: [
      "Performance des campagnes et ROI",
      "Évolution des pratiques managériales",
      "Solidité de la cohésion d'équipe",
      "Autonomie et appropriation des outils",
    ],
  },
];

const dimensions = [
  { icon: Target, label: "Stratégie commerciale" },
  { icon: ShieldCheck, label: "Leadership" },
  { icon: Users, label: "Dynamiques humaines" },
];

const values = [
  {
    icon: Sparkles,
    title: "Exigence",
    description: "Rigueur méthodologique à chaque étape",
  },
  {
    icon: ShieldCheck,
    title: "Responsabilité",
    description: "Engagement sur les résultats",
  },
  {
    icon: LineChart,
    title: "Orientation résultats",
    description: "KPIs clairs et mesure continue",
  },
  {
    icon: HeartHandshake,
    title: "L'humain au cœur",
    description: "Accompagnement comportemental systématique",
  },
  {
    icon: Compass,
    title: "Courage managérial",
    description: "Nous challengeons avec bienveillance",
  },
  {
    icon: Handshake,
    title: "Engagement collectif",
    description: "Nous avançons à vos côtés comme partenaires",
  },
];

export default function HwcMethod() {
  return (
    <>
      <Hero />
      <MethodIntro />
      <Stages />
      <Why360 />
      <ValuesSection />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative flex min-h-[76vh] items-center overflow-hidden pb-16 pt-24 md:pt-32">
      <div className="absolute inset-0 bg-gradient-to-br from-primary/15 via-background to-secondary/10" />
      <div
        className="absolute inset-0 bg-cover bg-center opacity-20"
        style={{
          backgroundImage:
            "url(https://images.unsplash.com/photo-1552664730-d307ca884978?w=1600)",
        }}
      />
      <div className="section-container relative z-10">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
            <Sparkles className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">
              La Méthode HWC 360°
            </span>
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            La Méthode <span className="text-gradient">HWC 360°</span>
          </h1>
          <p className="mx-auto max-w-3xl text-xl leading-relaxed text-muted-foreground">
            Une approche intégrale qui aligne diagnostic stratégique, activation
            opérationnelle et optimisation continue pour transformer vos enjeux
            en résultats mesurables.
          </p>
          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <a
              className="btn btn-hero h-14 px-8 text-base"
              href="https://calendly.com"
              target="_blank"
              rel="noreferrer"
            >
              Planifier un diagnostic
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <a className="btn btn-outline-hero h-14 px-8 text-base" href="#methode">
              Découvrir les étapes
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

function MethodIntro() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <div className="mx-auto max-w-4xl text-center">
          <p className="text-xl leading-relaxed text-muted-foreground">
            L'approche HWC 360° ne traite jamais un levier de façon isolée.
            Chaque diagnostic intègre les 3 dimensions : stratégie, leadership,
            humain. Chaque action génère un impact systémique. Chaque résultat
            est mesuré sur la durée.
          </p>
        </div>
        <div className="mt-10 grid gap-6 md:grid-cols-3">
          {dimensions.map((item) => {
            const Icon = item.icon;
            return (
              <div className="card-elevated p-6 text-center" key={item.label}>
                <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-primary/10">
                  <Icon className="h-6 w-6 text-primary" />
                </div>
                <h3 className="font-semibold text-foreground">{item.label}</h3>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function Stages() {
  return (
    <section id="methode" className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Les 4 temps de la méthode"
          subtitle="Comprendre, aligner, transformer, puis pérenniser la performance."
        />
        <div className="grid gap-6 lg:grid-cols-2">
          {stages.map((stage) => {
            const Icon = stage.icon;
            return (
              <article className="card-elevated p-6 md:p-8" key={stage.number}>
                <div className="mb-5 flex items-start justify-between gap-4">
                  <div className="flex items-center gap-4">
                    <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-secondary">
                      <Icon className="h-7 w-7 text-primary-foreground" />
                    </div>
                    <div>
                      <p className="text-xs font-semibold uppercase tracking-wider text-secondary">
                        {stage.label}
                      </p>
                      <h3 className="font-display text-2xl font-bold">
                        {stage.verb}
                      </h3>
                    </div>
                  </div>
                  <span className="font-display text-4xl font-bold text-primary/15">
                    {stage.number}
                  </span>
                </div>
                <h4 className="mb-3 text-xl font-semibold text-foreground">
                  {stage.title}
                </h4>
                <p className="mb-6 leading-relaxed text-muted-foreground">
                  {stage.description}
                </p>
                <p className="mb-3 font-semibold text-foreground">{stage.caption}</p>
                <ul className="space-y-3">
                  {stage.bullets.map((item) => (
                    <li className="flex items-start gap-3" key={item}>
                      <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                      <span className="text-sm text-foreground">{item}</span>
                    </li>
                  ))}
                </ul>
              </article>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function Why360() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <div className="grid items-center gap-12 lg:grid-cols-[1.05fr_0.95fr]">
          <div>
            <SectionHeading
              title="Pourquoi 360° ?"
              subtitle="Une vision intégrale de votre performance"
              centered={false}
            />
            <p className="mb-5 leading-relaxed text-muted-foreground">
              360° symbolise notre approche complète et circulaire : nous
              analysons tous les angles de votre organisation, stratégie
              commerciale, leadership, dynamiques humaines, pour créer une
              transformation cohérente où chaque dimension nourrit les autres.
            </p>
            <p className="leading-relaxed text-muted-foreground">
              Pendant que d'autres consultants traitent les symptômes isolément,
              HWC 360° traite le système dans sa globalité pour générer un
              impact durable et mesurable.
            </p>
          </div>
          <div className="relative mx-auto flex aspect-square w-full max-w-[420px] items-center justify-center">
            <div className="absolute inset-0 rounded-full border border-secondary/20" />
            <div className="absolute inset-8 animate-rotate-slow rounded-full border-2 border-dashed border-secondary/25" />
            <div className="absolute inset-20 rounded-full bg-gradient-to-br from-primary/10 to-secondary/10" />
            <div className="relative text-center">
              <Layers className="mx-auto mb-3 h-12 w-12 text-primary" />
              <p className="font-display text-4xl font-bold text-primary">HWC</p>
              <p className="text-lg font-semibold text-muted-foreground">360°</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

function ValuesSection() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Nos Valeurs en Action"
          subtitle="Des principes concrets qui guident chaque accompagnement."
        />
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {values.map((value) => {
            const Icon = value.icon;
            return (
              <div className="card-elevated p-6" key={value.title}>
                <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-secondary/10">
                  <Icon className="h-6 w-6 text-secondary" />
                </div>
                <h3 className="mb-2 text-lg font-semibold text-foreground">
                  {value.title}
                </h3>
                <p className="text-sm leading-relaxed text-muted-foreground">
                  {value.description}
                </p>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function FinalCta() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <div className="card-elevated rounded-3xl bg-gradient-to-br from-primary/5 via-background to-secondary/5 p-8 text-center md:p-12">
          <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">
            Transformons vos enjeux en résultats mesurables
          </h2>
          <p className="mx-auto mb-8 max-w-2xl text-muted-foreground">
            Un échange pour comprendre votre contexte, vos priorités et les
            leviers à activer avec la Méthode HWC 360°.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <a
              className="btn btn-hero h-14 px-8 text-base"
              href="https://calendly.com"
              target="_blank"
              rel="noreferrer"
            >
              Réserver un appel découverte
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <Link
              className="btn btn-outline-hero h-14 px-8 text-base"
              to="/offres/regie-outsourcing"
            >
              Voir nos offres
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
