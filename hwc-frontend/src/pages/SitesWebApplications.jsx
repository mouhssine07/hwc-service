import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Web & Apps",
  title: "Sites web & Applications",
  subtitle:
    "Concevez des expériences digitales performantes, intuitives et orientées conversion pour impressionner vos visiteurs.",
  heroImage: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1600",
  introTitle: "Sites web & Applications",
  introText:
    "Nous concevons des interfaces utiles et élégantes qui servent votre image, vos besoins métiers et vos objectifs de conversion.",
  bullets: [
    "Cadrage du besoin et cahier des charges détaillé",
    "Conception UX/UI et parcours utilisateurs",
    "Développement web sur-mesure",
    "Tests, optimisation et lancement",
  ],
  highlights: [
    { title: "Expérience utilisateur", description: "Des interfaces claires et fluides pour améliorer l'engagement." },
    { title: "Conversion", description: "Des parcours conçus pour transformer les visites en demandes concrètes." },
    { title: "Sur-mesure", description: "Des solutions adaptées à vos besoins, pas des gabarits génériques." },
    { title: "Maintenabilité", description: "Des livrables pensés pour durer et évoluer." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une livraison structurée, du cadrage à la mise en ligne",
  processSteps: [
    { step: 1, title: "Cadrage", description: "Élaboration du cahier des charges détaillé et définition des fonctionnalités essentielles." },
    { step: 2, title: "Design UX/UI", description: "Conception de maquettes ergonomiques et interfaces attractives." },
    { step: 3, title: "Développement web", description: "Intégration technique, développement et tests continus." },
    { step: 4, title: "Lancement", description: "Mise en ligne optimisée, ajustements finaux et support post-lancement." },
  ],
  faqs: [
    {
      question: "Quelles technologies utilisez-vous pour développer les sites web et applications ?",
      answer:
        "Nous utilisons les technologies web les plus adaptées à vos besoins : React et Next.js pour les applications modernes, WordPress pour les sites institutionnels, Shopify et WooCommerce pour l'e-commerce, ainsi que des solutions sur-mesure selon vos objectifs.",
    },
    {
      question: "Combien coûte la création d'un site web ou d'une application ?",
      answer:
        "Le tarif varie selon la complexité, les fonctionnalités et les technologies utilisées. Chaque projet étant unique, nous établissons un devis personnalisé après analyse de vos besoins spécifiques.",
    },
    {
      question: "Quel est le délai de réalisation d'un site web ?",
      answer:
        "Le délai de développement dépend de la complexité du projet et des fonctionnalités souhaitées. Nous définissons ensemble un planning personnalisé lors de la phase de cadrage pour respecter vos contraintes.",
    },
    {
      question: "Proposez-vous la maintenance de sites web ?",
      answer:
        "Oui, nous proposons des contrats de maintenance incluant mises à jour de sécurité, sauvegardes régulières, monitoring des performances et support technique pour garantir la pérennité de votre site.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function SitesWebApplications() {
  return <ServiceDetailPage {...data} />;
}
