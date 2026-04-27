import {
  ArrowRight,
  HeartHandshake,
  Lightbulb,
  Mail,
  MapPin,
  Phone,
  Play,
  ShieldCheck,
  Target,
  User,
  Users,
} from "lucide-react";
import SectionHeading from "./SectionHeading.jsx";

const values = [
  ["Excellence", "Nous visons l'excellence dans chaque projet, chaque stratégie, chaque livrable.", Target],
  ["Engagement", "Votre succès est notre priorité. Nous nous engageons à vos côtés sur le long terme.", HeartHandshake],
  ["Collaboration", "Nous travaillons en partenariat avec nos clients, pas seulement pour eux.", Users],
  ["Innovation", "Nous explorons constamment de nouvelles approches pour garder une longueur d'avance.", Lightbulb],
];

const team = [
  ["Fondateur & CEO", "Stratégie & Vision", "20 ans d'expérience en marketing digital et transformation digitale."],
  ["Directrice Créative", "Branding & Contenu", "Experte en identité de marque et stratégie de contenu B2B."],
  ["Head of Growth", "Acquisition & Performance", "Spécialiste des campagnes d'acquisition et d'optimisation de la conversion."],
  ["Expert SEO", "Référencement naturel", "Certifié Google et Semrush, plus de 100 projets SEO réalisés."],
];

export default function About({ page = false }) {
  return page ? <AboutPage /> : <HomeAbout />;
}

