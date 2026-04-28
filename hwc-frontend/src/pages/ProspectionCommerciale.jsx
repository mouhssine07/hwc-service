import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Lead Generation",
  title: "Acquisition et Lead Generation",
  subtitle:
    "Générez un flux régulier de prospects qualifiés avec des campagnes ciblées, des messages percutants et des tunnels de conversion performants.",
  heroImage: "https://images.unsplash.com/photo-1556761175-b413da4baf72?w=1600",
  introTitle: "Acquisition et Lead Generation",
  introText:
    "Nous concevons des dispositifs d'acquisition qui combinent ciblage, création, diffusion et optimisation pour maximiser votre retour sur investissement.",
  bullets: [
    "Ciblage précis de vos audiences et définition détaillée de vos personas",
    "Création de campagnes impactantes, de créatifs performants et de landing pages optimisées pour la conversion",
    "Déploiement stratégique et tests progressifs pour valider les meilleures approches",
    "Analyse approfondie des données et amélioration continue des performances",
  ],
  highlights: [
    {
      title: "Publicités classiques",
      description:
        "Google Ads, Meta Ads, LinkedIn Ads et stratégies d'inbound marketing pour couvrir vos canaux.",
    },
    {
      title: "Lead Booster",
      description:
        "Plateforme propriétaire propulsée par l'IA qui génère et qualifie automatiquement vos leads.",
    },
    {
      title: "Lead nurturing",
      description:
        "Séquences automatisées pour accompagner vos prospects jusqu'à la conversion finale.",
    },
    {
      title: "Pilotage par la donnée",
      description:
        "Suivi des coûts, des volumes, des conversions et de la performance des campagnes.",
    },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une mécanique d'acquisition structurée et mesurable",
  processSteps: [
    {
      step: 1,
      title: "Ciblage",
      description:
        "Identification précise de vos audiences et définition détaillée de vos personas.",
    },
    {
      step: 2,
      title: "Création",
      description:
        "Conception de campagnes impactantes, de créatifs performants et de landing pages optimisées pour la conversion.",
    },
    {
      step: 3,
      title: "Lancement",
      description:
        "Déploiement stratégique et tests progressifs pour valider les meilleures approches.",
    },
    {
      step: 4,
      title: "Optimisation",
      description:
        "Analyse approfondie des données et amélioration continue des performances.",
    },
  ],
  faqs: [
    {
      question:
        "Quel budget publicitaire faut-il prévoir pour une campagne de génération de leads efficace ?",
      answer:
        "Pour une stratégie de lead generation performante, nous recommandons un budget publicitaire mensuel permettant d'obtenir un volume de données suffisant pour optimiser vos campagnes Google Ads, Meta Ads ou LinkedIn Ads.",
    },
    {
      question:
        "Combien de temps avant de voir les premiers résultats en génération de leads ?",
      answer:
        "Les premières conversions et leads qualifiés arrivent généralement dès les premières semaines de votre campagne publicitaire. L'optimisation complète du tunnel et l'amélioration du taux de transformation prennent environ 2 à 3 mois.",
    },
    {
      question:
        "Proposez-vous un accompagnement après la génération de leads ?",
      answer:
        "Oui, nous mettons en place des stratégies de lead nurturing automatisées via email marketing et marketing automation pour accompagner vos prospects jusqu'à la conversion finale.",
    },
    {
      question: "Sur quelles plateformes publicitaires intervenez-vous ?",
      answer:
        "Nous travaillons avec Google Ads, Meta Ads, LinkedIn Ads et les approches d'inbound marketing. Nous pouvons également activer Lead Booster si le dispositif est adapté.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle:
    "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function ProspectionCommerciale() {
  return <ServiceDetailPage {...data} />;
}
