import { Check, ChevronDown, Circle, Clock3, Edit3, ExternalLink, Eye, Globe2, Loader2, Save, Sparkles, X } from "lucide-react";
import { useState } from "react";

const STEPS = [
  ["COMPANY_DISCOVERY", "Entreprise"], ["OBJECTIVES", "Objectif"], ["CURRENT_AUDIT", "Audit"],
  ["TARGET_AUDIENCE", "Cible"], ["POSITIONING", "Positionnement"], ["CHANNEL_SELECTION", "Canaux"],
  ["BUDGET_AND_RESOURCES", "Ressources"], ["ACTION_PLAN", "Plan d'action"], ["KPI_SELECTION", "KPI"],
  ["FINAL_DELIVERABLE", "Livrable"],
];

const TYPE_STYLES = {
  DATA: "bg-sky-50 text-sky-700 border-sky-200",
  HYPOTHESIS: "bg-amber-50 text-amber-700 border-amber-200",
  RECOMMENDATION: "bg-violet-50 text-violet-700 border-violet-200",
};

const TYPE_LABELS = { DATA: "Donnée", HYPOTHESIS: "À confirmer", RECOMMENDATION: "Recommandation" };

const REMAINING_EXCHANGES = {
  COMPANY_DISCOVERY: 10, OBJECTIVES: 8, CURRENT_AUDIT: 7, TARGET_AUDIENCE: 6, POSITIONING: 5,
  CHANNEL_SELECTION: 4, BUDGET_AND_RESOURCES: 3, ACTION_PLAN: 2, KPI_SELECTION: 1,
  FINAL_DELIVERABLE: 0, COMPLETED: 0,
};

const compact = (value) => {
  if (value == null || value === "" || (Array.isArray(value) && value.length === 0)) return "En cours de définition";
  if (Array.isArray(value)) return value.map((item) => typeof item === "object" ? item.name ?? JSON.stringify(item) : item).join(", ");
  if (typeof value === "object") return Object.values(value).filter(Boolean).join(" · ") || "En cours de définition";
  return String(value);
};

const editableValue = (value) => {
  if (Array.isArray(value)) return value.every((item) => typeof item !== "object") ? value.join(", ") : JSON.stringify(value, null, 2);
  if (value && typeof value === "object") return JSON.stringify(value, null, 2);
  return value == null ? "" : String(value);
};

const parseValue = (raw, original) => {
  if (Array.isArray(original)) {
    if (original.some((item) => typeof item === "object")) return JSON.parse(raw);
    return raw.split(",").map((item) => item.trim()).filter(Boolean);
  }
  if (original && typeof original === "object") return JSON.parse(raw);
  if (typeof original === "boolean") return raw === "true";
  if (typeof original === "number") return Number(raw);
  return raw.trim();
};

function StrategyField({ label, path, value, type = "DATA", onCorrect, disabled }) {
  const [editing, setEditing] = useState(false);
  const [draft, setDraft] = useState("");
  const [saving, setSaving] = useState(false);
  const missing = value == null || value === "" || (Array.isArray(value) && value.length === 0);

  const save = async () => {
    setSaving(true);
    try {
      await onCorrect(path, parseValue(draft, value));
      setEditing(false);
    } catch {
      // Le parent affiche le détail de l'erreur et conserve l'éditeur ouvert.
    } finally {
      setSaving(false);
    }
  };

  return <div className="rounded-xl border border-slate-100 bg-slate-50/70 p-3">
    <div className="flex items-start justify-between gap-2">
      <div className="min-w-0 flex-1">
        <div className="flex flex-wrap items-center gap-2">
          <p className="text-xs font-bold text-slate-500">{label}</p>
          <span className={`rounded-full border px-2 py-0.5 text-[9px] font-bold uppercase tracking-wide ${TYPE_STYLES[type]}`}>{TYPE_LABELS[type]}</span>
        </div>
        {editing ? <textarea autoFocus value={draft} onChange={(event) => setDraft(event.target.value)} rows={3} className="mt-2 w-full rounded-lg border border-primary/30 bg-white p-2 text-xs outline-none focus:border-primary" />
          : <p className={`mt-1 break-words text-xs leading-5 ${missing ? "italic text-slate-400" : "font-medium text-slate-700"}`}>{compact(value)}</p>}
      </div>
      {editing ? <div className="flex gap-1"><button type="button" disabled={saving} onClick={() => void save()} className="rounded-lg p-1.5 text-primary hover:bg-primary/10">{saving ? <Loader2 className="h-3.5 w-3.5 animate-spin" /> : <Save className="h-3.5 w-3.5" />}</button><button type="button" onClick={() => setEditing(false)} className="rounded-lg p-1.5 text-slate-400 hover:bg-slate-200"><X className="h-3.5 w-3.5" /></button></div>
        : <button type="button" disabled={disabled} onClick={() => { setDraft(editableValue(value)); setEditing(true); }} title={`Corriger ${label}`} className="rounded-lg p-1.5 text-slate-400 hover:bg-white hover:text-primary disabled:opacity-40"><Edit3 className="h-3.5 w-3.5" /></button>}
    </div>
  </div>;
}

