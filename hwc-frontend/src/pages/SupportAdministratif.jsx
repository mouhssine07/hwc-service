import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Support Client",
  title: "Support Administratif",
  subtitle:
    "Fluidifiez vos opérations quotidiennes avec un support administratif et commercial performant, rigoureux et intégré à vos processus.",
  heroImage: "https://images.unsplash.com/photo-1520607162513-77705c0f0d4a?w=1600",
  introTitle: "Support Administratif",
  introText:
    "Nous prenons en charge les tâches administratives et commerciales répétitives pour libérer du temps à vos équipes et fiabiliser vos opérations.",
  bullets: [
    "Gestion des devis et commandes",
    "Facturation et relances",
    "Suivi des livraisons et coordination",
    "Gestion des bases clients et mise à jour CRM",
    "Support aux équipes commerciales",
  ],
  highlights: [
    { title: "Gain de temps", description: "Vos équipes se concentrent sur la vente et la relation client." },
    { title: "Fiabilité opérationnelle", description: "Des processus rigoureux et un contrôle qualité permanent." },
    { title: "Scalabilité", description: "Des ressources ajustables selon vos pics d'activité." },
    { title: "Intégration fluide", description: "Votre assistant s'intègre à vos outils et à vos procédures existantes." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "De l'audit de vos processus à l'intégration opérationnelle",
  processSteps: [
    { step: 1, title: "Audit des processus", description: "Analyse de vos flux administratifs, identification des goulots et des optimisations." },
    { step: 2, title: "Recrutement & formation", description: "Sélection d'un assistant qualifié, formation à vos outils et culture d'entreprise." },
    { step: 3, title: "Intégration opérationnelle", description: "Mise en place des accès, documentation des procédures et montée en charge progressive." },
    { step: 4, title: "Pilotage & amélioration", description: "Points réguliers, KPI de suivi, feedback continu et optimisation des processus." },
  ],
  faqs: [
    {
      question: "Quelles tâches administratives pouvez-vous prendre en charge ?",
      answer:
        "Nous couvrons l'ensemble des fonctions administratives et commerciales : gestion des devis, commandes, facturation, suivi des livraisons, mise à jour CRM, gestion des bases clients, support aux équipes commerciales, coordination inter-services et tâches récurrentes.",
    },
    {
      question: "Comment garantissez-vous la confidentialité de nos données ?",
      answer:
        "Tous nos collaborateurs signent des accords de confidentialité et nos processus respectent les standards RGPD. Les accès aux outils et données sont strictement contrôlés et limités au périmètre de la mission.",
    },
    {
      question: "Comment l'assistant s'intègre-t-il à nos outils existants ?",
      answer:
        "Votre assistant HWC est formé à utiliser vos outils : CRM, ERP, logiciels de facturation, Suite Google ou Microsoft et outils de gestion de projet.",
    },
    {
      question: "Comment gérez-vous les pics d'activité ?",
      answer:
        "Notre modèle flexible permet d'ajuster rapidement les ressources. En cas de pic d'activité, nous pouvons mobiliser des renforts formés à vos processus pour absorber la charge supplémentaire.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function SupportAdministratif() {
  return <ServiceDetailPage {...data} />;
}
