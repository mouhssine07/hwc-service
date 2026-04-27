import { useState } from "react";
import {
  ArrowRight,
  BarChart3,
  BriefcaseBusiness,
  Check,
  ChevronDown,
  Headphones,
  Rocket,
  ShieldCheck,
  Sparkles,
  Target,
  Users,
  Wallet,
} from "lucide-react";
import { Link } from "react-router-dom";
import SectionHeading from "../components/SectionHeading.jsx";

const benefits = [
  {
    icon: Users,
    title: "Renforcement des capacités",
    description:
      "Renforcez vos équipes rapidement sans recruter localement. Accédez à des talents qualifiés, opérationnels en quelques semaines.",
  },
  {
    icon: Rocket,
    title: "Flexibilité totale",
    description:
      "Adaptez le périmètre des missions en fonction de vos besoins. Montez en charge ou recentrez vos priorités sans contraintes.",
  },
  {
    icon: BarChart3,
    title: "Performance garantie",
    description:
      "Vos collaborateurs sont recrutés, formés et pilotés par HWC IO. Chaque mission est suivie par des KPI et optimisée en continu.",
  },
  {
    icon: Wallet,
    title: "Réduction des coûts",
    description:
      "Optimisez vos coûts opérationnels tout en maintenant un haut niveau de qualité. Le modèle mauricien offre un ratio performance/coût exceptionnel.",
  },
];

const pillars = [
  {
    number: "01",
    icon: BriefcaseBusiness,
    title: "Une ressource dédiée",
    description:
      "Chaque mission repose sur un collaborateur qualifié, basé à l'île Maurice, sélectionné et formé spécifiquement pour répondre aux exigences de votre entreprise.",
  },
  {
    number: "02",
    icon: Sparkles,
    title: "L'intelligence collective HWC",
    description:
      "Votre collaborateur ne travaille jamais seul. Il bénéficie de l'appui permanent d'un Team Leader et de l'expérience collective de l'équipe HWC.",
  },
  {
    number: "03",
    icon: BarChart3,
    title: "Un pilotage par la performance",
    description:
      "Chaque mission est pilotée par des indicateurs de performance précis. Nous mesurons, analysons et optimisons en continu pour garantir des résultats concrets.",
  },
];

const values = [
  {
    icon: Target,
    title: "Modèle intégré",
    description:
      "Contrairement aux freelances ou plateformes, HWC pilote, forme et accompagne chaque collaborateur dans la durée.",
  },
  {
    icon: ShieldCheck,
    title: "Qualité garantie",
    description:
      "Processus de recrutement exigeant, formation continue et supervision par des experts métier.",
  },
  {
    icon: Users,
    title: "Proximité culturelle",
    description:
      "Collaborateurs francophones, formés aux standards européens, travaillant sur vos fuseaux horaires.",
  },
  {
    icon: Rocket,
    title: "Amélioration continue",
    description:
      "Notre modèle repose sur des cycles d'amélioration réguliers, pas de simples mises à disposition de ressources.",
  },
];

const process = [
  {
    step: 1,
    title: "Analyse des besoins",
    description:
      "Échange approfondi pour comprendre votre organisation, vos objectifs et définir le profil idéal.",
  },
  {
    step: 2,
    title: "Recrutement & formation",
    description:
      "Sélection rigoureuse du collaborateur, formation aux outils et processus spécifiques de votre entreprise.",
  },
  {
    step: 3,
    title: "Intégration & lancement",
    description:
      "Onboarding structuré, mise en place des outils de collaboration et définition des KPI de suivi.",
  },
  {
    step: 4,
    title: "Pilotage & optimisation",
    description:
      "Suivi continu, reportings réguliers, ajustements et amélioration continue de la performance.",
  },
];

