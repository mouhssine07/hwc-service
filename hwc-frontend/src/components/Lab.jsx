import { useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import {
  ArrowLeft,
  ArrowRight,
  BookOpen,
  Calendar,
  CheckCircle2,
  ChevronLeft,
  ChevronRight,
  Clock,
  FileText,
  Quote,
  Search,
  Share2,
  Sparkles,
  Tag,
  TrendingUp,
} from "lucide-react";
import SectionHeading from "./SectionHeading.jsx";

const certifications = [
  ["HubSpot", "●"],
  ["Semrush", "●"],
  ["Google Ads", "●"],
  ["Google Ateliers Numériques", "●"],
  ["Meta", "●"],
];

const homePosts = [
  {
    icon: TrendingUp,
    title: "Multiplier les leads qualifiés sans alourdir l'équipe",
    description:
      "Méthodes d'acquisition B2B, priorisation des canaux et pilotage par KPI.",
  },
  {
    icon: Search,
    title: "Construire une stratégie SEO orientée revenu",
    description:
      "Des contenus utiles, une architecture claire et une mesure rigoureuse de la demande.",
  },
  {
    icon: BookOpen,
    title: "Outsourcing : passer du renfort au système performant",
    description:
      "Comment combiner ressource dédiée, supervision et amélioration continue.",
  },
];

const testimonials = [
  {
    quote:
      "HWC a transformé notre approche digitale. En 6 mois, nous avons multiplié par 3 nos leads qualifiés.",
    author: "Marie Dubois",
    role: "Directrice Marketing",
    company: "Interflons",
  },
  {
    quote:
      "Une équipe réactive et experte. Leur méthodologie appliquée au marketing est vraiment efficace.",
    author: "Jean-Claude Carpentier",
    role: "CEO",
    company: "JCC Industries",
  },
  {
    quote:
      "Grâce à HWC, notre visibilité SEO a fortement progressé sur nos mots-clés stratégiques.",
    author: "Sophie Martin",
    role: "Responsable Digital",
    company: "TechCorp",
  },
  {
    quote:
      "Professionnalisme et créativité au rendez-vous. Nos campagnes social media sont plus claires et performantes.",
    author: "Ahmed Bennani",
    role: "Fondateur",
    company: "GlobalBiz",
  },
];

const labArticles = [
  {
    id: "1",
    type: "article",
    theme: "marketing",
    title: "10 stratégies d'acquisition B2B qui fonctionnent en 2025",
    excerpt:
      "Découvrez les méthodes les plus efficaces pour générer des leads qualifiés cette année.",
    image: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=800",
    articleImage:
      "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=1200",
    date: "15 Déc 2024",
    fullDate: "15 Décembre 2024",
    readTime: "8 min",
    content: [
      "L'acquisition B2B a considérablement évolué ces dernières années. Les techniques qui fonctionnaient il y a quelques années sont devenues obsolètes, remplacées par des approches plus sophistiquées et centrées sur la valeur.",
      "Dans cet article, nous explorons les 10 stratégies d'acquisition qui génèrent les meilleurs résultats pour nos clients en 2025. Ces méthodes ont été testées et validées auprès de dizaines d'entreprises B2B dans différents secteurs.",
      "## 1. Le content marketing ciblé\n\nLe contenu reste roi, mais la clé est de créer du contenu ultra-ciblé qui répond aux problématiques spécifiques de votre audience. Les guides pratiques et les études de cas génèrent en moyenne 3x plus de leads que les contenus génériques.",
      "## 2. Le social selling sur LinkedIn\n\nLinkedIn est devenu incontournable pour le B2B. Une stratégie de social selling bien exécutée peut générer jusqu'à 40% de vos leads qualifiés.",
      "## 3. Les webinaires interactifs\n\nLes webinaires continuent de performer, à condition d'apporter une vraie valeur et d'inclure des éléments interactifs comme des sondages et des Q&A en direct.",
      "## 4. L'Account Based Marketing (ABM)\n\nL'ABM permet de concentrer vos efforts sur les comptes à plus fort potentiel. Cette approche génère un ROI 87% supérieur aux autres stratégies marketing.",
      "## 5. L'automatisation intelligente\n\nL'automatisation ne signifie pas déshumanisation. Utilisez l'IA pour personnaliser vos communications à grande échelle tout en gardant une touche humaine.",
    ],
  },
  {
    id: "2",
    type: "blog",
    theme: "branding",
    title: "Comment construire une marque forte en B2B",
    excerpt:
      "Les clés pour développer une identité de marque qui résonne avec vos clients professionnels.",
    image: "https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=800",
    date: "12 Déc 2024",
    readTime: "6 min",
  },
  {
    id: "3",
    type: "article",
    theme: "ia",
    title: "L'IA au service du marketing digital",
    excerpt:
      "Comment l'intelligence artificielle transforme les stratégies marketing modernes.",
    image: "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=800",
    date: "10 Déc 2024",
    readTime: "10 min",
  },
  {
    id: "4",
    type: "blog",
    theme: "leadership",
    title: "Leadership digital : manager à l'ère du remote",
    excerpt:
      "Adapter son style de management aux équipes distribuées et hybrides.",
    image: "https://images.unsplash.com/photo-1556761175-b413da4baf72?w=800",
    date: "5 Déc 2024",
    readTime: "7 min",
  },
  {
    id: "5",
    type: "article",
    theme: "management",
    title: "Optimiser la productivité de votre équipe marketing",
    excerpt:
      "Outils et méthodes pour maximiser l'efficacité de vos équipes créatives.",
    image: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=800",
    date: "1 Déc 2024",
    readTime: "5 min",
  },
  {
    id: "6",
    type: "blog",
    theme: "marketing",
    title: "Les tendances SEO à surveiller en 2025",
    excerpt:
      "Google évolue constamment. Voici ce qui va changer dans le référencement naturel.",
    image: "https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?w=800",
    date: "28 Nov 2024",
    readTime: "9 min",
  },
];

const relatedArticles = [
  {
    id: "3",
    title: "Construire une marque B2B mémorable",
    image: "https://images.unsplash.com/photo-1542744094-3a31f272c490?w=400",
    readTime: "10 min",
  },
  {
    id: "4",
    title: "Le leadership à l'ère du travail hybride",
    image: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=400",
    readTime: "7 min",
  },
  {
    id: "5",
    title: "Optimiser la productivité de votre équipe marketing",
    image: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=400",
    readTime: "5 min",
  },
];

const typeFilters = [
  ["all", "Tous"],
  ["article", "Articles"],
  ["blog", "Blog"],
];

const themeFilters = [
  ["all", "Toutes"],
  ["marketing", "Marketing"],
  ["branding", "Branding"],
  ["ia", "Intelligence Artificielle"],
  ["management", "Management"],
  ["leadership", "Leadership"],
];

const themeStyles = {
  marketing: "bg-primary/20 text-primary",
  branding: "bg-secondary/20 text-secondary",
  ia: "bg-accent/20 text-accent",
  management: "bg-primary/20 text-primary",
  leadership: "bg-secondary/20 text-secondary",
};

const themeLabels = Object.fromEntries(themeFilters);
const typeLabels = Object.fromEntries(typeFilters);

export default function Lab({ page = false, article = false }) {
  if (article) {
    return <LabArticlePage />;
  }

  if (page) {
    return <LabPage />;
  }

  return <HomeLab />;
}

function HomeLab() {
  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading
          title="Certifications & Partenariats"
          subtitle="Notre équipe est certifiée par les leaders du marché"
        />

        <div className="flex flex-wrap items-center justify-center gap-8 md:gap-16">
          {certifications.map(([name, logo]) => (
            <div
              className="group card-elevated flex cursor-default flex-col items-center gap-3 rounded-2xl p-6 transition-all hover:border-secondary/50"
              key={name}
            >
              <div className="text-4xl text-secondary transition-transform group-hover:scale-110">
                {logo}
              </div>
              <span className="text-sm font-medium text-muted-foreground transition-colors group-hover:text-foreground">
                {name}
              </span>
            </div>
          ))}
        </div>
        <Testimonials />
      </div>
    </section>
  );
}

