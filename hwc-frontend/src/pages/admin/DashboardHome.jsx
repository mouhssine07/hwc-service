import { useEffect, useState } from "react";
import { BarChart3, BriefcaseBusiness, Globe2, Loader2, MessageSquare } from "lucide-react";
import toast from "react-hot-toast";
import { getDashboardStats } from "../../api/dashboardApi.js";
import useAuthStore from "../../store/authStore.js";

const statsConfig = [
  {
    key: "services",
    label: "Services",
    helper: "Offres principales publiees",
    icon: BriefcaseBusiness,
    tone: "bg-primary/10 text-primary",
  },
  {
    key: "temoignages",
    label: "Temoignages",
    helper: "Preuves sociales disponibles",
    icon: MessageSquare,
    tone: "bg-secondary/10 text-secondary",
  },
  {
    key: "demandesContact",
    label: "Demandes contact",
    helper: "Leads recus via le site",
    icon: BarChart3,
    tone: "bg-accent/10 text-accent-foreground",
  },
  {
    key: "pays",
    label: "Pays",
    helper: "Zones de presence affichees",
    icon: Globe2,
    tone: "bg-muted text-foreground",
  },
];

export default function DashboardHome() {
  const user = useAuthStore((state) => state.user);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let ignore = false;

    async function loadStats() {
      try {
        const data = await getDashboardStats();
        if (!ignore) {
          setStats(data);
        }
      } catch (error) {
        if (!ignore) {
          toast.error("Impossible de charger les statistiques du dashboard.");
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    }

    loadStats();

    return () => {
      ignore = true;
    };
  }, []);

  return (
    <div className="space-y-6">
      <section className="rounded-lg border border-border bg-card p-5 shadow-sm md:p-6">
        <p className="text-sm font-semibold uppercase tracking-wider text-secondary">
          Dashboard Admin
        </p>
        <h2 className="mt-2 font-display text-2xl font-bold text-foreground md:text-3xl">
          Bienvenue {user?.prenom || user?.email || "Admin"}
        </h2>
        <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground md:text-base">
          Vue rapide sur les contenus importants du site HWC. Les ecrans de gestion
          detailles seront branches dans les prochaines phases.
        </p>
      </section>

      <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {statsConfig.map((item) => {
          const Icon = item.icon;
          const value = stats?.[item.key] ?? 0;

          return (
            <article key={item.key} className="rounded-lg border border-border bg-card p-5 shadow-sm">
              <div className="mb-4 flex items-center justify-between gap-3">
                <div className={`flex h-11 w-11 items-center justify-center rounded-lg ${item.tone}`}>
                  <Icon className="h-5 w-5" />
                </div>
                {loading ? (
                  <Loader2 className="h-5 w-5 animate-spin text-muted-foreground" />
                ) : null}
              </div>
              <p className="text-sm font-medium text-muted-foreground">{item.label}</p>
              <p className="mt-2 text-3xl font-bold text-foreground">{loading ? "-" : value}</p>
              <p className="mt-2 text-sm text-muted-foreground">{item.helper}</p>
            </article>
          );
        })}
      </section>

      <section className="rounded-lg border border-border bg-card p-5 shadow-sm md:p-6">
        <h3 className="text-lg font-semibold text-foreground">Prochaine construction</h3>
        <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
          Le layout est pret pour recevoir les pages CRUD : services, temoignages,
          pays, chiffres cles, clients et demandes de contact.
        </p>
      </section>
    </div>
  );
}