const services = [
  {
    icon: Rocket,
    title: "Marketing Digital",
    description:
      "Développez votre visibilité en ligne et générez des leads qualifiés grâce à une équipe dédiée.",
    href: "/offres/regie-outsourcing/marketing-digital",
    features: [
      "Réseaux sociaux",
      "Création de contenu",
      "SEO & SEA",
      "Marketing automation",
    ],
  },
  {
    icon: Target,
    title: "Prospection Commerciale",
    description:
      "Accélérez votre développement commercial avec une équipe structurée qui identifie, qualifie et convertit vos prospects.",
    href: "/offres/regie-outsourcing/prospection-commerciale",
    features: [
      "Identification prospects",
      "Qualification leads",
      "Prise de RDV B2B",
      "Suivi CRM",
    ],
  },
  {
    icon: Headphones,
    title: "Support Administratif",
    description:
      "Fluidifiez vos opérations quotidiennes avec un support rigoureux et intégré à vos processus.",
    href: "/offres/regie-outsourcing/support-client",
    features: [
      "Gestion devis & commandes",
      "Facturation & relances",
      "CRM & bases clients",
      "Support équipes commerciales",
    ],
  },
];

const faqs = [
  {
    question:
      "Quelle est la différence entre la régie HWC et un freelance ou une agence classique ?",
    answer:
      "Contrairement à un freelance isolé ou une agence qui gère vos projets à distance, HWC met à disposition un collaborateur dédié, intégré à vos équipes, supervisé par un Team Leader et bénéficiant de l'intelligence collective HWC.",
  },
  {
    question: "Comment sont recrutés et formés les collaborateurs HWC ?",
    answer:
      "Nos collaborateurs passent par un processus de sélection rigoureux incluant tests techniques, entretiens comportementaux et vérification de références, puis suivent une formation initiale sur vos outils et processus.",
  },
  {
    question: "Comment fonctionne le pilotage des missions ?",
    answer:
      "Chaque mission est pilotée selon le modèle HWC Method : KPI définis dès le démarrage, points hebdomadaires, ajustement des priorités et reporting mensuel détaillé.",
  },
  {
    question: "Quel est le délai de mise en place d'une mission ?",
    answer:
      "En moyenne, une mission démarre entre 2 et 4 semaines après validation du besoin. Ce délai inclut le recrutement, la formation initiale et l'onboarding complet.",
  },
];

