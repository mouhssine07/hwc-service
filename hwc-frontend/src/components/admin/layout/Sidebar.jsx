import {
  Award,
  BarChart3,
  BriefcaseBusiness,
  FileQuestion,
  Globe2,
  Home,
  Image,
  Layers,
  ListChecks,
  LogOut,
  MessageSquare,
  Star,
  Tags,
  Users,
  X,
} from "lucide-react";
import { NavLink } from "react-router-dom";
import useAuthStore from "../../../store/authStore.js";

const groups = [
  {
    label: "Vue générale",
    items: [{ label: "Accueil", href: "/admin", icon: Home }],
  },
  {
    label: "Contenu principal",
    items: [
      { label: "Services", href: "/admin/services", icon: BriefcaseBusiness },
      { label: "Sous-services", href: "/admin/sous-services", icon: Layers },
      { label: "Étiquettes", href: "/admin/etiquettes", icon: Tags },
      { label: "Chiffres clés", href: "/admin/chiffres-cles", icon: BarChart3 },
    ],
  },
  {
    label: "Preuves & présence",
    items: [
      { label: "Témoignages", href: "/admin/temoignages", icon: MessageSquare },
      { label: "Certifications", href: "/admin/certifications", icon: Award },
      { label: "Pays", href: "/admin/pays", icon: Globe2 },
      { label: "Clients confiance", href: "/admin/clients-confiance", icon: Users },
      { label: "Demandes contact", href: "/admin/demandes-contact", icon: MessageSquare },
    ],
  },
  {
    label: "Détails services",
    items: [
      { label: "Fonctionnalités services", href: "/admin/service-fonctionnalites", icon: ListChecks },
      { label: "Images services", href: "/admin/service-images", icon: Image },
      { label: "Fonctionnalités sous-services", href: "/admin/sous-service-fonctionnalites", icon: ListChecks },
      { label: "Avantages sous-services", href: "/admin/sous-service-avantages", icon: Star },
      { label: "Étapes sous-services", href: "/admin/sous-service-etapes", icon: BarChart3 },
      { label: "FAQs sous-services", href: "/admin/sous-service-faqs", icon: FileQuestion },
      { label: "Accompagnements", href: "/admin/accompagnements", icon: Users },
    ],
  },
];

export default function Sidebar({ open, onClose }) {
  const logout = useAuthStore((state) => state.logout);

  const linkClass = ({ isActive }) =>
    `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors ${
      isActive
        ? "bg-primary text-primary-foreground"
        : "text-foreground/70 hover:bg-muted hover:text-primary"
    }`;

  return (
    <>
      {open ? (
        <button
          className="fixed inset-0 z-40 bg-foreground/30 lg:hidden"
          type="button"
          aria-label="Fermer le menu"
          onClick={onClose}
        />
      ) : null}

      <aside
        className={`fixed inset-y-0 left-0 z-50 flex w-80 max-w-[86vw] flex-col border-r border-border bg-card shadow-lg transition-transform duration-300 lg:static lg:z-auto lg:w-72 lg:translate-x-0 lg:shadow-none ${
          open ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-20 items-center justify-between border-b border-border px-5">
          <NavLink to="/admin" className="flex items-center gap-3" onClick={onClose}>
            <img
              src="/images/Logo_HWC-J-9-1DQr.png"
              alt="Harmony Works Consulting"
              className="h-11 w-auto object-contain"
            />
          </NavLink>
          <button
            className="rounded-lg p-2 text-muted-foreground hover:bg-muted lg:hidden"
            type="button"
            aria-label="Fermer le menu"
            onClick={onClose}
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <nav className="flex-1 overflow-y-auto px-4 py-5">
          <div className="space-y-6">
            {groups.map((group) => (
              <div key={group.label}>
                <p className="mb-2 px-3 text-xs font-bold uppercase tracking-wider text-muted-foreground">
                  {group.label}
                </p>
                <div className="space-y-1">
                  {group.items.map((item) => {
                    const Icon = item.icon;
                    return (
                      <NavLink
                        key={item.href}
                        to={item.href}
                        end={item.href === "/admin"}
                        className={linkClass}
                        onClick={onClose}
                      >
                        <Icon className="h-4 w-4 shrink-0" />
                        <span className="min-w-0 truncate">{item.label}</span>
                      </NavLink>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
        </nav>

        <div className="border-t border-border p-4">
          <button
            className="flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-semibold text-foreground/70 transition-colors hover:bg-muted hover:text-primary"
            type="button"
            onClick={logout}
          >
            <LogOut className="h-4 w-4" />
            Déconnexion
          </button>
        </div>
      </aside>
    </>
  );
}
