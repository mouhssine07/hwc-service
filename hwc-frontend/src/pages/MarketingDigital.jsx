import { ArrowRight, BarChart3, Bot, Globe, Megaphone, Search, Share2, Target } from "lucide-react";
import { Link } from "react-router-dom";
import SectionHeading from "../components/SectionHeading.jsx";

const serviceCards = [
  {
    icon: Target,
    title: "Audit et stratégie",
    description: "Audit 360° de votre présence digitale et construction d'une feuille de route d'acquisition alignée avec vos objectifs de croissance.",
    href: "/offres/regie-outsourcing/marketing-digital/audit-strategie",
  },
  {
    icon: Megaphone,
    title: "Acquisition & Lead Generation",
    description: "Génération des leads qualifiés grâce à des campagnes performantes et ciblées.",
    href: "/offres/regie-outsourcing/marketing-digital/acquisition-lead-generation",
  },
  {
    icon: Share2,
    title: "Réseaux Sociaux & Contenu",
    description: "Création d'une présence engageante avec du contenu qui résonne avec votre audience.",
    href: "/offres/regie-outsourcing/marketing-digital/contenu-reseaux-sociaux",
  },
  {
    icon: Globe,
    title: "Web et applications",
    description: "Conception des sites web et applications sur-mesure. Transformez votre vision en réalité tangible.",
    href: "/offres/regie-outsourcing/marketing-digital/sites-web-applications",
  },
  {
    icon: Search,
    title: "Référencement SEO & SEA",
    description: "Amélioration de la visibilité organique, attraction d'un trafic qualifié et gestion des campagnes payantes.",
    href: "/offres/regie-outsourcing/marketing-digital/referencement-seo-sea",
  },
  {
    icon: Bot,
    title: "IA et Automatisation",
    description: "Optimisation des processus et gain de temps avec l'intelligence artificielle et l'automatisation.",
    href: "/offres/regie-outsourcing/marketing-digital/ia-automatisations",
  },
];

const process = [
  {
    step: 1,
    title: "Audit & stratégie",
    description: "Analyse de votre positionnement digital, audit de vos canaux existants et définition d'une stratégie sur-mesure.",
  },
  {
    step: 2,
    title: "Mise en place",
    description: "Configuration des outils, création des comptes, setup technique et calendrier éditorial.",
  },
  {
    step: 3,
    title: "Production & diffusion",
    description: "Création de contenus, gestion des campagnes, community management et optimisation continue.",
  },
  {
    step: 4,
    title: "Reporting & optimisation",
    description: "Rapports de performance détaillés, analyse des KPI et ajustements stratégiques pour maximiser les résultats.",
  },
];

export default function MarketingDigital() {
  return (
    <>
      <Hero />
      <ImageBand />
      <ServicesGrid />
      <ProcessSection />
      <FinalCta />
    </>
  );
}

function Hero() {
  return (
    <section className="relative flex min-h-[72vh] items-center overflow-hidden bg-muted/30 pb-16 pt-24">
      <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-secondary/5" />
      <div
        className="absolute inset-0 bg-cover bg-center opacity-20"
        style={{
          backgroundImage:
            "url(https://images.unsplash.com/photo-1557838923-2985c318be48?w=1600)",
        }}
      />
      <div className="section-container relative z-10">
        <div className="mx-auto max-w-4xl text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
            <Megaphone className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">Régie & Outsourcing</span>
          </div>
          <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
            Marketing <span className="text-gradient">Digital</span>
          </h1>
          <p className="mx-auto mb-4 max-w-3xl text-xl text-muted-foreground">
            Développez votre visibilité en ligne et générez des leads qualifiés grâce à une équipe marketing dédiée,
            formée et pilotée par HWC.
          </p>
          <p className="mx-auto mb-8 max-w-2xl text-base text-muted-foreground">
            Réseaux sociaux · SEO · Content · Automation · Campagnes publicitaires
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
              Profitez de votre audit offert
              <ArrowRight className="ml-2 h-5 w-5" />
            </a>
            <a className="btn btn-outline-hero h-14 px-8 text-base" href="#services">
              Découvrez nos services
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

function ImageBand() {
  const images = [
    "https://images.unsplash.com/photo-1497366754035-f200968a6e72?w=1200",
    "https://images.unsplash.com/photo-1552664730-d307ca884978?w=1200",
    "https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=1200",
  ];

  return (
    <section className="py-8">
      <div className="section-container">
        <div className="overflow-x-auto">
          <div className="grid min-w-[900px] grid-cols-3 gap-4">
            {images.map((src) => (
              <img
                key={src}
                src={src}
                alt=""
                className="aspect-[4/3] rounded-2xl object-cover shadow-card"
              />
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

function ServicesGrid() {
  return (
    <section id="services" className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Nos services en marketing digital"
          subtitle="Une couverture complète de vos besoins pour structurer, activer et optimiser votre croissance"
        />
        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          {serviceCards.map((item) => {
            const Icon = item.icon;
            return (
              <Link key={item.title} to={item.href} className="group card-elevated p-6 transition-all hover:border-secondary/50">
                <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-secondary transition-transform group-hover:scale-110">
                  <Icon className="h-6 w-6 text-primary-foreground" />
                </div>
                <h3 className="mb-2 text-xl font-semibold transition-colors group-hover:text-secondary">{item.title}</h3>
                <p className="mb-6 text-sm text-muted-foreground">{item.description}</p>
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

function ProcessSection() {
  return (
    <section className="bg-muted/30 py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Notre processus" subtitle="Une méthodologie structurée pour des résultats mesurables" />
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
          {process.map((item) => (
            <div className="card-elevated relative p-6" key={item.step}>
              <div className="absolute -left-4 -top-4 flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary font-bold text-primary-foreground">
                {item.step}
              </div>
              <h4 className="mb-2 mt-4 font-semibold text-foreground">{item.title}</h4>
              <p className="text-sm text-muted-foreground">{item.description}</p>
            </div>
          ))}
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
          <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl">Parlons de votre projet</h2>
          <p className="mx-auto mb-8 max-w-2xl text-muted-foreground">
            Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.
          </p>
          <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
            Profitez de votre audit offert
            <ArrowRight className="ml-2 h-5 w-5" />
          </a>
        </div>
      </div>
    </section>
  );
}
