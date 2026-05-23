import { ShieldCheck, UserRoundCheck, UserRoundX } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { listAdminClients, updateAdminClientStatus } from "../../api/adminClientsApi.js";
import Button from "../../components/ui/Button.jsx";
import Card, { CardContent, CardDescription, CardHeader, CardTitle } from "../../components/ui/Card.jsx";
import Input from "../../components/ui/Input.jsx";
import Pagination from "../../components/ui/Pagination.jsx";
import Table from "../../components/ui/Table.jsx";

const DEFAULT_SIZE = 10;

function asPage(data, fallbackPage, fallbackSize) {
  return {
    content: data?.content ?? [],
    number: data?.number ?? fallbackPage,
    size: data?.size ?? fallbackSize,
    totalElements: data?.totalElements ?? data?.content?.length ?? 0,
    totalPages: data?.totalPages ?? 1,
  };
}

function formatDate(value) {
  if (!value) {
    return "-";
  }
  return new Intl.DateTimeFormat("fr-FR", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}

export default function ClientsPage() {
  const [pageData, setPageData] = useState(() => asPage(null, 0, DEFAULT_SIZE));
  const [page, setPage] = useState(0);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);

  async function loadClients(targetPage = page) {
    setLoading(true);
    try {
      const data = await listAdminClients({
        page: targetPage,
        size: DEFAULT_SIZE,
        sort: "id,desc",
      });
      setPageData(asPage(data, targetPage, DEFAULT_SIZE));
    } catch {
      toast.error("Impossible de charger les clients.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadClients(page);
  }, [page]);

  const filteredRows = useMemo(() => {
    const normalizedQuery = query.trim().toLowerCase();
    if (!normalizedQuery) {
      return pageData.content;
    }

    return pageData.content.filter((client) =>
      [client.email, client.nom, client.prenom, client.entreprise, client.secteur]
        .some((value) => String(value ?? "").toLowerCase().includes(normalizedQuery)),
    );
  }, [pageData.content, query]);

  const activeCount = pageData.content.filter((client) => client.actif).length;

  async function toggleStatus(client) {
    setUpdatingId(client.id);
    try {
      await updateAdminClientStatus(client.id, !client.actif);
      toast.success(client.actif ? "Client desactive." : "Client active.");
      await loadClients(page);
    } catch {
      toast.error("Impossible de modifier le statut du client.");
    } finally {
      setUpdatingId(null);
    }
  }

  const columns = [
    {
      key: "identity",
      header: "Client",
      render: (client) => (
        <div>
          <p className="font-semibold text-foreground">
            {client.prenom} {client.nom}
          </p>
          <p className="text-xs text-muted-foreground">{client.email}</p>
        </div>
      ),
    },
    {
      key: "entreprise",
      header: "Entreprise",
      render: (client) => (
        <div>
          <p className="font-medium">{client.entreprise || "-"}</p>
          <p className="text-xs text-muted-foreground">{client.secteur || "-"}</p>
        </div>
      ),
    },
    {
      key: "contact",
      header: "Contact",
      render: (client) => client.telephone || <span className="text-muted-foreground">-</span>,
    },
    {
      key: "actif",
      header: "Statut",
      render: (client) => (
        <span
          className={`inline-flex rounded-full px-2.5 py-1 text-xs font-bold ${
            client.actif
              ? "bg-green-50 text-green-700"
              : "bg-red-50 text-red-700"
          }`}
        >
          {client.actif ? "Actif" : "Desactive"}
        </span>
      ),
    },
    {
      key: "dateDerniereConnexion",
      header: "Derniere connexion",
      render: (client) => formatDate(client.dateDerniereConnexion),
    },
  ];

  return (
    <div className="space-y-6">
      <Card>
        <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
          <CardHeader className="mb-0">
            <CardTitle>Clients</CardTitle>
            <CardDescription>
              Consultez les comptes clients inscrits et controlez leur acces a la plateforme.
            </CardDescription>
          </CardHeader>
          <div className="grid grid-cols-2 gap-3 text-sm">
            <div className="rounded-lg border border-border bg-muted/40 px-4 py-3">
              <p className="text-muted-foreground">Page courante</p>
              <p className="mt-1 text-2xl font-bold text-foreground">{pageData.content.length}</p>
            </div>
            <div className="rounded-lg border border-border bg-muted/40 px-4 py-3">
              <p className="text-muted-foreground">Actifs</p>
              <p className="mt-1 text-2xl font-bold text-primary">{activeCount}</p>
            </div>
          </div>
        </div>
      </Card>

      <Card>
        <CardContent className="space-y-4">
          <div className="max-w-md">
            <Input
              id="clients-search"
              label="Recherche"
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Nom, email, entreprise ou secteur"
              value={query}
            />
          </div>

          <Table
            columns={columns}
            data={filteredRows}
            emptyText="Aucun client ne correspond a la recherche."
            loading={loading}
            rowActions={(client) => (
              <Button
                loading={updatingId === client.id}
                onClick={() => toggleStatus(client)}
                size="sm"
                variant={client.actif ? "outline" : "primary"}
              >
                {client.actif ? <UserRoundX className="h-4 w-4" /> : <UserRoundCheck className="h-4 w-4" />}
                {client.actif ? "Desactiver" : "Activer"}
              </Button>
            )}
          />

          <Pagination
            onPageChange={setPage}
            page={pageData.number}
            size={pageData.size}
            totalElements={pageData.totalElements}
            totalPages={pageData.totalPages}
          />
        </CardContent>
      </Card>

      <Card>
        <CardContent className="flex items-start gap-3 text-sm text-muted-foreground">
          <ShieldCheck className="mt-0.5 h-5 w-5 shrink-0 text-primary" />
          <p>
            Les comptes clients sont crees via l'inscription client. L'admin peut surveiller les comptes
            et couper/restaurer l'acces sans modifier les identifiants ou les roles.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
