import { ArrowRight, BarChart3, Check, ClipboardList, Layers, Rocket, ShieldCheck, Sparkles, Target, Users } from "lucide-react";
import { Link } from "react-router-dom";
import SectionHeading from "../components/SectionHeading.jsx";

const pillars = [
  {
    number: "01",
    icon: ClipboardList,
    title: "DIAGNOSTIC",
    subtitle: "Comprendre",
    description:
      "Analyse stratégique 360° de votre écosystème pour identifier opportunités et blocages sur les volets stratégie, leadership et humain.",
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
    title: "STRUCTURATION",
    subtitle: "Aligner",
    description:
      "Co-construction de votre moteur de croissance harmonieux avec une feuille de route intégrée articulant stratégie commerciale, développement du leadership et performance humaine.",
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
    title: "ACTIVATION",
    subtitle: "Transformer",
    description:
      "Mise en œuvre opérationnelle des leviers de croissance : campagnes digitales, coaching dirigeants, formations managériales et programmes expérientiels.",
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
    title: "OPTIMISATION",
    subtitle: "Pérenniser",
    description:
      "Amélioration continue, mesure des résultats, ajustements tactiques et consolidation des acquis pour garantir une performance durable.",
    bullets: [
      "Performance des campagnes et ROI",
      "Évolution des pratiques managériales",
      "Solidité de la cohésion d'équipe",
      "Autonomie et appropriation des outils",
    ],
  },
];

const values = [
  "Exigence",
  "Responsabilité",
  "Orientation résultats",
  "L'humain au cœur",
  "Courage managérial",
  "Engagement collectif",
];