export default function RegieOutsourcing() {
  return (
    <>
      <Hero />
      <ValueSection />
      <MethodSection />
      <DedicatedTeams />
      <Differentiators />
      <ProcessSection />
      <ServicesSection />
      <FaqSection />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative flex min-h-[72vh] items-center overflow-hidden bg-muted/30 pb-16 pt-24">
      <div className="section-container relative z-10">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
            <Users className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">
              Business Unit
            </span>
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            Régie & <span className="text-gradient">Outsourcing</span>
          </h1>
          <p className="mx-auto mb-4 max-w-3xl text-xl text-muted-foreground">
            Des équipes opérationnelles qualifiées, structurées et pilotées par
            HWC Indian Ocean pour renforcer vos capacités sans recruter
            localement.
          </p>
          <p className="mx-auto mb-8 max-w-2xl text-base text-muted-foreground">
            Marketing digital · Prospection commerciale · Support administratif
            — trois pôles stratégiques pour accélérer votre croissance depuis
            l'île Maurice.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <a
              className="btn btn-hero h-14 px-8 text-base"
              href="https://calendly.com"
              target="_blank"
              rel="noreferrer"
            >
              Planifier un échange stratégique
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <a
              className="btn btn-outline-hero h-14 px-8 text-base"
              href="#hwc-method"
            >
              Découvrir notre méthode
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

function Section({ children, className = "", id }) {
  return (
    <section id={id} className={`py-16 md:py-24 ${className}`}>
      <div className="section-container">{children}</div>
    </section>
  );
}

function ValueSection() {
  return (
    <Section>
      <SectionHeading
        title="Pourquoi externaliser avec HWC ?"
        subtitle="Une proposition de valeur claire, mesurable et différenciante pour les entreprises européennes"
      />
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {benefits.map((item) => {
          const Icon = item.icon;
          return (
            <div
              className="card-elevated p-6 text-center transition-all hover:border-secondary/50"
              key={item.title}
            >
              <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-secondary">
                <Icon className="h-7 w-7 text-primary-foreground" />
              </div>
              <h3 className="mb-2 text-lg font-semibold">{item.title}</h3>
              <p className="text-sm text-muted-foreground">
                {item.description}
              </p>
            </div>
          );
        })}
      </div>
    </Section>
  );
}

function MethodSection() {
  return (
    <Section id="hwc-method" className="bg-muted/30">
      <SectionHeading
        title="Le modèle HWC Method"
        subtitle="Bien plus qu'une mise à disposition : un modèle de performance intégré"
      />
      <p className="mx-auto mb-12 max-w-3xl text-center text-muted-foreground">
        Contrairement aux modèles d'outsourcing classiques, HWC ne met pas
        simplement une ressource à disposition. Chaque mission repose sur un
        triptyque unique qui garantit une performance durable.
      </p>
      <div className="grid gap-8 md:grid-cols-3">
        {pillars.map((item) => {
          const Icon = item.icon;
          return (
            <div
              className="card-elevated group relative p-8 transition-all hover:border-secondary/50"
              key={item.number}
            >
              <div className="absolute -left-4 -top-4 flex h-12 w-12 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary text-sm font-bold text-primary-foreground">
                {item.number}
              </div>
              <div className="mb-4 mt-4 flex h-12 w-12 items-center justify-center rounded-xl bg-secondary/10">
                <Icon className="h-6 w-6 text-secondary" />
              </div>
              <h3 className="mb-3 text-xl font-semibold">{item.title}</h3>
              <p className="text-sm text-muted-foreground">
                {item.description}
              </p>
            </div>
          );
        })}
      </div>
    </Section>
  );
}

function DedicatedTeams() {
  return (
    <Section>
      <div className="grid items-center gap-12 lg:grid-cols-2">
        <div>
          <h2 className="mb-4 font-display text-3xl font-bold">
            Des équipes dédiées{" "}
            <span className="text-gradient">à distance</span>
          </h2>
          <p className="mb-4 text-muted-foreground">
            Nous mettons à disposition des entreprises européennes des
            collaborateurs qualifiés basés à l'île Maurice, travaillant en
            collaboration directe avec vos équipes sous pilotage opérationnel
            HWC.
          </p>
          <p className="mb-6 text-muted-foreground">
            Ces collaborateurs sont francophones, formés aux standards européens
            et opèrent sur vos fuseaux horaires pour une intégration fluide et
            transparente.
          </p>
        </div>
        <div className="card-elevated p-8">
          <div className="mb-6 rounded-xl bg-gradient-to-br from-primary/10 to-secondary/10 p-8 text-center">
            <p className="mb-2 text-5xl font-bold text-secondary">3</p>
            <p className="mb-1 text-lg font-semibold">Pôles de services</p>
            <p className="text-sm text-muted-foreground">
              Marketing · Prospection · Support
            </p>
          </div>
          <div className="grid grid-cols-2 gap-4 text-center">
            <Stat value="40-60%" label="Économies réalisées" />
            <Stat value="2-4 sem." label="Délai de démarrage" />
          </div>
        </div>
      </div>
    </Section>
  );
}

function Stat({ value, label }) {
  return (
    <div className="rounded-lg bg-muted/50 p-4">
      <p className="text-2xl font-bold text-secondary">{value}</p>
      <p className="text-xs text-muted-foreground">{label}</p>
    </div>
  );
}

function Differentiators() {
  return (
    <Section className="bg-muted/30">
      <SectionHeading
        title="Ce qui nous différencie"
        subtitle="Un modèle d'outsourcing premium pensé pour la performance"
      />
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {values.map((item) => {
          const Icon = item.icon;
          return (
            <div
              className="card-elevated p-6 transition-all hover:border-secondary/50"
              key={item.title}
            >
              <Icon className="mb-4 h-8 w-8 text-secondary" />
              <h3 className="mb-2 font-semibold">{item.title}</h3>
              <p className="text-sm text-muted-foreground">
                {item.description}
              </p>
            </div>
          );
        })}
      </div>
    </Section>
  );
}

function ProcessSection() {
  return (
    <Section>
      <SectionHeading
        title="Notre processus"
        subtitle="De l'analyse de vos besoins au pilotage opérationnel : une méthodologie éprouvée"
      />
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {process.map((item) => (
          <div className="card-elevated relative p-6" key={item.step}>
            <div className="absolute -left-4 -top-4 flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary font-bold text-primary-foreground">
              {item.step}
            </div>
            <h4 className="mb-2 mt-4 font-semibold text-foreground">
              {item.title}
            </h4>
            <p className="text-sm text-muted-foreground">{item.description}</p>
          </div>
        ))}
      </div>
    </Section>
  );
}

function ServicesSection() {
  return (
    <Section id="services-regie" className="bg-muted/30">
      <SectionHeading
        title="Nos services en régie"
        subtitle="Trois pôles opérationnels structurés pour couvrir vos besoins stratégiques"
      />
      <div className="grid gap-8 md:grid-cols-3">
        {services.map((item) => {
          const Icon = item.icon;
          return (
            <Link
              className="group card-elevated p-8 transition-all hover:border-secondary/50"
              to={item.href}
              key={item.href}
            >
              <div className="mb-6 flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-primary to-secondary transition-transform group-hover:scale-110">
                <Icon className="h-8 w-8 text-primary-foreground" />
              </div>
              <h3 className="mb-3 text-2xl font-semibold transition-colors group-hover:text-secondary">
                {item.title}
              </h3>
              <p className="mb-6 text-muted-foreground">{item.description}</p>
              <div className="mb-6 flex flex-wrap gap-2">
                {item.features.map((feature) => (
                  <span
                    className="rounded-full bg-muted px-3 py-1 text-xs text-muted-foreground"
                    key={feature}
                  >
                    {feature}
                  </span>
                ))}
              </div>
              <div className="flex items-center font-medium text-secondary">
                Découvrir ce service
                <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-2" />
              </div>
            </Link>
          );
        })}
      </div>
    </Section>
  );
}

function FaqSection() {
  const [open, setOpen] = useState(0);

  return (
    <Section>
      <SectionHeading
        title="Questions fréquentes"
        subtitle="Tout ce que vous devez savoir sur notre offre Régie & Outsourcing"
      />
      <div className="mx-auto max-w-3xl space-y-4">
        {faqs.map((faq, index) => {
          const active = open === index;
          return (
            <div className="card-elevated rounded-xl px-6" key={faq.question}>
              <button
                type="button"
                onClick={() => setOpen(active ? -1 : index)}
                className="flex w-full items-center justify-between gap-4 py-6 text-left"
              >
                <span className="font-medium text-foreground">
                  {faq.question}
                </span>
                <ChevronDown
                  className={`h-4 w-4 shrink-0 transition-transform ${active ? "rotate-180" : ""}`}
                />
              </button>
              <div
                className={`grid transition-all duration-300 ease-out ${
                  active
                    ? "grid-rows-[1fr] opacity-100"
                    : "grid-rows-[0fr] opacity-0"
                }`}
              >
                <div className="overflow-hidden">
                  <p className="pb-6 text-sm leading-relaxed text-muted-foreground">
                    {faq.answer}
                  </p>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </Section>
  );
}

function FinalCta() {
  return (
    <Section className="bg-muted/30">
      <div className="card-elevated rounded-3xl bg-gradient-to-br from-primary/5 via-background to-secondary/5 p-8 text-center md:p-12">
        <h2 className="mb-4 font-display text-2xl font-bold md:text-3xl">
          Prêt à renforcer <span className="text-gradient">vos équipes</span> ?
        </h2>
        <p className="mx-auto mb-8 max-w-2xl text-muted-foreground">
          Planifiez un échange stratégique avec notre équipe pour définir vos
          besoins et découvrir comment notre modèle de régie peut accélérer
          votre développement.
        </p>
        <div className="flex flex-wrap justify-center gap-4">
          <a
            className="btn btn-hero h-14 px-8 text-base"
            href="https://calendly.com"
            target="_blank"
            rel="noreferrer"
          >
            Planifier un échange gratuit
            <ArrowRight className="ml-2 h-5 w-5" />
          </a>
          <Link
            className="btn btn-outline-hero h-14 px-8 text-base"
            to="/outils"
          >
            Nos ressources gratuites
          </Link>
        </div>
      </div>
    </Section>
  );
}
