import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Lead Generation",
  title: "Acquisition & Lead Generation",
  subtitle:
    "Générez un flux régulier de prospects qualifiés avec des campagnes ciblées, des messages percutants et des tunnels de conversion performants.",
  heroImage: "https://images.unsplash.com/photo-1556761175-b413da4baf72?w=1600",
  introTitle: "Acquisition & Lead Generation",
  introText:
    "Nous concevons des dispositifs d'acquisition qui combinent ciblage, création, diffusion et optimisation pour maximiser votre retour sur investissement.",
  bullets: [
    "Ciblage précis de vos audiences et définition de personas",
    "Création de campagnes impactantes et de landing pages optimisées",
    "Lancement stratégique et tests progressifs",
    "Analyse approfondie des données et amélioration continue",
  ],
  highlights: [
    { title: "Campagnes performantes", description: "Google Ads, Meta Ads et LinkedIn Ads calibrées selon votre marché." },
    { title: "Lead nurturing", description: "Automatisation et séquences de suivi pour accompagner vos prospects." },
    { title: "Conversion optimisée", description: "Landing pages et messages alignés avec votre proposition de valeur." },
    { title: "Pilotage par la donnée", description: "Suivi des coûts, des volumes et des taux de conversion." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une mécanique d'acquisition structurée et mesurable",
  processSteps: [
    { step: 1, title: "Ciblage", description: "Identification précise de vos audiences et définition détaillée de vos personas." },
    { step: 2, title: "Création", description: "Conception de campagnes impactantes, de créatifs performants et de landing pages optimisées pour la conversion." },
    { step: 3, title: "Lancement", description: "Déploiement stratégique et tests progressifs pour valider les meilleures approches." },
    { step: 4, title: "Optimisation", description: "Analyse approfondie des données et amélioration continue des performances." },
  ],
  faqs: [
    {
      question: "Quel budget publicitaire faut-il prévoir pour une campagne de génération de leads efficace ?",
      answer:
        "Pour une stratégie de lead generation performante, nous recommandons un budget publicitaire mensuel permettant d'obtenir un volume de données suffisant pour optimiser vos campagnes Google Ads, Meta Ads ou LinkedIn Ads.",
    },
    {
      question: "Combien de temps avant de voir les premiers résultats en génération de leads ?",
      answer:
        "Les premières conversions et leads qualifiés arrivent généralement dès les premières semaines de votre campagne publicitaire. L'optimisation complète de votre tunnel de conversion et l'amélioration du taux de transformation prennent environ 2-3 mois pour maximiser votre retour sur investissement publicitaire (ROAS).",
    },
    {
      question: "Proposez-vous un accompagnement après la génération de leads ?",
      answer:
        "Oui, nous mettons en place des stratégies de lead nurturing automatisées via email marketing et marketing automation pour accompagner vos prospects tout au long du parcours client jusqu'à la conversion finale, en collaboration avec votre équipe commerciale.",
    },
    {
      question: "Sur quelles plateformes publicitaires intervenez-vous pour la génération de leads ?",
      answer:
        "Nous utilisons deux méthodes principales : publicité classique avec Google Ads, Meta Ads et LinkedIn Ads, combinées avec des stratégies d'inbound marketing, et Lead Booster, notre plateforme propriétaire propulsée par l'IA qui génère et qualifie automatiquement vos leads.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function AcquisitionLeadGeneration() {
  return <ServiceDetailPage {...data} />;
}