function LabPage() {
  const [type, setType] = useState("all");
  const [theme, setTheme] = useState("all");

  const filteredArticles = useMemo(
    () =>
      labArticles.filter((article) => {
        const matchesType = type === "all" || article.type === type;
        const matchesTheme = theme === "all" || article.theme === theme;
        return matchesType && matchesTheme;
      }),
    [type, theme],
  );

  const resetFilters = () => {
    setType("all");
    setTheme("all");
  };

  return (
    <>
      <section className="relative overflow-hidden pb-16 pt-32">
        <div className="absolute inset-0 -z-10 bg-gradient-to-b from-primary/10 via-transparent to-transparent" />
        <div className="section-container text-center">
          <div className="glass mb-8 inline-flex items-center gap-2 rounded-full px-4 py-2 text-sm font-medium">
            <Sparkles className="h-4 w-4 text-secondary" />
            HWC Lab
          </div>
          <h1 className="mx-auto max-w-4xl font-display text-5xl font-bold leading-tight md:text-6xl lg:text-7xl">
            Ressources & <span className="text-gradient">Insights</span>
          </h1>
          <p className="mx-auto mt-6 max-w-3xl text-lg leading-relaxed text-muted-foreground md:text-xl">
            Articles, analyses et conseils pour booster votre stratégie
            digitale.
          </p>
        </div>
      </section>

      <section className="border-b border-border bg-muted/30 py-6">
        <div className="section-container">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-start">
            <FilterGroup
              icon={FileText}
              label="Type de contenu"
              value={type}
              options={typeFilters}
              onChange={setType}
              className="lg:w-[360px] lg:flex-none"
            />
            <FilterGroup
              icon={Tag}
              label="Thématique"
              value={theme}
              options={themeFilters}
              onChange={setTheme}
              className="lg:min-w-0 lg:flex-1"
              nowrap
            />
          </div>
        </div>
      </section>

      <section className="py-16">
        <div className="section-container">
          {filteredArticles.length === 0 ? (
            <div className="mx-auto max-w-xl rounded-3xl border border-border bg-card p-10 text-center shadow-card">
              <Search className="mx-auto mb-5 h-12 w-12 text-muted-foreground" />
              <h2 className="font-display text-2xl font-bold text-foreground">
                Aucun contenu trouvé
              </h2>
              <p className="mt-3 text-muted-foreground">
                Essayez une autre combinaison de filtres.
              </p>
              <button
                type="button"
                onClick={resetFilters}
                className="btn-hero mt-6"
              >
                Réinitialiser les filtres
              </button>
            </div>
          ) : (
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              {filteredArticles.map((article) => (
                <Link
                  to={`/lab/${article.id}`}
                  className="group card-elevated overflow-hidden rounded-2xl transition-all hover:-translate-y-1 hover:border-secondary/50"
                  key={article.id}
                >
                  <div className="relative aspect-video overflow-hidden">
                    <img
                      src={article.image}
                      alt={article.title}
                      className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-110"
                    />
                    <div className="absolute left-4 top-4 flex flex-wrap gap-2">
                      <span className="rounded-full bg-card/90 px-3 py-1 text-xs font-semibold uppercase text-foreground backdrop-blur">
                        {typeLabels[article.type]}
                      </span>
                      <span
                        className={`rounded-full px-3 py-1 text-xs font-semibold ${
                          themeStyles[article.theme] ||
                          "bg-muted text-muted-foreground"
                        }`}
                      >
                        {themeLabels[article.theme]}
                      </span>
                    </div>
                  </div>

                  <div className="p-6">
                    <h2 className="mb-3 font-display text-xl font-bold leading-tight text-foreground transition-colors group-hover:text-secondary">
                      {article.title}
                    </h2>
                    <p className="mb-6 text-sm leading-relaxed text-muted-foreground">
                      {article.excerpt}
                    </p>
                    <div className="flex items-center justify-between text-sm text-muted-foreground">
                      <span>{article.date}</span>
                      <span className="flex items-center gap-1">
                        <Clock className="h-4 w-4" />
                        {article.readTime} de lecture
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </section>

      <section className="bg-card py-16">
        <div className="section-container text-center">
          <div className="mx-auto max-w-2xl">
            <h2 className="font-display text-4xl font-bold md:text-5xl">
              Restez <span className="text-gradient">informé</span>
            </h2>
            <p className="mt-4 text-lg text-muted-foreground">
              Recevez nos derniers articles et conseils directement dans votre
              boîte mail.
            </p>
            <a
              href="#newsletter"
              className="btn-hero mt-8 inline-flex items-center gap-2"
            >
              S'abonner à la newsletter
              <ArrowRight className="h-5 w-5" />
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

function LabArticlePage() {
  const { articleId } = useParams();
  const article = labArticles.find((item) => item.id === articleId);

  if (!article) {
    return (
      <section className="pt-32">
        <div className="section-container py-16 text-center">
          <h1 className="mb-4 font-display text-4xl font-bold">
            Article non trouvé
          </h1>
          <Link to="/lab" className="btn-hero inline-flex">
            Retour au Lab
          </Link>
        </div>
      </section>
    );
  }

  return (
    <>
      <section className="pb-8 pt-32">
        <div className="section-container">
          <Link
            to="/lab"
            className="mb-8 inline-flex items-center gap-2 text-muted-foreground transition-colors hover:text-foreground"
          >
            <ArrowLeft className="h-4 w-4" />
            Retour au Lab
          </Link>

          <div className="max-w-3xl">
            <div className="mb-4 flex flex-wrap gap-2">
              <span className="rounded-full bg-muted px-3 py-1 text-sm capitalize">
                {article.type}
              </span>
              <span className="rounded-full bg-secondary/20 px-3 py-1 text-sm text-secondary">
                {themeLabels[article.theme]}
              </span>
            </div>
            <h1 className="mb-6 font-display text-3xl font-bold md:text-4xl lg:text-5xl">
              {article.title}
            </h1>
            <p className="mb-6 text-xl text-muted-foreground">
              {article.excerpt}
            </p>
            <div className="flex flex-wrap items-center gap-6 text-sm text-muted-foreground">
              <div className="flex items-center gap-2">
                <Calendar className="h-4 w-4" />
                <span>{article.fullDate || article.date}</span>
              </div>
              <div className="flex items-center gap-2">
                <Clock className="h-4 w-4" />
                <span>{article.readTime} de lecture</span>
              </div>
              <button
                type="button"
                className="flex items-center gap-2 transition-colors hover:text-secondary"
              >
                <Share2 className="h-4 w-4" />
                <span>Partager</span>
              </button>
            </div>
          </div>
        </div>
      </section>

      <section className="py-8">
        <div className="section-container">
          <div className="mx-auto max-w-4xl">
            <img
              src={article.articleImage || article.image}
              alt={article.title}
              className="aspect-video w-full rounded-2xl object-cover"
            />
          </div>
        </div>
      </section>

      <section className="py-8">
        <div className="section-container">
          <article className="mx-auto max-w-3xl">
            {article.content?.map((block, index) => (
              <ArticleBlock block={block} key={index} />
            ))}
          </article>
        </div>
      </section>

      <section className="bg-card py-16 md:py-24">
        <div className="section-container">
          <div className="mx-auto max-w-4xl">
            <h2 className="mb-8 font-display text-2xl font-bold">
              Vous pourriez être{" "}
              <span className="text-gradient">intéressé</span> par...
            </h2>
            <div className="grid gap-6 md:grid-cols-3">
              {relatedArticles.map((item) => (
                <Link
                  to={`/lab/${item.id}`}
                  className="group card-elevated overflow-hidden rounded-xl transition-all hover:border-secondary/50"
                  key={item.id}
                >
                  <div className="aspect-video overflow-hidden">
                    <img
                      src={item.image}
                      alt={item.title}
                      className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
                    />
                  </div>
                  <div className="p-4">
                    <h3 className="line-clamp-2 text-sm font-medium transition-colors group-hover:text-secondary">
                      {item.title}
                    </h3>
                    <p className="mt-2 text-xs text-muted-foreground">
                      {item.readTime} de lecture
                    </p>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24">
        <div className="section-container text-center">
          <div className="mx-auto max-w-2xl">
            <h2 className="mb-6 font-display text-3xl font-bold md:text-4xl">
              Besoin d'<span className="text-gradient">accompagnement</span> ?
            </h2>
            <p className="mb-8 text-muted-foreground">
              Nos experts sont là pour vous aider à mettre en place ces
              stratégies dans votre entreprise.
            </p>
            <a
              href="https://calendly.com"
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center justify-center gap-2 whitespace-nowrap ring-offset-background transition-all duration-300 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0 bg-gradient-to-r from-primary to-secondary text-primary-foreground font-semibold shadow-lg hover:shadow-glow hover:scale-105 active:scale-100 h-14 rounded-xl px-10 text-lg"
            >
              Discutons de votre projet
              <ArrowRight className="h-5 w-5" />
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

function ArticleBlock({ block }) {
  if (block.startsWith("##")) {
    const [heading, ...body] = block.replace("## ", "").split("\n\n");
    return (
      <>
        <h2 className="mb-4 mt-8 font-display text-2xl font-bold text-gradient">
          {heading}
        </h2>
        {body.map((paragraph) => (
          <p
            className="mb-4 text-lg leading-relaxed text-muted-foreground"
            key={paragraph}
          >
            {paragraph}
          </p>
        ))}
      </>
    );
  }

  return (
    <p className="mb-4 text-lg leading-relaxed text-muted-foreground">
      {block}
    </p>
  );
}

function FilterGroup({
  icon: Icon,
  label,
  options,
  value,
  onChange,
  className = "",
  nowrap = false,
}) {
  return (
    <div
      className={`rounded-2xl bg-background/80 p-4 shadow-card ${className}`}
    >
      <div className="mb-3 flex items-center gap-2 text-sm font-semibold text-foreground">
        <Icon className="h-4 w-4 text-secondary" />
        {label}
      </div>
      <div
        className={`${nowrap ? "flex-nowrap overflow-x-auto pb-1" : "flex-wrap"} flex gap-2`}
      >
        {options.map(([id, name]) => {
          const active = id === value;
          return (
            <button
              type="button"
              onClick={() => onChange(id)}
              className={`shrink-0 whitespace-nowrap rounded-full px-4 py-2 text-sm font-medium transition-all ${
                active
                  ? "bg-secondary text-secondary-foreground"
                  : "bg-muted text-muted-foreground hover:bg-muted/80"
              }`}
              key={id}
            >
              {active && <CheckCircle2 className="mr-1 inline h-3.5 w-3.5" />}
              {name}
            </button>
          );
        })}
      </div>
    </div>
  );
}

function Testimonials() {
  const [active, setActive] = useState(0);
  const testimonial = testimonials[active];

  const previous = () => {
    setActive((current) =>
      current === 0 ? testimonials.length - 1 : current - 1,
    );
  };

  const next = () => {
    setActive((current) =>
      current === testimonials.length - 1 ? 0 : current + 1,
    );
  };

  return (
    <div className="mt-20 rounded-3xl bg-card py-16">
      <SectionHeading
        title="Ce que disent nos clients"
        subtitle="La satisfaction de nos clients est notre meilleure récompense"
      />
      <div className="relative mx-auto max-w-4xl px-14 text-center">
        <button
          type="button"
          onClick={previous}
          className="absolute left-0 top-1/2 hidden h-11 w-11 -translate-y-1/2 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary md:flex"
          aria-label="Témoignage précédent"
        >
          <ChevronLeft className="h-5 w-5" />
        </button>

        <div key={active} className="animate-fade-in">
          <Quote className="mx-auto mb-6 h-12 w-12 text-secondary/30" />
          <p className="mb-8 font-display text-xl font-medium text-foreground md:text-2xl lg:text-3xl">
            “{testimonial.quote}”
          </p>
          <p className="font-semibold text-foreground">{testimonial.author}</p>
          <p className="text-muted-foreground">
            {testimonial.role}, {testimonial.company}
          </p>
        </div>

        <button
          type="button"
          onClick={next}
          className="absolute right-0 top-1/2 hidden h-11 w-11 -translate-y-1/2 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary md:flex"
          aria-label="Témoignage suivant"
        >
          <ChevronRight className="h-5 w-5" />
        </button>

        <div className="mt-8 flex items-center justify-center gap-3">
          {testimonials.map((item, index) => (
            <button
              type="button"
              onClick={() => setActive(index)}
              className={`h-3 rounded-full transition-all ${
                index === active
                  ? "w-8 bg-secondary"
                  : "w-3 bg-muted hover:bg-muted-foreground/30"
              }`}
              aria-label={`Voir le témoignage ${index + 1} de ${item.author}`}
              aria-current={index === active}
              key={item.author}
            />
          ))}
        </div>

        <div className="mt-6 flex justify-center gap-3 md:hidden">
          <button
            type="button"
            onClick={previous}
            className="flex h-10 w-10 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary"
            aria-label="Témoignage précédent"
          >
            <ChevronLeft className="h-5 w-5" />
          </button>
          <button
            type="button"
            onClick={next}
            className="flex h-10 w-10 items-center justify-center rounded-full border border-border bg-background text-muted-foreground shadow-card transition-all hover:border-secondary hover:text-secondary"
            aria-label="Témoignage suivant"
          >
            <ChevronRight className="h-5 w-5" />
          </button>
        </div>
      </div>
    </div>
  );
}