export default function MarketingStrategyPanel({ state, onCorrect, onDecidePublicFinding, disabled, celebrationsEnabled, onToggleCelebrations, onResearchPublicPresence, researchingPresence }) {
  const [open, setOpen] = useState(false);
  const stage = state?.stage === "COMPLETED" ? "FINAL_DELIVERABLE" : state?.stage;
  const activeIndex = Math.max(0, STEPS.findIndex(([key]) => key === stage));
  const fields = [
    ["Entreprise", "company.name", state?.company?.name, "DATA"],
    ["Activité", "company.sector", state?.company?.sector, "DATA"],
    ["Offres", "company.productsOrServices", state?.company?.productsOrServices, "DATA"],
    ["Objectif SMART", "objectives.smartStatement", state?.objectives?.smartStatement, "DATA"],
    ["Cible prioritaire", "targetAudience.primaryPersona", state?.targetAudience?.primaryPersona, "DATA"],
    ["Proposition de valeur", "positioning.valueProposition", state?.positioning?.valueProposition, "DATA"],
    ["Canaux retenus", "recommendedChannels", state?.recommendedChannels, "RECOMMENDATION"],
    ["Budget mensuel", "budget.monthlyAmount", state?.budget?.monthlyAmount, "DATA"],
    ["Plan d'action", "weeklyActions", state?.weeklyActions, "RECOMMENDATION"],
    ["KPI", "kpis", state?.kpis, "RECOMMENDATION"],
  ];
  const remaining = REMAINING_EXCHANGES[state?.stage] ?? 0;
  const revealedSections = fields.filter(([, , value]) => value != null && value !== "" && (!Array.isArray(value) || value.length > 0)).length;

  return <aside className="rounded-3xl border border-[#dbe8e8] bg-white shadow-card xl:sticky xl:top-6 xl:max-h-[calc(100vh-3rem)] xl:overflow-y-auto">
    <button type="button" onClick={() => setOpen((value) => !value)} className="flex w-full items-center justify-between p-5 text-left xl:pointer-events-none">
      <div><p className="text-xs font-bold uppercase tracking-wider text-primary">Aperçu progressif</p><h2 className="mt-1 font-extrabold">Stratégie en construction</h2></div>
      <ChevronDown className={`h-5 w-5 transition xl:hidden ${open ? "rotate-180" : ""}`} />
    </button>
    <div className={`${open ? "block" : "hidden"} border-t border-slate-100 p-5 xl:block`}>
      <ol className="space-y-2">
        {STEPS.map(([key, label], index) => {
          const completed = state?.stage === "COMPLETED" || index < activeIndex;
          const active = index === activeIndex && state?.stage !== "COMPLETED";
          return <li key={key} className={`flex items-center gap-2 rounded-lg px-2 py-1.5 text-xs ${active ? "bg-primary/10 font-bold text-primary" : completed ? "text-emerald-700" : "text-slate-400"}`}>
            {completed ? <Check className="h-3.5 w-3.5" /> : active ? <Circle className="h-3.5 w-3.5 fill-current" /> : <Circle className="h-3.5 w-3.5" />}{label}
          </li>;
        })}
      </ol>
      <div className="mt-4 rounded-xl border border-primary/15 bg-primary/5 p-3">
        <p className="flex items-center gap-2 text-xs font-bold text-primary"><Clock3 className="h-4 w-4" /> Effort restant estimé</p>
        <p className="mt-1 text-xs leading-5 text-slate-600">{remaining > 0 ? `Encore environ ${remaining} échange${remaining > 1 ? "s" : ""} avant votre livrable.` : state?.stage === "COMPLETED" ? "Votre stratégie est finalisée et disponible pour le suivi." : "Votre livrable peut maintenant être préparé."}</p>
        <p className="mt-1 text-[10px] text-slate-400">Estimation indicative, adaptée à la richesse de vos réponses.</p>
      </div>
      <button type="button" onClick={onToggleCelebrations} className="mt-3 flex w-full items-center justify-between rounded-xl border border-slate-100 px-3 py-2 text-left text-xs text-slate-500"><span className="flex items-center gap-2"><Sparkles className="h-3.5 w-3.5" /> Micro-célébrations</span><span className={`rounded-full px-2 py-0.5 text-[10px] font-bold ${celebrationsEnabled ? "bg-emerald-100 text-emerald-700" : "bg-slate-100 text-slate-500"}`}>{celebrationsEnabled ? "Activées" : "Désactivées"}</span></button>
      <button type="button" disabled={disabled || researchingPresence || !state?.company?.name} onClick={() => void onResearchPublicPresence()} className="mt-3 flex w-full items-center justify-center gap-2 rounded-xl border border-sky-200 bg-sky-50 px-3 py-2.5 text-xs font-bold text-sky-700 disabled:cursor-not-allowed disabled:opacity-50">{researchingPresence ? <Loader2 className="h-4 w-4 animate-spin" /> : <Globe2 className="h-4 w-4" />} Vérifier la présence publique</button>
      {state?.publicWebFindings?.length > 0 ? <div className="mt-3 rounded-xl border border-amber-200 bg-amber-50 p-3"><p className="text-[10px] font-bold uppercase tracking-wide text-amber-700">Informations publiques à confirmer</p><ul className="mt-2 space-y-3">{state.publicWebFindings.map((finding, index) => <li key={`${finding.url}-${finding.claim}`} className="rounded-lg bg-white/70 p-2 text-xs leading-5 text-amber-900"><p>{finding.claim}<a href={finding.url} target="_blank" rel="noreferrer" className="ml-1 inline-flex items-center gap-1 font-bold text-amber-700 underline">{finding.sourceTitle}<ExternalLink className="h-3 w-3" /></a></p>{finding.proposedPath ? <div className="mt-2 flex gap-2"><button type="button" disabled={disabled} onClick={() => onDecidePublicFinding(index, true)} className="rounded-lg bg-primary px-3 py-1.5 font-bold text-white disabled:opacity-50">Confirmer</button><button type="button" disabled={disabled} onClick={() => onDecidePublicFinding(index, false)} className="rounded-lg border border-amber-300 px-3 py-1.5 font-bold text-amber-800 disabled:opacity-50">Écarter</button></div> : <button type="button" disabled={disabled} onClick={() => onDecidePublicFinding(index, false)} className="mt-2 rounded-lg border border-amber-300 px-3 py-1.5 font-bold text-amber-800 disabled:opacity-50">Masquer</button>}</li>)}</ul></div> : null}
      <div className="my-5 border-t border-slate-100" />
      <div className="mb-3 flex items-center justify-between"><div><p className="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-primary"><Eye className="h-4 w-4" /> Aperçu du livrable</p><p className="mt-1 text-[11px] text-slate-400">{revealedSections} section{revealedSections > 1 ? "s" : ""} déjà révélée{revealedSections > 1 ? "s" : ""}</p></div><span className="rounded-full bg-primary/10 px-2 py-1 text-[10px] font-bold text-primary">{Math.round((revealedSections / fields.length) * 100)}%</span></div>
      <div className="space-y-2">{fields.map(([label, path, value, fallbackType]) => <StrategyField key={path} label={label} path={path} value={value} type={state?.informationTypes?.[path] ?? fallbackType} onCorrect={onCorrect} disabled={disabled} />)}</div>
      {state?.assumptions?.length > 0 ? <div className="mt-3 rounded-xl border border-amber-200 bg-amber-50 p-3"><span className={`rounded-full border px-2 py-0.5 text-[9px] font-bold uppercase ${TYPE_STYLES.HYPOTHESIS}`}>À confirmer</span><ul className="mt-2 space-y-1 text-xs text-amber-800">{state.assumptions.map((item) => <li key={item}>• {item}</li>)}</ul></div> : null}
    </div>
  </aside>;
}
