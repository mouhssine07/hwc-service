import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "SEO & SEA",
  title: "Référencement SEO & SEA",
  subtitle:
    "Améliorez la visibilité organique, attirez un trafic qualifié et pilotez vos campagnes payantes avec une logique de performance.",
  heroImage: "https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?w=1600",
  introTitle: "Référencement SEO & SEA",
  introText:
    "Nous combinons le référencement naturel et les campagnes payantes pour maximiser votre présence sur les moteurs de recherche et accélérer vos résultats.",
  bullets: [
    "Audit et stratégie SEO/SEA",
    "Optimisation technique et éditoriale",
    "Gestion des campagnes Google Ads",
    "Suivi des performances et scaling",
  ],
  highlights: [
    { title: "Visibilité immédiate & durable", description: "Le SEA génère du trafic instantané pendant que le SEO construit votre présence à long terme." },
    { title: "Couverture maximale", description: "Occupez les résultats organiques et sponsorisés pour dominer les pages de recherche." },
    { title: "Synergie des données", description: "Les insights SEA alimentent votre stratégie SEO et vice versa pour une optimisation continue." },
    { title: "ROI optimisé", description: "Réduisez progressivement vos dépenses publicitaires à mesure que le SEO prend le relais." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une séquence d'optimisation du diagnostic au scaling",
  processSteps: [
    { step: 1, title: "Audit & stratégie", description: "Analyse du positionnement, de la concurrence et définition d'une stratégie cohérente." },
    { step: 2, title: "Optimisation SEO", description: "Corrections techniques, optimisation des contenus et développement de l'autorité du site." },
    { step: 3, title: "Lancement SEA", description: "Création et déploiement de campagnes Google Ads pour générer du trafic qualifié rapidement." },
    { step: 4, title: "Pilotage & scaling", description: "Suivi des performances, ajustement des budgets SEA et accélération de la croissance organique." },
  ],
  faqs: [
    {
      question: "Quel budget prévoir pour le SEA ?",
      answer:
        "Le budget dépend de votre secteur, de la concurrence sur vos mots-clés et de vos objectifs. Nous optimisons continuellement vos campagnes pour maximiser le retour sur investissement.",
    },
    {
      question: "Combien de temps faut-il pour voir des résultats SEO ?",
      answer:
        "Le SEO demande généralement plusieurs semaines à plusieurs mois selon la concurrence et l'état de départ du site. Le SEA permet, lui, d'obtenir des résultats plus rapides.",
    },
    {
      question: "Pouvez-vous gérer SEO et SEA ensemble ?",
      answer:
        "Oui. Le pilotage conjoint SEO/SEA permet d'aligner les données, d'optimiser les budgets et de renforcer votre présence sur les moteurs de recherche.",
    },
    {
      question: "Travaillez-vous sur les contenus SEO ?",
      answer:
        "Oui, nous optimisons ou produisons les contenus nécessaires pour soutenir la performance organique et la conversion.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function SeoSea() {
  return <ServiceDetailPage {...data} />;
}
