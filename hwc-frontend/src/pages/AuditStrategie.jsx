import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Audit gratuit",
  title: "Audit et Stratégie",
  subtitle:
    "Analysez en profondeur votre écosystème digital grâce à un audit 360° complet de votre présence en ligne. Identifiez vos forces, vos axes d'amélioration et les opportunités inexploitées.",
  heroImage: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=1600",
  introTitle: "Audit et Stratégie",
  introText:
    "Nous vous aidons à poser un diagnostic clair, structuré et actionnable sur votre présence digitale afin de construire une feuille de route adaptée à vos objectifs.",
  bullets: [
    "Analyse complète de votre présence digitale",
    "Étude de la concurrence et benchmarking",
    "Identification des opportunités de croissance",
    "Définition des KPIs et objectifs mesurables",
    "Élaboration d'un plan d'action stratégique",
  ],
  highlights: [
    {
      title: "Vision 360° de votre présence digitale",
      description: "Cartographie complète de vos canaux, performances et positionnement concurrentiel.",
    },
    {
      title: "Opportunités de croissance identifiées",
      description: "Détection des leviers prioritaires pour booster vos résultats rapidement.",
    },
    {
      title: "Stratégie mesurable et objectifs clairs",
      description: "KPIs définis, feuille de route priorisée et résultats quantifiables.",
    },
    {
      title: "Plan d'action opérationnel",
      description: "Roadmap détaillée avec timelines, budgets et étapes concrètes de mise en œuvre.",
    },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une méthodologie éprouvée pour des résultats concrets",
  processSteps: [
    { step: 1, title: "Découverte", description: "Entretien approfondi pour comprendre vos objectifs et défis." },
    { step: 2, title: "Analyse", description: "Audit complet de vos canaux et de votre environnement concurrentiel." },
    { step: 3, title: "Stratégie", description: "Élaboration d'une stratégie sur-mesure avec priorisation des actions." },
    {
      step: 4,
      title: "Activation",
      description: "Un plan opérationnel clé en main et accompagnement personnalisé pour garantir une mise en œuvre réussie.",
    },
  ],
  faqs: [
    {
      question: "Combien de temps dure un audit stratégique ?",
      answer:
        "Un audit stratégique HWC prend généralement entre 2 et 4 semaines, selon la complexité de votre écosystème digital et le nombre de canaux à analyser. Notre équipe HWC s'adapte à vos contraintes de temps tout en garantissant une analyse approfondie et des recommandations actionnables pour votre croissance.",
    },
    {
      question: "Quels éléments sont analysés lors de l'audit ?",
      answer:
        "Nos experts réalisent une analyse complète de votre écosystème digital : site web, réseaux sociaux, SEO, campagnes publicitaires, email marketing, positionnement de marque, environnement concurrentiel et bien d'autres volets selon vos besoins spécifiques.",
    },
    {
      question: "Que contient le livrable final ?",
      answer:
        "HWC vous remet un rapport complet comprenant : l'analyse détaillée de chaque canal digital, un diagnostic de vos performances actuelles, les recommandations prioritaires hiérarchisées selon leur impact, un plan d'action opérationnel avec roadmap de mise en œuvre.",
    },
    {
      question: "Proposez-vous un accompagnement après l'audit ?",
      answer:
        "Oui, nous proposons un accompagnement pour vous aider à mettre en œuvre les recommandations de l'audit, que ce soit en gestion complète ou en coaching de vos équipes.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function AuditStrategie() {
  return <ServiceDetailPage {...data} />;
}
