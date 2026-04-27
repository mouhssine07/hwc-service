import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

const data = {
  eyebrow: "IA & Automation",
  title: "IA et Automatisations",
  subtitle:
    "Optimisez vos processus et gagnez du temps avec l'intelligence artificielle et l'automatisation.",
  heroImage: "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=1600",
  introTitle: "IA et Automatisations",
  introText:
    "Nous intégrons des outils intelligents pour fluidifier votre production, accélérer vos opérations et fiabiliser vos flux de travail.",
  bullets: [
    "Automatisation des tâches répétitives",
    "Optimisation des parcours et workflows",
    "Création de prompts et d'assistants IA",
    "Intégration des outils via API",
  ],
  highlights: [
    { title: "Gain de temps", description: "Réduction des tâches manuelles pour vous concentrer sur la valeur." },
    { title: "Processus fiabilisés", description: "Moins d'erreurs et plus de cohérence dans les opérations." },
    { title: "Intégration métier", description: "Des solutions connectées à votre environnement existant." },
    { title: "Évolutivité", description: "Des automatisations pensées pour grandir avec votre activité." },
  ],
  processTitle: "Notre processus",
  processSubtitle: "De l'identification du besoin au déploiement opérationnel",
  processSteps: [
    { step: 1, title: "Cartographie", description: "Identification des tâches et processus à forte valeur d'automatisation." },
    { step: 2, title: "Conception", description: "Choix des outils, scénarios et logiques d'automatisation adaptés." },
    { step: 3, title: "Implémentation", description: "Mise en place des automatisations et intégration aux outils existants à travers des API." },
    { step: 4, title: "Optimisation", description: "Suivi des performances et amélioration continue des scénarios." },
  ],
  faqs: [
    {
      question: "Comment intégrer l'IA générative dans nos processus métier ?",
      answer:
        "L'intégration se fait principalement via les API des fournisseurs (OpenAI, Anthropic, Google) que vous connectez à vos systèmes existants. Cela permet d'automatiser des tâches comme la création de contenu, le support client, l'analyse de documents, la génération de code ou l'enrichissement de données.",
    },
    {
      question: "Quels types de tâches peut-on automatiser ?",
      answer:
        "Les tâches répétitives, les traitements de données, la génération de contenus, les workflows de validation et certains échanges avec vos outils métiers sont de bons candidats.",
    },
    {
      question: "Les contenus ou réponses générés par l'IA sont-ils validés ?",
      answer:
        "Oui. L'IA accélère la production, mais les livrables restent validés par nos experts avant diffusion ou intégration.",
    },
    {
      question: "Avez-vous une approche sur mesure ?",
      answer:
        "Oui. Nous concevons les scénarios d'automatisation en fonction de vos processus, de votre stack technique et de vos contraintes métier.",
    },
  ],
  finalTitle: "Parlons de votre projet",
  finalSubtitle: "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta: "Profitez de votre audit offert",
};

export default function IaAutomatisations() {
  return <ServiceDetailPage {...data} />;
}