function HomeAbout() {
  return (
    <section id="a-propos" className="bg-card py-16 md:py-24">
      <div className="section-container">
        <div className="grid items-center gap-12 lg:grid-cols-2">
          <div className="group card-3d relative aspect-video cursor-pointer overflow-hidden rounded-2xl">
            <div className="absolute inset-0 bg-gradient-to-br from-primary/20 to-secondary/20" />
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="flex h-20 w-20 items-center justify-center rounded-full bg-secondary text-secondary-foreground shadow-glow transition-transform group-hover:scale-110">
                <Play className="ml-1 h-8 w-8" />
              </div>
            </div>
            <div className="absolute inset-0 bg-gradient-to-t from-background/80 via-transparent to-transparent" />
            <div className="absolute bottom-6 left-6 right-6">
              <p className="text-sm text-muted-foreground">Découvrez notre approche</p>
              <p className="text-lg font-semibold">2:45 min</p>
            </div>
          </div>

          <div>
            <SectionHeading title="Qui sommes-nous ?" centered={false} />
            <div className="space-y-6 text-muted-foreground">
              <p className="text-lg">
                Chez <span className="font-semibold text-foreground">Harmony Works Consulting</span>, nous croyons qu'une
                organisation ne peut croître durablement sans aligner trois piliers fondamentaux : une stratégie claire, un
                leadership responsable et une performance humaine cultivée.
              </p>
              <p>
                C'est pourquoi nous accompagnons les dirigeants ambitieux dans la structuration complète de leur moteur de
                croissance en harmonisant marketing digital, développement managérial et excellence opérationnelle.
              </p>
              <p>
                Présents au Maroc, aux îles Maurice, en France et au Luxembourg, nous intervenons comme de véritables
                partenaires engagés, avec l'exigence et le courage nécessaires pour transformer vos défis en résultats
                mesurables.
              </p>
              <div className="grid grid-cols-3 gap-6 pt-6">
                {[
                  ["20+", "Ans d'expertise"],
                  ["98%", "Dirigeants recommandent HWC"],
                  ["4", "Pays d'intervention"],
                ].map(([value, label]) => (
                  <div className="text-center" key={label}>
                    <p className="font-display text-3xl font-bold text-gradient">{value}</p>
                    <p className="text-sm text-muted-foreground">{label}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

function AboutPage() {
  return (
    <>
      <section className="relative overflow-hidden pb-16 pt-32">
        <div className="absolute inset-0 -z-10 bg-gradient-to-b from-primary/10 via-transparent to-transparent" />
        <div className="section-container relative z-10">
          <div className="mx-auto max-w-3xl text-center">
            <img src="/images/Logo_HWC-J-9-1DQr.png" alt="HWC" className="mx-auto mb-8 h-16 w-auto" />
            <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
              À propos de <span className="text-gradient">HWC</span>
            </h1>
            <p className="mb-8 text-xl text-muted-foreground">
              Harmony Works Consulting accompagne les entreprises ambitieuses dans leur transformation digitale depuis 2018.
            </p>
          </div>
        </div>
      </section>

      <section className="bg-card py-16 md:py-24">
        <div className="section-container">
          <div className="grid items-center gap-12 lg:grid-cols-2">
            <div>
              <h2 className="mb-6 font-display text-3xl font-bold">
                Notre <span className="text-gradient">histoire</span>
              </h2>
              <div className="space-y-4 text-muted-foreground">
                <p>
                  <span className="font-semibold text-foreground">Harmony Works Consulting</span> est née d'une conviction :
                  le marketing digital peut et doit générer des résultats concrets et mesurables pour les entreprises.
                </p>
                <p>
                  Fondée à Casablanca en 2018, notre agence s'est rapidement développée pour accompagner des clients au
                  Maroc, en France et au Luxembourg.
                </p>
                <p>
                  Aujourd'hui, nous sommes une équipe de 15 experts passionnés, unis par la même ambition : aider nos clients
                  à atteindre leurs objectifs de croissance grâce au digital.
                </p>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              {[["2018", "Création"], ["15+", "Experts"], ["150+", "Projets"], ["3", "Pays"]].map(([value, label]) => (
                <div className="card-elevated p-6 text-center" key={label}>
                  <p className="mb-2 font-display text-4xl font-bold text-gradient">{value}</p>
                  <p className="text-sm text-muted-foreground">{label}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24">
        <div className="section-container">
          <SectionHeading title="Nos valeurs" subtitle="Les principes qui guident notre travail au quotidien" />
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {values.map(([title, description, Icon]) => (
              <div className="card-elevated card-3d p-6 text-center" key={title}>
                <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-primary to-secondary">
                  <Icon className="h-7 w-7 text-primary-foreground" />
                </div>
                <h3 className="mb-2 text-lg font-semibold">{title}</h3>
                <p className="text-sm text-muted-foreground">{description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="bg-card py-16 md:py-24">
        <div className="section-container">
          <SectionHeading title="Notre équipe" subtitle="Des experts passionnés à votre service" />
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            {team.map(([name, role, description]) => (
              <div className="card-elevated p-6 text-center" key={name}>
                <div className="mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary">
                  <User className="h-10 w-10 text-primary-foreground" />
                </div>
                <h3 className="mb-1 font-semibold">{name}</h3>
                <p className="mb-2 text-sm text-secondary">{role}</p>
                <p className="text-xs text-muted-foreground">{description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24">
        <div className="section-container">
          <div className="grid gap-12 lg:grid-cols-2">
            <div>
              <h2 className="mb-6 font-display text-3xl font-bold">
                <span className="text-gradient">Contactez</span>-nous
              </h2>
              <p className="mb-8 text-muted-foreground">
                Nous sommes disponibles pour discuter de votre projet et répondre à toutes vos questions.
              </p>
              <div className="space-y-4">
                <ContactItem icon={MapPin} title="Nos bureaux">
                  <p>Casablanca, Maroc</p>
                  <p>Paris, France</p>
                  <p>Luxembourg</p>
                </ContactItem>
                <ContactItem icon={Mail} title="Email">
                  <a href="mailto:contact@hwc-consulting.com" className="hover:text-secondary">
                    contact@hwc-consulting.com
                  </a>
                </ContactItem>
                <ContactItem icon={Phone} title="Téléphone">
                  <p>+212 5 XX XX XX XX</p>
                  <p>+33 1 XX XX XX XX</p>
                </ContactItem>
              </div>
            </div>
            <div className="card-elevated rounded-2xl p-8">
              <ShieldCheck className="mb-5 h-10 w-10 text-secondary" />
              <h3 className="mb-4 text-xl font-semibold">Prenez rendez-vous</h3>
              <p className="mb-6 text-muted-foreground">
                Réservez un créneau de 30 minutes pour discuter de votre projet avec l'un de nos experts.
              </p>
              <a href="https://calendly.com" target="_blank" rel="noopener noreferrer" className="btn btn-hero h-12 w-full px-8 text-base">
                Réserver un appel
                <ArrowRight className="ml-2 h-5 w-5" />
              </a>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

function ContactItem({ icon: Icon, title, children }) {
  return (
    <div className="flex items-start gap-4">
      <div className="flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-lg bg-muted">
        <Icon className="h-5 w-5 text-secondary" />
      </div>
      <div>
        <p className="font-medium">{title}</p>
        <div className="text-sm text-muted-foreground transition-colors">{children}</div>
      </div>
    </div>
  );
}