export default function HwcMethod() {
  return (
    <>
      <Hero />
      <Pillars />
      <Why360 />
      <ValuesSection />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative overflow-hidden pb-16 pt-24 md:pt-32">
      <div className="absolute inset-0 bg-[var(--gradient-hero)]" />
      <div
        className="absolute inset-0 bg-cover bg-center opacity-15"
        style={{ backgroundImage: "url(https://images.unsplash.com/photo-1521737604893-d14cc237f11d?w=1600)" }}
      />
      <div className="absolute right-0 top-1/3 h-96 w-96 rounded-full bg-primary/5 blur-3xl" />
      <div className="absolute bottom-0 left-1/4 h-72 w-72 rounded-full bg-secondary/5 blur-3xl" />
      <div className="section-container relative z-10 py-20">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full bg-primary/10 px-4 py-2 text-sm font-medium text-primary">
            <Sparkles className="h-4 w-4" />
            La Méthode HWC 360°
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            Une approche <span className="text-gradient">intégrale</span> pour des résultats mesurables
          </h1>
          <p className="mx-auto mb-8 max-w-3xl text-lg text-muted-foreground md:text-xl">
            Une approche intégrale qui aligne diagnostic stratégique, activation opérationnelle et optimisation continue
            pour transformer vos enjeux en résultats mesurables.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <a className="btn btn-hero h-12 px-7 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
              Découvrir la méthode
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <Link className="btn btn-outline-hero h-12 px-7 text-base" to="/offres/regie-outsourcing">
              Voir nos offres Régie
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}

function Pillars() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <p className="mx-auto mb-12 max-w-4xl text-center text-lg text-muted-foreground">
          L'approche HWC 360° ne traite jamais un levier de façon isolée. Chaque diagnostic intègre les 3 dimensions -
          stratégie, leadership, humain. Chaque action génère un impact systémique. Chaque résultat est mesuré sur la durée.
        </p>
        <div className="space-y-16">
          {pillars.map((pillar, index) => {
            const Icon = pillar.icon;
            const reverse = index % 2 === 1;
            return (
              <div className="grid items-center gap-12 lg:grid-cols-2" key={pillar.number}>
                <div className={reverse ? "lg:order-2" : ""}>
                  <div className="mb-4 flex items-center gap-4">
                    <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-primary to-secondary">
                      <Icon className="h-7 w-7 text-primary-foreground" />
                    </div>
                    <span className="font-display text-5xl font-bold text-primary/15">{pillar.number}</span>
                  </div>
                  <h2 className="mb-2 font-display text-2xl font-bold text-foreground md:text-3xl">
                    {pillar.title} <span className="text-gradient">— {pillar.subtitle}</span>
                  </h2>
                  <p className="mb-6 text-lg text-muted-foreground">{pillar.description}</p>
                  <div className="space-y-3">
                    <p className="font-semibold text-foreground">Ce que nous analysons / structurons / activons :</p>
                    {pillar.bullets.map((item) => (
                      <div className="flex items-start gap-3" key={item}>
                        <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                        <span className="text-foreground">{item}</span>
                      </div>
                    ))}
                  </div>
                </div>
                <div className={`card-elevated bg-gradient-to-br from-primary/5 to-secondary/5 p-8 ${reverse ? "lg:order-1" : ""}`}>
                  <div className="relative mx-auto flex aspect-square max-w-xs items-center justify-center">
                    <div className="absolute inset-0 animate-rotate-slow rounded-full border-2 border-dashed border-border/50" />
                    <div className="absolute inset-6 rounded-full border border-secondary/20" />
                    <div className="absolute inset-12 rounded-full bg-gradient-to-br from-primary/10 to-secondary/10" />
                    <div className="relative z-10 text-center">
                      <Icon className="mx-auto mb-3 h-16 w-16 text-primary" />
                      <p className="font-display text-lg font-bold text-foreground">{pillar.subtitle}</p>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function Why360() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Pourquoi 360° ?" subtitle="Une vision intégrale de votre performance" />
        <div className="grid gap-12 lg:grid-cols-2">
          <div>
            <p className="mb-4 text-muted-foreground">
              360° symbolise notre approche complète et circulaire : nous analysons tous les angles de votre organisation -
              stratégie commerciale, leadership, dynamiques humaines - pour créer une transformation cohérente où chaque
              dimension nourrit les autres.
            </p>
            <p className="text-muted-foreground">
              Pendant que d'autres consultants traitent les symptômes isolément, HWC 360° traite le système dans sa
              globalité pour générer un impact durable et mesurable.
            </p>
          </div>
          <div className="card-elevated p-6">
            <h3 className="mb-4 text-xl font-semibold">Nos valeurs en action</h3>
            <div className="flex flex-wrap gap-2">
              {values.map((value) => (
                <span key={value} className="rounded-full bg-muted px-4 py-2 text-sm font-medium text-foreground">
                  {value}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

function ValuesSection() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Nos Valeurs en Action" subtitle="Ce qui guide notre accompagnement au quotidien" />
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {[
            "Exigence — Rigueur méthodologique à chaque étape",
            "Responsabilité — Engagement sur les résultats",
            "Orientation résultats — KPIs clairs et mesure continue",
            "L'humain au cœur — Accompagnement comportemental systématique",
            "Courage managérial — Nous challengeons avec bienveillance",
            "Engagement collectif — Nous avançons à vos côtés comme partenaires",
          ].map((item) => (
            <div className="card-elevated p-6" key={item}>
              <Check className="mb-3 h-5 w-5 text-secondary" />
              <p className="text-sm leading-relaxed text-foreground">{item}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function FinalCta() {
  return (
    <section className="bg-gradient-to-br from-primary to-secondary py-16 text-primary-foreground md:py-24">
      <div className="section-container">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">
            Prêt à transformer vos résultats avec HWC 360° ?
          </h2>
          <p className="mb-8 text-lg text-primary-foreground/80">
            Parlons de votre contexte et voyons comment la méthode peut structurer une transformation durable.
          </p>
          <a className="btn h-12 bg-white px-7 text-primary hover:bg-white/90" href="https://calendly.com" target="_blank" rel="noreferrer">
            Réserver un appel découverte
            <ArrowRight className="ml-2 h-5 w-5" />
          </a>
        </div>
      </div>
    </section>
  );
}
