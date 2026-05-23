import { Edit, Plus, Trash2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import {
  createRecommendationRule,
  deleteRecommendationRule,
  listDiagnosticCategories,
  listRecommendationRules,
  updateRecommendationRule,
} from "../../api/adminRecommendationRulesApi.js";
import { listAdminResource } from "../../api/adminCrudApi.js";
import Button from "../../components/ui/Button.jsx";
import Card, { CardContent, CardDescription, CardHeader, CardTitle } from "../../components/ui/Card.jsx";
import ConfirmDialog from "../../components/ui/ConfirmDialog.jsx";
import Input from "../../components/ui/Input.jsx";
import Modal from "../../components/ui/Modal.jsx";
import Select from "../../components/ui/Select.jsx";
import Table from "../../components/ui/Table.jsx";
import Textarea from "../../components/ui/Textarea.jsx";

const emptyRule = {
  categorieId: "",
  seuilScore: "50",
  operateur: "<",
  serviceHwcId: "",
  sousServiceHwcId: "",
  titreRecommandation: "",
  descriptionRecommandation: "",
  horizon: "COURT_TERME",
  impactEstime: "",
  kpisSuggeres: "",
  priorite: "1",
  actif: true,
};

function asPage(data) {
  return Array.isArray(data) ? data : data?.content ?? [];
}

function toOptions(items, labelKey = "titre") {
  return items.map((item) => ({
    value: item.id,
    label: item[labelKey] ?? item.nom ?? item.titre ?? `#${item.id}`,
  }));
}

function toPayload(values) {
  return {
    ...values,
    categorieId: Number(values.categorieId),
    seuilScore: Number(values.seuilScore),
    serviceHwcId: values.serviceHwcId ? Number(values.serviceHwcId) : null,
    sousServiceHwcId: values.sousServiceHwcId ? Number(values.sousServiceHwcId) : null,
    priorite: Number(values.priorite),
    actif: Boolean(values.actif),
  };
}

export default function ReglesRecommandationPage() {
  const [rules, setRules] = useState([]);
  const [categories, setCategories] = useState([]);
  const [services, setServices] = useState([]);
  const [sousServices, setSousServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [formOpen, setFormOpen] = useState(false);
  const [editingRule, setEditingRule] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [values, setValues] = useState(emptyRule);

  async function loadData() {
    setLoading(true);
    try {
      const [rulesData, categoriesData, servicesData, sousServicesData] = await Promise.all([
        listRecommendationRules(),
        listDiagnosticCategories(),
        listAdminResource("services", { page: 0, size: 100, sort: "id,desc" }),
        listAdminResource("sous-services", { page: 0, size: 100, sort: "id,desc" }),
      ]);
      setRules(rulesData);
      setCategories(categoriesData);
      setServices(asPage(servicesData));
      setSousServices(asPage(sousServicesData));
    } catch {
      toast.error("Impossible de charger les regles de recommandation.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  const categoryOptions = useMemo(() => toOptions(categories, "nom"), [categories]);
  const serviceOptions = useMemo(() => toOptions(services), [services]);
  const sousServiceOptions = useMemo(() => toOptions(sousServices), [sousServices]);

  function openCreate() {
    setEditingRule(null);
    setValues(emptyRule);
    setFormOpen(true);
  }

  function openEdit(rule) {
    setEditingRule(rule);
    setValues({
      categorieId: rule.categorieId ?? "",
      seuilScore: rule.seuilScore ?? "",
      operateur: rule.operateur ?? "<",
      serviceHwcId: rule.serviceHwcId ?? "",
      sousServiceHwcId: rule.sousServiceHwcId ?? "",
      titreRecommandation: rule.titreRecommandation ?? "",
      descriptionRecommandation: rule.descriptionRecommandation ?? "",
      horizon: rule.horizon ?? "COURT_TERME",
      impactEstime: rule.impactEstime ?? "",
      kpisSuggeres: rule.kpisSuggeres ?? "",
      priorite: rule.priorite ?? "1",
      actif: rule.actif,
    });
    setFormOpen(true);
  }

  function updateValue(name, value) {
    setValues((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!values.categorieId || !values.titreRecommandation || !values.seuilScore || !values.priorite) {
      toast.error("Categorie, seuil, titre et priorite sont obligatoires.");
      return;
    }

    setSaving(true);
    try {
      if (editingRule) {
        await updateRecommendationRule(editingRule.id, toPayload(values));
        toast.success("Regle modifiee.");
      } else {
        await createRecommendationRule(toPayload(values));
        toast.success("Regle creee.");
      }
      setFormOpen(false);
      await loadData();
    } catch {
      toast.error("Impossible d'enregistrer la regle.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (!deleteTarget) {
      return;
    }
    setSaving(true);
    try {
      await deleteRecommendationRule(deleteTarget.id);
      toast.success("Regle supprimee.");
      setDeleteTarget(null);
      await loadData();
    } catch {
      toast.error("Impossible de supprimer la regle.");
    } finally {
      setSaving(false);
    }
  }

  const columns = [
    { key: "categorieNom", header: "Categorie" },
    {
      key: "condition",
      header: "Condition",
      render: (rule) => `${rule.operateur} ${rule.seuilScore}`,
    },
    {
      key: "titreRecommandation",
      header: "Recommandation",
      render: (rule) => (
        <div>
          <p className="font-semibold">{rule.titreRecommandation}</p>
          <p className="text-xs text-muted-foreground">{rule.horizon}</p>
        </div>
      ),
    },
    {
      key: "serviceHwcNom",
      header: "Service HWC",
      render: (rule) => rule.serviceHwcNom ?? <span className="text-muted-foreground">-</span>,
    },
    {
      key: "priorite",
      header: "Priorite",
      render: (rule) => `P${rule.priorite}`,
    },
    {
      key: "actif",
      header: "Statut",
      render: (rule) => (
        <span className={`rounded-full px-2.5 py-1 text-xs font-bold ${rule.actif ? "bg-green-50 text-green-700" : "bg-red-50 text-red-700"}`}>
          {rule.actif ? "Active" : "Inactive"}
        </span>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <Card>
        <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
          <CardHeader className="mb-0">
            <CardTitle>Regles de recommandation</CardTitle>
            <CardDescription>
              Configurez les conditions qui transforment les scores diagnostic en recommandations client.
            </CardDescription>
          </CardHeader>
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            Ajouter
          </Button>
        </div>
      </Card>

      <Card>
        <CardContent>
          <Table
            columns={columns}
            data={rules}
            emptyText="Aucune regle configuree."
            loading={loading}
            rowActions={(rule) => (
              <div className="flex justify-end gap-2">
                <Button aria-label="Modifier" onClick={() => openEdit(rule)} size="icon" variant="outline">
                  <Edit className="h-4 w-4" />
                </Button>
                <Button aria-label="Supprimer" onClick={() => setDeleteTarget(rule)} size="icon" variant="danger">
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            )}
          />
        </CardContent>
      </Card>

      <Modal
        description="Une regle active genere une recommandation quand la condition du score est respectee."
        onClose={() => setFormOpen(false)}
        open={formOpen}
        title={editingRule ? "Modifier la regle" : "Ajouter une regle"}
      >
        <form className="space-y-4" onSubmit={handleSubmit}>
          <Select
            id="categorieId"
            label="Categorie diagnostic"
            onChange={(event) => updateValue("categorieId", event.target.value)}
            options={categoryOptions}
            placeholder="Selectionner"
            value={values.categorieId}
          />
          <div className="grid gap-4 md:grid-cols-3">
            <Select
              id="operateur"
              label="Operateur"
              onChange={(event) => updateValue("operateur", event.target.value)}
              options={[
                { value: "<", label: "<" },
                { value: "<=", label: "<=" },
                { value: ">", label: ">" },
                { value: ">=", label: ">=" },
                { value: "=", label: "=" },
              ]}
              value={values.operateur}
            />
            <Input
              id="seuilScore"
              label="Seuil score"
              onChange={(event) => updateValue("seuilScore", event.target.value)}
              type="number"
              value={values.seuilScore}
            />
            <Input
              id="priorite"
              label="Priorite"
              onChange={(event) => updateValue("priorite", event.target.value)}
              type="number"
              value={values.priorite}
            />
          </div>
          <Input
            id="titreRecommandation"
            label="Titre recommandation"
            onChange={(event) => updateValue("titreRecommandation", event.target.value)}
            value={values.titreRecommandation}
          />
          <Textarea
            id="descriptionRecommandation"
            label="Description"
            onChange={(event) => updateValue("descriptionRecommandation", event.target.value)}
            rows={4}
            value={values.descriptionRecommandation}
          />
          <div className="grid gap-4 md:grid-cols-2">
            <Select
              id="horizon"
              label="Horizon"
              onChange={(event) => updateValue("horizon", event.target.value)}
              options={[
                { value: "COURT_TERME", label: "Court terme" },
                { value: "MOYEN_TERME", label: "Moyen terme" },
                { value: "LONG_TERME", label: "Long terme" },
              ]}
              value={values.horizon}
            />
            <Input
              id="impactEstime"
              label="Impact estime"
              onChange={(event) => updateValue("impactEstime", event.target.value)}
              value={values.impactEstime}
            />
          </div>
          <div className="grid gap-4 md:grid-cols-2">
            <Select
              id="serviceHwcId"
              label="Service HWC"
              onChange={(event) => updateValue("serviceHwcId", event.target.value)}
              options={serviceOptions}
              placeholder="Aucun"
              value={values.serviceHwcId}
            />
            <Select
              id="sousServiceHwcId"
              label="Sous-service HWC"
              onChange={(event) => updateValue("sousServiceHwcId", event.target.value)}
              options={sousServiceOptions}
              placeholder="Aucun"
              value={values.sousServiceHwcId}
            />
          </div>
          <Textarea
            helperText="Separez les KPI par point-virgule."
            id="kpisSuggeres"
            label="KPI suggeres"
            onChange={(event) => updateValue("kpisSuggeres", event.target.value)}
            rows={3}
            value={values.kpisSuggeres}
          />
          <label className="flex items-center gap-3 rounded-lg border border-border p-3 text-sm font-semibold">
            <input
              checked={values.actif}
              onChange={(event) => updateValue("actif", event.target.checked)}
              type="checkbox"
            />
            Regle active
          </label>

          <div className="flex justify-end gap-3 pt-2">
            <Button disabled={saving} onClick={() => setFormOpen(false)} variant="outline">
              Annuler
            </Button>
            <Button loading={saving} type="submit">
              Enregistrer
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        confirmLabel="Supprimer"
        description="Cette regle ne generera plus de nouvelles recommandations."
        loading={saving}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        open={Boolean(deleteTarget)}
        title="Supprimer cette regle ?"
      />
    </div>
  );
}
