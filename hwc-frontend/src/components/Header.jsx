import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Check, ChevronDown, ClipboardList, Globe2, Menu, X } from "lucide-react";

const offers = [
  {
    name: "Régie & Outsourcing",
    description: "Équipes opérationnelles performantes",
    href: "/offres/regie-outsourcing",
  },
  {
    name: "Leadership & Corporate Events",
    description: "Formation, leadership et organisation",
    href: "/offres/leadership-corporate-events",
  },
  {
    name: "Performance sous pression",
    description: "Leadership sous stress et cohésion",
    href: "/offres/performance-sous-pression",
  },
];

function Logo({ inverted = false }) {
  return (
    <img
      src="/images/Logo_HWC-J-9-1DQr.png"
      alt="Harmony Works Consulting"
      className={`h-10 w-auto object-contain md:h-12 ${inverted ? "brightness-0 invert" : ""}`}
    />
  );
}

const languages = [
  { code: "FR", name: "France" },
  { code: "MA", name: "Maroc" },
  { code: "MU", name: "Maurice" },
  { code: "LU", name: "Luxembourg" },
];

function LanguageSelector() {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(languages[0]);

  return (
    <div className="relative">
      <button
        type="button"
        aria-haspopup="listbox"
        aria-expanded={open}
        onClick={() => setOpen((value) => !value)}
        className="inline-flex h-10 items-center gap-2 rounded-xl border border-border bg-card px-3 text-sm font-semibold text-foreground/80 shadow-sm transition-all hover:border-secondary hover:text-primary"
      >
        <Globe2 className="h-4 w-4" />
        {selected.code}
        <ChevronDown className={`h-4 w-4 transition-transform ${open ? "rotate-180" : ""}`} />
      </button>

      {open && (
        <div className="absolute right-0 top-full z-50 mt-2 w-56 rounded-xl bg-white p-2 shadow-lg ring-1 ring-border/70 animate-fade-in">
          <div className="space-y-1" role="listbox" aria-label="Choisir le pays">
            {languages.map((language) => {
              const active = language.code === selected.code;
              return (
                <button
                  type="button"
                  role="option"
                  aria-selected={active}
                  key={language.code}
                  onClick={() => {
                    setSelected(language);
                    setOpen(false);
                  }}
                  className={`flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-left font-sans transition-colors ${
                    active ? "bg-teal-50" : "bg-white hover:bg-muted"
                  }`}
                >
                  <span className="flex h-4 w-4 items-center justify-center">
                    {active ? <Check className="h-4 w-4 text-secondary" /> : null}
                  </span>
                  <span className="w-8 text-xs font-bold uppercase tracking-wide text-muted-foreground">
                    {language.code}
                  </span>
                  <span className="text-sm font-normal text-foreground">{language.name}</span>
                </button>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}

export default function Header() {
  const [scrolled, setScrolled] = useState(false);
  const [open, setOpen] = useState(false);
  const [dropdown, setDropdown] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    onScroll();
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  useEffect(() => {
    setOpen(false);
    setDropdown(false);
  }, [location.pathname]);

  return (
    <header
      className={`fixed left-0 right-0 top-0 z-50 transition-all duration-500 ${
        scrolled ? "bg-card/95 py-3 shadow-sm backdrop-blur-lg" : "bg-transparent py-5"
      }`}
    >
      <div className="section-container">
        <nav className="flex items-center justify-between">
          <Link to="/" className="flex-shrink-0" aria-label="Harmony Works Consulting">
            <Logo />
          </Link>

          <div className="hidden items-center gap-8 lg:flex">
            <div className="relative" onMouseEnter={() => setDropdown(true)} onMouseLeave={() => setDropdown(false)}>
              <button className="flex items-center gap-1 py-2 font-medium text-foreground/80 transition-colors hover:text-primary">
                Nos Offres
                <ChevronDown className={`h-4 w-4 transition-transform ${dropdown ? "rotate-180" : ""}`} />
              </button>
              {dropdown && (
                <div className="absolute left-0 top-full w-[320px] pt-2">
                  <div className="animate-fade-in rounded-xl border border-border bg-card p-3 shadow-lg">
                    <div className="space-y-1">
                      {offers.map((offer) => (
                        <Link
                          to={offer.href}
                          className="group block rounded-lg p-3 transition-colors hover:bg-muted"
                          key={offer.href}
                        >
                          <span className="text-sm font-semibold text-foreground transition-colors group-hover:text-primary">
                            {offer.name}
                          </span>
                          <p className="mt-0.5 text-xs text-muted-foreground">{offer.description}</p>
                        </Link>
                      ))}
                    </div>
                  </div>
                </div>
              )}
            </div>
            <Link className="py-2 font-medium text-foreground/80 transition-colors hover:text-primary" to="/lab">
              HWC Lab
            </Link>
            <Link className="py-2 font-medium text-foreground/80 transition-colors hover:text-primary" to="/outils">
              Boîte à outils
            </Link>
            <Link className="py-2 font-medium text-foreground/80 transition-colors hover:text-primary" to="/a-propos">
              À propos
            </Link>
          </div>

          <div className="hidden items-center gap-4 lg:flex">
            <LanguageSelector />
            <Link className="btn btn-outline-hero h-12" to="/client/diagnostic">
              <ClipboardList className="mr-2 h-4 w-4" />
              Diagnostic gratuit
            </Link>
            <a className="btn btn-cta h-12" href="https://calendly.com" target="_blank" rel="noreferrer">
              Discutons de votre projet
            </a>
          </div>

          <button className="p-2 text-foreground lg:hidden" onClick={() => setOpen((value) => !value)} aria-label="Menu">
            {open ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </nav>

        {open && (
          <div className="mt-4 max-h-[80vh] overflow-y-auto rounded-2xl border border-border bg-card p-6 shadow-lg animate-fade-in lg:hidden">
            <div className="space-y-4">
              <div className="border-b border-border pb-4">
                <LanguageSelector />
              </div>
              {offers.map((offer) => (
                <Link
                  className="block py-2 font-medium text-foreground/80 hover:text-primary"
                  to={offer.href}
                  key={offer.href}
                >
                  {offer.name}
                </Link>
              ))}
              <div className="space-y-2 border-t border-border pt-4">
                <Link className="block py-2 font-medium text-foreground/80 hover:text-primary" to="/lab">
                  HWC Lab
                </Link>
                <Link className="block py-2 font-medium text-foreground/80 hover:text-primary" to="/outils">
                  Boîte à outils
                </Link>
                <Link className="block py-2 font-medium text-foreground/80 hover:text-primary" to="/a-propos">
                  À propos
                </Link>
              </div>
              <a className="btn btn-cta mt-4 w-full" href="https://calendly.com" target="_blank" rel="noreferrer">
                Discutons de votre projet
              </a>
              <Link className="btn btn-outline-hero w-full" to="/client/diagnostic">
                <ClipboardList className="mr-2 h-4 w-4" />
                Lancer mon diagnostic
              </Link>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}

export { Logo };
