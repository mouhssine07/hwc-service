import {
  ArrowRight,
  Brain,
  Check,
  HeartHandshake,
  MessageCircle,
  Shield,
  Target,
  Users,
  Zap,
} from "lucide-react";
import SectionHeading from "../components/SectionHeading.jsx";

const pillars = [
  {
    icon: Zap,
    title: "Leadership sous stress",
    description:
      "Développer la lucidité, la prise de décision rapide et la stabilité émotionnelle quand la pression monte.",
  },
  {
    icon: Users,
    title: "Team building & cohésion",
    description:
      "Créer des expériences collectives fortes pour renforcer la confiance, la coopération et l'engagement.",
  },
  {
    icon: Shield,
    title: "Gestion des conflits",
    description:
      "Apprendre à désamorcer les tensions, poser un cadre clair et maintenir une posture constructive.",
  },
  {
    icon: MessageCircle,
    title: "Communication assertive",
    description:
      "S'exprimer avec clarté, fermeté et respect, même dans les situations sensibles ou conflictuelles.",
  },
];

const process = [
  {
    step: 1,
    title: "Cadrage des enjeux",
    description:
      "Identification des situations de pression, des tensions récurrentes et des objectifs comportementaux à travailler.",
  },
  {
    step: 2,
    title: "Immersion expérientielle",
    description:
      "Ateliers inspirés du Krav Maga pour expérimenter le stress, la posture, la vigilance et la décision en temps réel.",
  },
  {
    step: 3,
    title: "Débrief comportemental",
    description:
      "Analyse des réactions individuelles et collectives pour transformer l'expérience physique en apprentissages managériaux.",
  },
  {
    step: 4,
    title: "Ancrage opérationnel",
    description:
      "Transposition dans les situations professionnelles : management, conflit, vente, communication et cohésion d'équipe.",
  },
];

const outcomes = [
  "Meilleure prise de décision en situation de stress",
  "Posture plus stable face aux tensions",
  "Cohésion renforcée entre managers et équipes",
  "Communication plus claire dans les moments sensibles",
  "Confiance collective et engagement durable",
  "Réflexes concrets pour gérer les conflits",
];

export default function PerformanceSousPression() {
  return (
    <>
      <Hero />
      <Intro />
      <Pillars />
      <ProcessSection />
      <Outcomes />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative flex min-h-[72vh] items-center overflow-hidden pb-16 pt-24 md:pt-32">
      <div className="absolute inset-0 bg-gradient-to-br from-primary/15 via-background to-secondary/10" />
      <div
        className="absolute inset-0 bg-cover bg-center opacity-20"
        style={{
          backgroundImage:
            "url(https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1600)",
        }}
      />
      <div className="section-container relative z-10">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
            <Shield className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">
              Performance sous pression
            </span>
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            Forger des leaders <span className="text-gradient">résilients</span>
          </h1>
          <p className="mx-auto max-w-3xl text-xl leading-relaxed text-muted-foreground">
            Des programmes expérientiels inspirés du Krav Maga pour renforcer la
            prise de décision sous stress, la cohésion d'équipe et la gestion
            constructive des tensions.
          </p>
          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <a
              className="btn btn-hero h-14 px-8 text-base"
              href="https://calendly.com"
              target="_blank"
              rel="noreferrer"
            >
              Construire un programme
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <a className="btn btn-outline-hero h-14 px-8 text-base" href="#programme">
              Découvrir l'approche
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

function Intro() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <div className="grid items-center gap-12 lg:grid-cols-2">
          <div>
            <SectionHeading
              title="Transformer la pression en levier de performance"
              centered={false}
            />
            <p className="mb-5 leading-relaxed text-muted-foreground">
              La pression révèle les réflexes individuels et collectifs :
              posture, communication, décision, coopération, gestion des
              tensions. Notre approche utilise l'expérience corporelle comme
              point d'entrée pour créer des apprentissages concrets et
              transférables dans l'entreprise.
            </p>
            <p className="leading-relaxed text-muted-foreground">
              L'objectif n'est pas la pratique sportive en elle-même, mais le
              développement de comportements utiles : lucidité, assertivité,
              confiance, responsabilité et solidarité sous contrainte.
            </p>
          </div>
          <div className="card-elevated p-8">
            <div className="mb-5 flex h-14 w-14 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-secondary">
              <Brain className="h-7 w-7 text-primary-foreground" />
            </div>
            <h3 className="mb-3 text-xl font-semibold text-foreground">
              Une expérience physique, un impact managérial
            </h3>
            <p className="text-sm leading-relaxed text-muted-foreground">
              Chaque mise en situation est suivie d'un débrief structuré pour
              relier les réactions observées aux enjeux réels de leadership, de
              communication et de performance collective.
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}

function Pillars() {
  return (
    <section id="programme" className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Les axes du programme"
          subtitle="Des ateliers conçus pour travailler les comportements décisifs en situation de pression."
        />
        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {pillars.map((item) => {
            const Icon = item.icon;
            return (
              <div className="card-elevated p-6" key={item.title}>
                <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-secondary/10">
                  <Icon className="h-6 w-6 text-secondary" />
                </div>
                <h3 className="mb-2 text-lg font-semibold text-foreground">
                  {item.title}
                </h3>
                <p className="text-sm leading-relaxed text-muted-foreground">
                  {item.description}
                </p>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

function ProcessSection() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Notre déroulé"
          subtitle="Une méthode progressive pour passer de l'expérience à l'ancrage professionnel."
        />
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
          {process.map((item) => (
            <div className="card-elevated relative p-6" key={item.step}>
              <div className="absolute -left-4 -top-4 flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary font-bold text-primary-foreground">
                {item.step}
              </div>
              <h3 className="mb-2 mt-4 font-semibold text-foreground">
                {item.title}
              </h3>
              <p className="text-sm leading-relaxed text-muted-foreground">
                {item.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function Outcomes() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <div className="grid items-start gap-12 lg:grid-cols-2">
          <div>
            <SectionHeading
              title="Résultats attendus"
              subtitle="Des bénéfices visibles dans les interactions, les décisions et la cohésion."
              centered={false}
            />
          </div>
          <div className="grid gap-3">
            {outcomes.map((item) => (
              <div className="flex items-start gap-3 rounded-xl bg-card p-4 shadow-card" key={item}>
                <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                <span className="text-sm text-foreground">{item}</span>
              </div>
            ))}
          </div>
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
          <HeartHandshake className="mx-auto mb-5 h-10 w-10 text-secondary" />
          <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">
            Concevoir une expérience adaptée à vos équipes
          </h2>
          <p className="mx-auto mb-8 max-w-2xl text-muted-foreground">
            Nous construisons le format selon votre contexte : séminaire,
            comité de direction, équipe commerciale, managers ou collectif en
            transformation.
          </p>
          <a
            className="btn btn-hero h-14 px-8 text-base"
            href="https://calendly.com"
            target="_blank"
            rel="noreferrer"
          >
            Planifier un échange
            <ArrowRight className="ml-2 h-5 w-5" />
          </a>
        </div>
      </div>
    </section>
  );
}
