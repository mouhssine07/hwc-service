import { Facebook, Instagram, Linkedin, Twitter } from "lucide-react";
import { Link } from "react-router-dom";

const footerColumns = [
  {
    title: "Nos Offres",
    links: [
      { label: "Régie & Outsourcing", href: "/offres/regie-outsourcing" },
      { label: "Leadership & Corporate Events", href: "/offres/leadership-corporate-events" },
    ],
  },
  {
    title: "Régie & Outsourcing",
    links: [
      { label: "Marketing Digital", href: "/offres/regie-outsourcing/marketing-digital" },
      { label: "Prospection Commerciale", href: "/offres/regie-outsourcing/prospection-commerciale" },
      { label: "Support Client", href: "/offres/regie-outsourcing/support-client" },
    ],
  },
  {
    title: "Informations",
    links: [
      { label: "Mentions légales", href: "#" },
      { label: "CGV", href: "#" },
      { label: "Politique de confidentialité", href: "#" },
    ],
  },
];

const socials = [
  { icon: Linkedin, label: "LinkedIn" },
  { icon: Instagram, label: "Instagram" },
  { icon: Facebook, label: "Facebook" },
  { icon: Twitter, label: "Twitter" },
];

export default function Footer() {
  return (
    <footer className="bg-primary text-primary-foreground lg:min-h-[45vh]">
      <div className="section-container py-10 lg:py-12">
        <div className="grid gap-8 lg:grid-cols-[1.45fr_1fr_1.15fr_1.15fr_1.2fr] lg:gap-12">
          <div>
            <Link to="/" className="mb-4 block">
              <img
                src="/images/Logo_HWC-J-9-1DQr.png"
                alt="Harmony Works Consulting"
                className="h-9 w-auto brightness-0 invert"
              />
            </Link>
            <p className="mb-5 max-w-xs text-sm font-medium leading-relaxed text-primary-foreground/90">
              Agence de conseil en croissance, outsourcing et leadership opérant à Maurice, au Maroc, en France et au
              Luxembourg.
            </p>
            <div className="space-y-1.5 text-sm font-medium text-primary-foreground/90">
              <p>
                <span className="mr-1 text-xs uppercase tracking-wide">MU</span> Maurice
              </p>
              <p>
                <span className="mr-1 text-xs uppercase tracking-wide">MA</span> Casablanca, Maroc
              </p>
              <p>
                <span className="mr-1 text-xs uppercase tracking-wide">FR</span> Paris, France
              </p>
              <p>
                <span className="mr-1 text-xs uppercase tracking-wide">LU</span> Luxembourg
              </p>
              <a className="block pt-1 transition-colors hover:text-white" href="mailto:contact@hwc-consulting.com">
                contact@hwc-consulting.com
              </a>
            </div>
          </div>

          {footerColumns.map((column) => (
            <div key={column.title}>
              <h3 className="mb-5 font-display text-lg font-bold text-white">{column.title}</h3>
              <ul className="space-y-3.5 text-sm font-medium text-primary-foreground/90">
                {column.links.map((link) => (
                  <li key={link.label}>
                    <a className="transition-colors hover:text-white" href={link.href}>
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          ))}

          <div>
            <h3 className="mb-5 font-display text-lg font-bold text-white">Suivez-nous</h3>
            <div className="flex flex-wrap gap-4">
              {socials.map(({ icon: Icon, label }) => (
                <a
                  className="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-primary-foreground/80 transition-all hover:bg-white hover:text-primary"
                  href="#"
                  aria-label={label}
                  key={label}
                >
                  <Icon className="h-5 w-5" />
                </a>
              ))}
            </div>
          </div>
        </div>

        <div className="mt-10 flex flex-col items-start justify-between gap-4 border-t border-white/20 pt-7 text-sm font-medium text-primary-foreground/90 md:flex-row md:items-center">
          <p>© {new Date().getFullYear()} Harmony Works Consulting. Tous droits réservés.</p>
          <p>
            Conçu avec passion <span className="text-sm uppercase tracking-wide">MU MA FR LU</span>
          </p>
        </div>
      </div>
    </footer>
  );
}
