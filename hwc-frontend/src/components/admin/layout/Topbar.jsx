import { Menu, UserCircle } from "lucide-react";
import { useMemo } from "react";
import { useLocation } from "react-router-dom";
import useAuthStore from "../../../store/authStore.js";

const titles = {
  "/admin": "Accueil dashboard",
  "/admin/services": "Gestion des services",
  "/admin/sous-services": "Gestion des sous-services",
  "/admin/etiquettes": "Gestion des étiquettes",
  "/admin/temoignages": "Gestion des témoignages",
  "/admin/certifications": "Gestion des certifications",
  "/admin/pays": "Gestion des pays",
  "/admin/chiffres-cles": "Gestion des chiffres clés",
  "/admin/clients-confiance": "Clients de confiance",
  "/admin/demandes-contact": "Demandes de contact",
  "/admin/service-fonctionnalites": "Fonctionnalités services",
  "/admin/service-images": "Images services",
  "/admin/sous-service-fonctionnalites": "Fonctionnalités sous-services",
  "/admin/sous-service-avantages": "Avantages sous-services",
  "/admin/sous-service-etapes": "Étapes sous-services",
  "/admin/sous-service-faqs": "FAQs sous-services",
  "/admin/accompagnements": "Accompagnements",
};

export default function Topbar({ onMenuClick }) {
  const location = useLocation();
  const user = useAuthStore((state) => state.user);

  const title = useMemo(() => titles[location.pathname] ?? "Dashboard admin", [location.pathname]);

  return (
    <header className="sticky top-0 z-30 border-b border-border bg-card/95 backdrop-blur-lg">
      <div className="flex h-16 items-center justify-between gap-4 px-4 md:px-6">
        <div className="flex min-w-0 items-center gap-3">
          <button
            className="rounded-lg p-2 text-muted-foreground transition-colors hover:bg-muted hover:text-primary lg:hidden"
            type="button"
            aria-label="Ouvrir le menu"
            onClick={onMenuClick}
          >
            <Menu className="h-5 w-5" />
          </button>
          <div className="min-w-0">
            <h1 className="truncate text-lg font-semibold text-foreground md:text-xl">
              {title}
            </h1>
            <p className="hidden text-xs text-muted-foreground sm:block">
              Administration du contenu HWC
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3 rounded-full border border-border bg-background px-3 py-2">
          <UserCircle className="h-5 w-5 text-secondary" />
          <span className="max-w-[150px] truncate text-sm font-semibold text-foreground">
            {user?.prenom || user?.email || "Admin"}
          </span>
        </div>
      </div>
    </header>
  );
}
