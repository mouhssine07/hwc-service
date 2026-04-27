import { ArrowRight, Calendar, Mouse } from "lucide-react";

export default function Hero() {
  const scrollToServices = () => {
    document.getElementById("services")?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <section className="relative flex min-h-screen items-center justify-center overflow-hidden">
      <div className="absolute inset-0 z-0">
        <img src="/images/hero-bg-CNz2WeeY.jpg" alt="" className="h-full w-full object-cover opacity-40" />
        <div className="absolute inset-0 bg-gradient-to-b from-background/60 via-background/80 to-background" />
      </div>
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute left-10 top-1/4 h-20 w-20 animate-float rounded-full bg-secondary/20 blur-xl" />
        <div className="absolute right-20 top-1/3 h-32 w-32 animate-float rounded-full bg-accent/40 blur-2xl [animation-delay:2s]" />
        <div className="absolute bottom-1/4 left-1/4 h-16 w-16 animate-float rounded-full bg-primary/30 blur-lg [animation-delay:4s]" />
      </div>

      <div className="section-container relative z-10 pt-24">
        <div className="mx-auto max-w-4xl text-center">
          <div className="glass mb-8 inline-flex animate-fade-in items-center gap-2 rounded-full px-4 py-2">
            <span className="h-2 w-2 animate-pulse rounded-full bg-secondary" />
            <span className="text-sm text-muted-foreground">Partenaire de développement</span>
          </div>
          <h1 className="mb-6 animate-fade-in font-display text-4xl font-bold leading-tight md:text-5xl lg:text-6xl xl:text-7xl [animation-delay:0.2s]">
            Scalez votre business en repensant votre <span className="text-gradient">moteur de croissance</span>
          </h1>
          <p className="mx-auto mb-10 max-w-2xl animate-fade-in text-lg text-muted-foreground md:text-xl [animation-delay:0.4s]">
            Harmony Works Consulting structure votre moteur de croissance en harmonisant la stratégie d'acquisition,
            développement du leadership et de l'excellence opérationnelle pour une transformation mesurable et durable.
          </p>
          <div className="flex animate-fade-in flex-col items-center justify-center gap-4 sm:flex-row [animation-delay:0.6s]">
            <button className="btn btn-hero h-14 px-8 text-base" onClick={scrollToServices}>
              Découvrez nos services
              <ArrowRight className="ml-2 h-5 w-5" />
            </button>
            <a className="btn btn-outline-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
              <Calendar className="mr-2 h-5 w-5" />
              Profitez de votre audit offert
            </a>
          </div>
          <div className="mt-16 animate-fade-in [animation-delay:0.8s]">
            <p className="mb-4 text-sm text-muted-foreground">Ils nous font confiance</p>
            <div className="flex flex-wrap items-center justify-center gap-6 opacity-60 md:gap-8">
              {["JCC", "Interflon"].map((name) => (
                <span className="text-lg font-semibold" key={name}>
                  {name}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce">
        <div className="flex h-10 w-6 items-start justify-center rounded-full border-2 border-muted-foreground/30 p-2">
          <Mouse className="h-4 w-4 text-secondary" />
        </div>
      </div>
    </section>
  );
}
