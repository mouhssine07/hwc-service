import { Edit, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import {
  createAdminResource,
  deleteAdminResource,
  listAdminResource,
  updateAdminResource,
} from "../../../api/adminCrudApi.js";
import Button from "../../../components/ui/Button.jsx";
import Card, { CardContent, CardDescription, CardHeader, CardTitle } from "../../../components/ui/Card.jsx";
import ConfirmDialog from "../../../components/ui/ConfirmDialog.jsx";
import EmptyState from "../../../components/ui/EmptyState.jsx";
import Input from "../../../components/ui/Input.jsx";
import Modal from "../../../components/ui/Modal.jsx";
import Pagination from "../../../components/ui/Pagination.jsx";
import Table from "../../../components/ui/Table.jsx";
import AdminEntityForm from "./AdminEntityForm.jsx";

const DEFAULT_SIZE = 10;

function asPage(data, fallbackPage, fallbackSize) {
  if (Array.isArray(data)) {
    return {
      content: data,
      number: fallbackPage,
      size: fallbackSize,
      totalElements: data.length,
      totalPages: Math.max(1, Math.ceil(data.length / fallbackSize)),
    };
  }

  return {
    content: data?.content ?? [],
    number: data?.number ?? fallbackPage,
    size: data?.size ?? fallbackSize,
    totalElements: data?.totalElements ?? data?.content?.length ?? 0,
    totalPages: data?.totalPages ?? 1,
  };
}

function getErrorMessage(error) {
  return error.response?.data?.message ?? error.response?.data?.error ?? "Operation impossible pour le moment.";
}

export default function AdminCrudPage({ config, formComponent: FormComponent = AdminEntityForm }) {
  const [pageData, setPageData] = useState(() => asPage([], 0, DEFAULT_SIZE));
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [query, setQuery] = useState("");
  const [editingItem, setEditingItem] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [formOpen, setFormOpen] = useState(false);
  const [options, setOptions] = useState({});

  async function loadPage(targetPage = page) {
    setLoading(true);
    try {
      const data = await listAdminResource(config.resource, {
        page: targetPage,
        size: DEFAULT_SIZE,
        sort: "id,desc",
      });
      setPageData(asPage(data, targetPage, DEFAULT_SIZE));
    } catch (error) {
      toast.error(getErrorMessage(error));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadPage(page);
  }, [config.resource, page]);

  useEffect(() => {
    const optionFields = config.fields.filter((field) => field.optionsResource);
    if (optionFields.length === 0) {
      setOptions({});
      return;
    }

    let ignore = false;

    async function loadOptions() {
      const nextOptions = {};
      await Promise.all(
        optionFields.map(async (field) => {
          const data = await listAdminResource(field.optionsResource, { page: 0, size: 100, sort: "id,desc" });
          const pageResult = asPage(data, 0, 100);
          nextOptions[field.name] = pageResult.content.map((item) => ({
            value: item.id,
            label: item[field.optionLabelKey] ?? item.nom ?? item.titre ?? `#${item.id}`,
          }));
        }),
      );

      if (!ignore) {
        setOptions(nextOptions);
      }
    }

    loadOptions().catch(() => toast.error("Impossible de charger les listes de selection."));

    return () => {
      ignore = true;
    };
  }, [config.fields]);

  const filteredRows = useMemo(() => {
    const rows = pageData.content;
    const normalizedQuery = query.trim().toLowerCase();
    if (!normalizedQuery) {
      return rows;
    }

    return rows.filter((row) =>
      config.searchFields.some((field) => String(row[field] ?? "").toLowerCase().includes(normalizedQuery)),
    );
  }, [config.searchFields, pageData.content, query]);

  function openCreate() {
    setEditingItem(null);
    setFormOpen(true);
  }

  function openEdit(item) {
    setEditingItem(item);
    setFormOpen(true);
  }

  async function handleSubmit(payload) {
    setSaving(true);
    try {
      if (editingItem) {
        await updateAdminResource(config.resource, editingItem.id, payload);
        toast.success("Element modifie avec succes.");
      } else {
        await createAdminResource(config.resource, payload);
        toast.success("Element cree avec succes.");
      }
      setFormOpen(false);
      setEditingItem(null);
      await loadPage(page);
    } catch (error) {
      toast.error(getErrorMessage(error));
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (!deleteTarget) {
      return;
    }

    setDeleting(true);
    try {
      await deleteAdminResource(config.resource, deleteTarget.id);
      toast.success("Element supprime avec succes.");
      setDeleteTarget(null);
      await loadPage(page);
    } catch (error) {
      toast.error(getErrorMessage(error));
    } finally {
      setDeleting(false);
    }
  }

  const columns = config.columns.map((column) => ({
    ...column,
    render: (row) => {
      const value = row[column.key];
      if (value === null || value === undefined || value === "") {
        return <span className="text-muted-foreground">-</span>;
      }
      return <span className="line-clamp-2">{String(value)}</span>;
    },
  }));

  return (
    <div className="space-y-6">
      <Card>
        <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
          <CardHeader className="mb-0">
            <CardTitle>{config.title}</CardTitle>
            <CardDescription>{config.description}</CardDescription>
          </CardHeader>
          {!config.readOnly ? (
            <Button onClick={openCreate}>
              <Plus className="h-4 w-4" />
              Ajouter
            </Button>
          ) : null}
        </div>
      </Card>

      <Card>
        <CardContent className="space-y-4">
          <div className="max-w-md">
            <Input
              id={`${config.resource}-search`}
              label="Recherche"
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Rechercher dans la page courante"
              value={query}
            />
          </div>

          {!loading && pageData.totalElements === 0 ? (
            <EmptyState
              action={!config.readOnly ? { label: "Ajouter", onClick: openCreate } : undefined}
              description={`Aucun ${config.singular} pour le moment.`}
              icon={Search}
              title="Aucune donnee"
            />
          ) : (
            <Table
              columns={columns}
              data={filteredRows}
              emptyText="Aucun resultat ne correspond a la recherche."
              loading={loading}
              rowActions={(row) => (
                <div className="flex justify-end gap-2">
                  {!config.readOnly ? (
                    <Button aria-label="Modifier" onClick={() => openEdit(row)} size="icon" variant="outline">
                      <Edit className="h-4 w-4" />
                    </Button>
                  ) : null}
                  <Button aria-label="Supprimer" onClick={() => setDeleteTarget(row)} size="icon" variant="danger">
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              )}
            />
          )}

          <Pagination
            onPageChange={setPage}
            page={pageData.number}
            size={pageData.size}
            totalElements={pageData.totalElements}
            totalPages={pageData.totalPages}
          />
        </CardContent>
      </Card>

      <Modal
        description={editingItem ? `Modifier cet element ${config.singular}.` : `Creer un nouvel element ${config.singular}.`}
        onClose={() => setFormOpen(false)}
        open={formOpen}
        title={editingItem ? `Modifier ${config.singular}` : `Ajouter ${config.singular}`}
      >
        <FormComponent
          config={config}
          item={editingItem}
          loading={saving}
          onCancel={() => setFormOpen(false)}
          onSubmit={handleSubmit}
          options={options}
        />
      </Modal>

      <ConfirmDialog
        confirmLabel="Supprimer"
        description={`Cette action supprimera definitivement cet element ${config.singular}.`}
        loading={deleting}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        open={Boolean(deleteTarget)}
        title="Confirmer la suppression"
      />
    </div>
  );
}
