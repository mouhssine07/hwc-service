import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "Contenu & Social",
  title: "Contenu et Réseaux Sociaux",
  subtitle:
    "Renforcez votre notoriété et votre présence digitale avec du contenu engageant qui booste votre visibilité et résonne avec votre audience.",
  heroImage: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1600",
  introTitle: "Contenu et Réseaux Sociaux",
  introText:
    "Nous concevons des prises de parole cohérentes et engageantes pour construire une marque plus visible, plus claire et plus crédible sur vos canaux clés.",
  bullets: [
    "Stratégie éditoriale personnalisée",
    "Création de contenus visuels et textuels",
    "Design graphique et supports de communication",
    "Gestion des réseaux sociaux et Community management",
    "Campagnes d'influence et partenariats",
    "Reporting et Analytics détaillés",
  ],
  highlights: [
    { title: "Présence de marque cohérente", description: "Des contenus alignés sur votre identité et vos objectifs." },
    { title: "Engagement de l'audience", description: "Une ligne éditoriale pensée pour générer interactions et confiance." },
    { title: "Production organisée", description: "Un calendrier et des formats adaptés à votre rythme de publication." },
    { title: "Mesure continue", description: "Reporting et analyse pour améliorer la performance de vos contenus." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "Une séquence claire pour piloter la présence sociale et le contenu",
  processSteps: [
    { step: 1, title: "Analyse du contenu", description: "Évaluation approfondie de votre présence digitale actuelle et de votre audience." },
    { step: 2, title: "Stratégie éditoriale", description: "Définition de votre ligne éditoriale et d'un calendrier de publication optimisé." },
    { step: 3, title: "Production", description: "Création de contenus visuels et rédactionnels impactants avec validation collaborative." },
    { step: 4, title: "Animation", description: "Publication régulière, modération active et stimulation de l'engagement." },
  ],
  faqs: [
    {
      question: "Quels formats de contenu pouvez-vous produire ?",
      answer:
        "Notre équipe prend en charge les textes, visuels, infographies, vidéos courtes, carrousels, supports de campagne et déclinaisons pour les réseaux sociaux.",
    },
    {
      question: "Travaillez-vous avec notre équipe interne ?",
      answer:
        "Oui. Nous structurons le process avec vos équipes pour garder une cohérence de ton, valider les contenus et intégrer vos retours métier à chaque étape.",
    },
    {
      question: "Comment suivez-vous la performance ?",
      answer:
        "Nous suivons la portée, l'engagement, les clics, les leads générés et les signaux qualitatifs pour ajuster la stratégie éditoriale.",
    },
    {
      question: "Faites-vous aussi la gestion des réseaux sociaux ?",
      answer:
        "Oui, nous pouvons prendre en charge la publication, la programmation, la modération et le community management selon votre besoin.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function ContenuReseauxSociaux() {
  return <ServiceDetailPage {...data} />;
}
