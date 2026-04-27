import { useState } from "react";
import { ArrowRight, Check, ChevronDown } from "lucide-react";
import SectionHeading from "./SectionHeading.jsx";

export default function ServiceDetailPage({
  eyebrow,
  title,
  subtitle,
  heroImage,
  introTitle,
  introText,
  bullets = [],
  highlights = [],
  processTitle = "Notre processus",
  processSubtitle = "",
  processSteps = [],
  faqs = [],
  finalTitle = "Parlons de votre projet",
  finalSubtitle = "Un premier échange pour comprendre vos enjeux et construire une approche sur-mesure.",
  finalCta = "Parlons de votre projet",
}) {
  return (
    <>
      <section className="relative overflow-hidden pb-16 pt-24 md:pt-32">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-secondary/5" />
        {heroImage ? <div className="absolute inset-0 bg-cover bg-center opacity-20" style={{ backgroundImage: `url(${heroImage})` }} /> : null}
        <div className="section-container relative z-10">
          <div className="mx-auto max-w-4xl text-center">
            <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-secondary/20 bg-secondary/10 px-4 py-2">
              <span className="text-sm font-medium text-secondary">{eyebrow}</span>
            </div>
            <h1 className="mb-6 font-display text-4xl font-bold md:text-5xl lg:text-6xl">
              {title}
            </h1>
            <p className="mx-auto max-w-3xl text-xl text-muted-foreground">{subtitle}</p>
            <div className="mt-8 flex flex-wrap justify-center gap-4">
              <a className="btn btn-hero h-14 px-8 text-base" href="https://calendly.com" target="_blank" rel="noreferrer">
                {finalCta}
                <ArrowRight className="ml-2 h-5 w-5" />
              </a>
              <a className="btn btn-outline-hero h-14 px-8 text-base" href="#process">
                Découvrir la méthode
              </a>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24">
        <div className="section-container">
          <div className="grid items-center gap-12 lg:grid-cols-2">
            <div>
              <SectionHeading title={introTitle} centered={false} />
              <p className="mb-6 text-muted-foreground">{introText}</p>
              {bullets.length ? (
                <ul className="space-y-3">
                  {bullets.map((item) => (
                    <li className="flex items-start gap-3" key={item}>
                      <Check className="mt-0.5 h-5 w-5 flex-shrink-0 text-secondary" />
                      <span className="text-foreground">{item}</span>
                    </li>
                  ))}
                </ul>
              ) : null}
            </div>

            <div className="grid gap-4">
              {highlights.map((item) => (
                <div className="card-elevated p-6" key={item.title}>
                  <h3 className="mb-2 text-lg font-semibold text-foreground">{item.title}</h3>
                  <p className="text-sm text-muted-foreground">{item.description}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {processSteps.length ? (
        <section id="process" className="bg-muted/30 py-16 md:py-24">
          <div className="section-container">
            <SectionHeading title={processTitle} subtitle={processSubtitle} />
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
              {processSteps.map((item) => (
                <div className="card-elevated relative p-6" key={item.step}>
                  <div className="absolute -left-4 -top-4 flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary to-secondary font-bold text-primary-foreground">
                    {item.step}
                  </div>
                  <h4 className="mb-2 mt-4 font-semibold text-foreground">{item.title}</h4>
                  <p className="text-sm text-muted-foreground">{item.description}</p>
                </div>
              ))}
            </div>
          </div>
        </section>
      ) : null}

      {faqs.length ? <FaqSection faqs={faqs} /> : null}

      <section className="bg-card py-16 md:py-24">
        <div className="section-container text-center">
          <div className="mx-auto max-w-2xl">
            <h2 className="font-display text-3xl font-bold md:text-4xl">{finalTitle}</h2>
            <p className="mt-4 text-lg text-muted-foreground">{finalSubtitle}</p>
            <a href="https://calendly.com" target="_blank" rel="noopener noreferrer" className="btn-hero mt-8 inline-flex items-center gap-2">
              {finalCta}
              <ArrowRight className="h-5 w-5" />
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

function FaqSection({ faqs }) {
  const [open, setOpen] = useState(0);

  return (
    <section className="py-16 md:py-24">
      <div className="section-container">
        <SectionHeading title="Questions fréquentes" subtitle="Tout ce que vous devez savoir sur ce service" />
        <div className="mx-auto max-w-3xl space-y-4">
          {faqs.map((faq, index) => {
            const active = open === index;
            return (
              <div className="card-elevated rounded-xl px-6" key={faq.question}>
                <button
                  type="button"
                  onClick={() => setOpen(active ? -1 : index)}
                  className="flex w-full items-center justify-between gap-4 py-6 text-left"
                >
                  <span className="font-medium text-foreground">{faq.question}</span>
                  <ChevronDown className={`h-4 w-4 shrink-0 transition-transform ${active ? "rotate-180" : ""}`} />
                </button>
                <div
                  className={`grid transition-all duration-300 ease-out ${
                    active ? "grid-rows-[1fr] opacity-100" : "grid-rows-[0fr] opacity-0"
                  }`}
                >
                  <div className="overflow-hidden">
                    <p className="pb-6 text-sm leading-relaxed text-muted-foreground">{faq.answer}</p>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
