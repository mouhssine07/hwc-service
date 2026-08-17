import { ArrowLeft, Bot, CheckCircle2, History, ImagePlus, Loader2, Mic, MicOff, PartyPopper, RefreshCw, Send, Sparkles, Target, Trash2, X } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import {
  createMarketingSession,
  addMarketingKpiMeasurement,
  correctMarketingState,
  deleteMarketingSession,
  decideMarketingPublicFinding,
  finalizeMarketingSession,
  getMarketingDeliverable,
  getMarketingMessages,
  getMarketingSession,
  getMarketingKpiDashboard,
  getMarketingFollowUp,
  getMarketingCheckIns,
  listMarketingSessions,
  researchMarketingPublicPresence,
  setMarketingCheckIns,
  sendMarketingMessage,
  updateMarketingAction,
  getMarketingInputSpec,
  reactToMarketingMessage,
} from "../../api/marketingCoachApi.js";
import MarketingStrategyPanel from "../../components/coach/MarketingStrategyPanel.jsx";
import MarketingKpiDashboard from "../../components/coach/MarketingKpiDashboard.jsx";
import MarketingFollowUpDashboard from "../../components/coach/MarketingFollowUpDashboard.jsx";
import MarketingFollowUpChat from "../../components/coach/MarketingFollowUpChat.jsx";
import MarketingStructuredInput from "../../components/coach/MarketingStructuredInput.jsx";

const STORAGE_KEY = "hwc_marketing_strategy_session_id";
const CELEBRATIONS_KEY = "hwc_marketing_coach_celebrations";

const STAGE_LABELS = {
  COMPANY_DISCOVERY: "Entreprise",
  OBJECTIVES: "Objectif",
  CURRENT_AUDIT: "Audit actuel",
  TARGET_AUDIENCE: "Cible",
  POSITIONING: "Positionnement",
  CHANNEL_SELECTION: "Canaux",
  BUDGET_AND_RESOURCES: "Budget et ressources",
  ACTION_PLAN: "Plan d'action",
  KPI_SELECTION: "KPI",
  FINAL_DELIVERABLE: "Livrable",
  COMPLETED: "Terminé",
};

const WORKFLOW_STAGES = Object.keys(STAGE_LABELS);

const withUiState = (state) => ({ ...state, readyToFinalize: state.stage === "FINAL_DELIVERABLE" });

const resumeMessage = (state) => {
  const index = WORKFLOW_STAGES.indexOf(state.stage);
  if (index <= 0) return null;
  if (state.stage === "COMPLETED") {
    return "Bon retour. Votre stratégie est finalisée ; nous pouvons maintenant examiner son exécution, vos résultats et choisir la prochaine priorité.";
  }
  const previous = STAGE_LABELS[WORKFLOW_STAGES[index - 1]];
  const active = STAGE_LABELS[state.stage];
  return `Bon retour. Nous avions terminé l'étape « ${previous} » et nous étions arrivés à « ${active} ». Reprenons exactement à cet endroit.`;
};

const historyWithResume = (state, history) => {
  const welcome = resumeMessage(state);
  const restored = history.map(({ role, content, imageDataUrl, imageName, imageAnnotations }) => ({ role, content, imageDataUrl, imageName, imageAnnotations }));
  restored.forEach((message, index) => {
    if (message.role !== "assistant" || !message.imageAnnotations?.length) return;
    for (let cursor = index - 1; cursor >= 0; cursor -= 1) {
      if (restored[cursor].role === "user" && restored[cursor].imageDataUrl) {
        restored[cursor].imageAnnotations = message.imageAnnotations;
        break;
      }
    }
  });
  return welcome ? [...restored, { role: "assistant", content: welcome, resume: true }] : restored;
};

function AnnotatedMarketingImage({ src, name, annotations = [] }) {
  return <div className="relative mb-3 w-fit max-w-full overflow-hidden rounded-xl">
    <img src={src} alt={name ?? "Image envoyée au Coach"} className="block max-h-72 max-w-full object-contain" />
    {annotations.map((annotation, index) => <div key={`${annotation.label}-${index}`} className="pointer-events-none absolute border-2 border-amber-400 bg-amber-300/10" style={{ left: `${annotation.x * 100}%`, top: `${annotation.y * 100}%`, width: `${annotation.width * 100}%`, height: `${annotation.height * 100}%` }}><span className="absolute -top-6 left-0 max-w-44 truncate rounded bg-amber-400 px-1.5 py-0.5 text-[9px] font-bold text-amber-950 shadow">{annotation.label}</span></div>)}
  </div>;
}

const STAGE_STATUS_MESSAGES = {
  COMPANY_DISCOVERY: ["Je précise le contexte de votre entreprise...", "J'organise les informations sur votre activité...", "Je prépare la prochaine question utile..."],
  OBJECTIVES: ["Je transforme votre priorité en objectif mesurable...", "Je vérifie la précision de votre objectif...", "J'aligne le résultat attendu et l'échéance..."],
  CURRENT_AUDIT: ["J'analyse votre présence Marketing actuelle...", "Je fais le point sur vos actions existantes...", "Je repère les éléments déjà mesurables..."],
  TARGET_AUDIENCE: ["J'affine votre cible prioritaire...", "J'analyse les besoins de votre audience...", "Je distingue votre segment le plus pertinent..."],
  POSITIONING: ["J'analyse votre positionnement...", "Je clarifie votre proposition de valeur...", "Je recherche votre différence la plus convaincante..."],
  CHANNEL_SELECTION: ["Je compare les canaux adaptés à votre contexte...", "J'évalue les canaux compatibles avec votre cible...", "Je vérifie la cohérence de votre sélection..."],
  BUDGET_AND_RESOURCES: ["Je vérifie la faisabilité de la stratégie...", "J'équilibre ambition et ressources disponibles...", "J'analyse votre capacité d'exécution..."],
  ACTION_PLAN: ["Je structure les prochaines actions...", "J'organise votre plan sur quatre semaines...", "Je transforme la stratégie en actions concrètes..."],
  KPI_SELECTION: ["Je relie vos indicateurs à l'objectif...", "Je sélectionne les mesures réellement utiles...", "Je prépare le suivi des résultats..."],
  FINAL_DELIVERABLE: ["Je vérifie la cohérence de votre stratégie...", "Je prépare votre livrable final...", "J'assemble les éléments validés..."],
  COMPLETED: ["J'analyse vos derniers résultats...", "Je prépare la prochaine priorité...", "Je poursuis le suivi de votre stratégie..."],
};

export default function MarketingStrategyCoachPage() {
  const navigate = useNavigate();
  const [session, setSession] = useState(null);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);
  const [deliverable, setDeliverable] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [savedSessions, setSavedSessions] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);
  const [statusMessage, setStatusMessage] = useState("");
  const [celebration, setCelebration] = useState(null);
  const [celebrationsEnabled, setCelebrationsEnabled] = useState(
    () => window.localStorage.getItem(CELEBRATIONS_KEY) !== "false",
  );
  const [researchingPresence, setResearchingPresence] = useState(false);
  const [kpiDashboard, setKpiDashboard] = useState(null);
  const [kpiSaving, setKpiSaving] = useState(false);
  const [checkInsEnabled, setCheckInsEnabled] = useState(true);
  const [followUpMode, setFollowUpMode] = useState(false);
  const [followUpDashboard, setFollowUpDashboard] = useState(null);
  const [followUpChatOpen, setFollowUpChatOpen] = useState(false);
  const [inputSpec, setInputSpec] = useState(null);
  const [structuredInput, setStructuredInput] = useState(null);
  const [listening, setListening] = useState(false);
  const recognitionRef = useRef(null);
  const endRef = useRef(null);
  const previousStageRef = useRef(null);

  const refreshSessions = async () => setSavedSessions(await listMarketingSessions());

  const openSession = async (sessionId) => {
    setLoading(true);
    try {
      const [state, history] = await Promise.all([getMarketingSession(sessionId), getMarketingMessages(sessionId)]);
      window.localStorage.setItem(STORAGE_KEY, sessionId);
      previousStageRef.current = state.stage;
      setSession(withUiState(state));
      setMessages(historyWithResume(state, history));
      setSuggestions([]);
      setFollowUpMode(false);
      setFollowUpDashboard(null);
      setDeliverable(state.stage === "COMPLETED" ? await getMarketingDeliverable(sessionId) : "");
      if (state.stage === "COMPLETED") {
        const [dashboard, enabled] = await Promise.all([getMarketingKpiDashboard(sessionId), getMarketingCheckIns(sessionId)]);
        setKpiDashboard(dashboard);
        setCheckInsEnabled(enabled);
      } else setKpiDashboard(null);
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Impossible d'ouvrir ce suivi.");
    } finally {
      setLoading(false);
    }
  };

  const removeSession = async (item) => {
    if (!window.confirm(`Supprimer définitivement le suivi « ${item.title} » et tout son historique ?`)) return;
    setSending(true);
    try {
      await deleteMarketingSession(item.sessionId);
      const remaining = savedSessions.filter((saved) => saved.sessionId !== item.sessionId);
      setSavedSessions(remaining);
      if (item.sessionId === session?.sessionId) {
        window.localStorage.removeItem(STORAGE_KEY);
        if (remaining.length > 0) await openSession(remaining[0].sessionId);
        else {
          const created = await createMarketingSession();
          window.localStorage.setItem(STORAGE_KEY, created.sessionId);
          const createdState = await getMarketingSession(created.sessionId);
          previousStageRef.current = createdState.stage;
          setSession(withUiState(createdState));
          setMessages([{ role: "assistant", content: created.message }]);
          setDeliverable("");
          await refreshSessions();
        }
      }
      toast.success("Le suivi et son historique ont été supprimés.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Impossible de supprimer ce suivi.");
    } finally {
      setSending(false);
    }
  };

  useEffect(() => {
    let active = true;
    const start = async () => {
      const existingSessions = await listMarketingSessions();
      if (active) setSavedSessions(existingSessions);
      const storedId = window.localStorage.getItem(STORAGE_KEY);
      if (storedId) {
        try {
          const [state, history] = await Promise.all([
            getMarketingSession(storedId),
            getMarketingMessages(storedId),
          ]);
          if (!active) return;
          previousStageRef.current = state.stage;
          setSession(withUiState(state));
          setMessages(historyWithResume(state, history));
          if (state.stage === "COMPLETED") {
            setDeliverable(await getMarketingDeliverable(storedId));
            const [dashboard, enabled] = await Promise.all([getMarketingKpiDashboard(storedId), getMarketingCheckIns(storedId)]);
            setKpiDashboard(dashboard);
            setCheckInsEnabled(enabled);
          }
          return;
        } catch {
          window.localStorage.removeItem(STORAGE_KEY);
        }
      }
      const created = await createMarketingSession();
      if (!active) return;
      window.localStorage.setItem(STORAGE_KEY, created.sessionId);
      const createdState = await getMarketingSession(created.sessionId);
      previousStageRef.current = createdState.stage;
      setSession(withUiState(createdState));
      setMessages([{ role: "assistant", content: created.message }]);
      if (active) await refreshSessions();
    };
    start().catch((error) => toast.error(error.response?.data?.message ?? "Impossible de démarrer le Coach Marketing."))
      .finally(() => active && setLoading(false));
    return () => { active = false; };
  }, []);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  useEffect(() => {
    if (!session?.sessionId || session.stage === "COMPLETED") { setInputSpec(null); return; }
    getMarketingInputSpec(session.sessionId).then(setInputSpec).catch(() => setInputSpec(null));
  }, [session?.sessionId, session?.stage, session?.missingInformation]);

  useEffect(() => () => recognitionRef.current?.stop(), []);

  const toggleVoiceInput = () => {
    if (listening) { recognitionRef.current?.stop(); return; }
    const Recognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!Recognition) { toast.error("La saisie vocale n’est pas prise en charge par ce navigateur."); return; }
    const recognition = new Recognition();
    recognition.lang = "fr-FR";
    recognition.interimResults = true;
    recognition.continuous = false;
    const initial = input;
    recognition.onresult = (event) => {
      const transcript = Array.from(event.results).map((result) => result[0].transcript).join(" ");
      setInput(`${initial}${initial ? " " : ""}${transcript}`);
    };
    recognition.onend = () => setListening(false);
    recognition.onerror = () => { setListening(false); toast.error("La dictée a été interrompue."); };
    recognitionRef.current = recognition;
    setListening(true);
    recognition.start();
  };

  const reactToMessage = async (reaction) => {
    if (!session || sending) return;
    setSending(true);
    try {
      const response = await reactToMarketingMessage(session.sessionId, reaction);
      setSession(withUiState(response.state));
      setMessages((items) => [...items, { role: "assistant", content: response.message, reactionResult: true }]);
      if (reaction !== "CORRECT") setInput(reaction === "CLARIFY" ? "Je souhaite une clarification sur : " : "Voici ma correction : ");
    } catch (error) { toast.error(error.response?.data?.message ?? "La réaction n’a pas pu être enregistrée."); }
    finally { setSending(false); }
  };

  useEffect(() => {
    const currentStage = session?.stage;
    if (!currentStage) return undefined;
    const previousStage = previousStageRef.current;
    previousStageRef.current = currentStage;
    if (!previousStage || previousStage === currentStage || !celebrationsEnabled) return undefined;
    const reducedMotion = window.matchMedia?.("(prefers-reduced-motion: reduce)").matches;
    const completedLabel = STAGE_LABELS[previousStage] ?? previousStage;
    setCelebration({ label: `${completedLabel} défini`, reducedMotion });
    const timeout = window.setTimeout(() => setCelebration(null), 2600);
    return () => window.clearTimeout(timeout);
  }, [session?.stage, celebrationsEnabled]);

  const toggleCelebrations = () => {
    setCelebrationsEnabled((enabled) => {
      const next = !enabled;
      window.localStorage.setItem(CELEBRATIONS_KEY, String(next));
      if (!next) setCelebration(null);
      return next;
    });
  };

  const sendMessage = async (rawMessage) => {
    const message = rawMessage.trim();
    if ((!message && !selectedImage) || !session || sending) return;
    const imageDataUrl = selectedImage ? URL.createObjectURL(selectedImage) : null;
    const effectiveMessage = message || "Analyse cette image dans le contexte de ma stratégie Marketing.";
    setMessages((items) => [...items, { role: "user", content: effectiveMessage, imageDataUrl, imageName: selectedImage?.name }]);
    setInput("");
    setSuggestions([]);
    setSending(true);
    const statuses = STAGE_STATUS_MESSAGES[session.stage] ?? ["J'analyse votre réponse..."];
    setStatusMessage(statuses[Math.floor(Math.random() * statuses.length)]);
    try {
      if (selectedImage) {
        const response = await sendMarketingMessage(session.sessionId, effectiveMessage, selectedImage);
        setSelectedImage(null);
        setSession(withUiState(await getMarketingSession(session.sessionId)));
        setMessages((items) => {
          const annotated = items.map((item, index) => index === items.length - 1 && item.role === "user"
            ? { ...item, imageAnnotations: response.imageAnnotations ?? [] }
            : item);
          return [...annotated, { role: "assistant", content: response.message, imageAnnotations: response.imageAnnotations ?? [] }];
        });
        setSuggestions(response.suggestedReplies ?? []);
      } else {
        const pendingStructured = structuredInput ?? JSON.parse(window.sessionStorage.getItem("hwc_marketing_structured_input") || "null");
        const response = await sendMarketingMessage(session.sessionId, effectiveMessage, null, pendingStructured);
        window.sessionStorage.removeItem("hwc_marketing_structured_input");
        setSession((current) => ({ ...current, ...response }));
        setSuggestions(response.suggestedReplies ?? []);
        setMessages((items) => [...items, { role: "assistant", content: response.message }]);
      }
      setSession(withUiState(await getMarketingSession(session.sessionId)));
      setStructuredInput(null);
      await refreshSessions();
    } catch (error) {
      setInput(message);
      setMessages((items) => items.filter((item) => !(item.role === "assistant" && item.streaming)));
      toast.error(error.response?.data?.message ?? "Le Coach n'a pas pu analyser cette réponse.");
    } finally {
      setStatusMessage("");
      setSending(false);
    }
  };

  const submit = (event) => {
    event.preventDefault();
    void sendMessage(input);
  };

  const restart = async () => {
    if (!window.confirm("Recommencer une nouvelle stratégie ? Votre session actuelle restera sauvegardée.")) return;
    setSending(true);
    try {
      const created = await createMarketingSession();
      window.localStorage.setItem(STORAGE_KEY, created.sessionId);
      const createdState = await getMarketingSession(created.sessionId);
      previousStageRef.current = createdState.stage;
      setSession(withUiState(createdState));
      setMessages([{ role: "assistant", content: created.message }]);
      setDeliverable("");
      setFollowUpMode(false);
      setFollowUpDashboard(null);
      setInput("");
      setSuggestions(created.suggestedReplies ?? []);
      await refreshSessions();
      toast.success("Une nouvelle stratégie a été démarrée.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Impossible de créer une nouvelle session.");
    } finally {
      setSending(false);
    }
  };

  const finalize = async () => {
    setSending(true);
    try {
      const result = await finalizeMarketingSession(session.sessionId);
      setDeliverable(result.markdown);
      setSession((current) => ({ ...current, stage: "COMPLETED", readyToFinalize: false }));
      const [dashboard, enabled] = await Promise.all([getMarketingKpiDashboard(session.sessionId), getMarketingCheckIns(session.sessionId)]);
      setKpiDashboard(dashboard);
      setCheckInsEnabled(enabled);
      await refreshSessions();
      toast.success("Votre mini-stratégie Marketing Digital est prête.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Le livrable ne peut pas encore être généré.");
    } finally {
      setSending(false);
    }
  };

  const correctState = async (path, value) => {
    try {
      const corrected = await correctMarketingState(session.sessionId, path, value);
      setSession(withUiState(corrected));
      toast.success("Information corrigée et historisée.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Cette information n'a pas pu être corrigée.");
      throw error;
    }
  };

  const researchPublicPresence = async () => {
    setResearchingPresence(true);
    try {
      const researched = await researchMarketingPublicPresence(session.sessionId);
      setSession(withUiState(researched));
      toast.success(researched.publicWebFindings?.length
        ? "Présence publique analysée. Les constats restent à confirmer."
        : "Recherche exécutée : aucun résultat public suffisamment fiable n'a été identifié.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "La recherche publique n'a pas pu être effectuée.");
    } finally {
      setResearchingPresence(false);
    }
  };

  const decidePublicFinding = async (findingIndex, accepted) => {
    setSending(true);
    try {
      const updated = await decideMarketingPublicFinding(session.sessionId, findingIndex, accepted);
      setSession(withUiState(updated));
      toast.success(accepted ? "Information publique confirmée et ajoutée à l’audit." : "Proposition publique écartée.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "La proposition publique n’a pas pu être traitée.");
    } finally {
      setSending(false);
    }
  };

  const addKpiMeasurement = async (measurement) => {
    setKpiSaving(true);
    try {
      const dashboard = await addMarketingKpiMeasurement(session.sessionId, measurement);
      setKpiDashboard(dashboard);
      setFollowUpDashboard((current) => current ? { ...current, kpis: dashboard } : current);
      toast.success("Mesure KPI enregistrée.");
    }
    catch (error) { toast.error(error.response?.data?.message ?? "La mesure n'a pas pu être enregistrée."); }
    finally { setKpiSaving(false); }
  };

  const toggleCheckIns = async () => {
    try { const next = !checkInsEnabled; await setMarketingCheckIns(session.sessionId, next); setCheckInsEnabled(next); toast.success(next ? "Relances activées." : "Relances désactivées."); }
    catch (error) { toast.error(error.response?.data?.message ?? "La préférence n'a pas pu être modifiée."); }
  };

  const continueFollowUp = async () => {
    setSending(true);
    try {
      const dashboard = await getMarketingFollowUp(session.sessionId);
      setFollowUpDashboard(dashboard);
      setKpiDashboard(dashboard.kpis);
      setFollowUpMode(true);
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Impossible d’ouvrir l’espace de suivi.");
    } finally {
      setSending(false);
    }
  };

  const updateAction = async (action, changes) => {
    try {
      const updated = await updateMarketingAction(session.sessionId, action.id, changes);
      setFollowUpDashboard((current) => ({
        ...current,
        actions: current.actions.map((item) => item.id === updated.id ? updated : item),
      }));
    } catch (error) {
      toast.error(error.response?.data?.message ?? "L’action n’a pas pu être mise à jour.");
    }
  };

  if (loading) return <main className="flex min-h-screen items-center justify-center bg-[#f5f9f9]"><Loader2 className="h-8 w-8 animate-spin text-primary" /></main>;

  return (
    <main className="min-h-screen bg-[#f5f9f9] px-4 py-6 text-[#092f38] md:px-8">
      {celebration ? <div role="status" aria-live="polite" className={`fixed left-1/2 top-5 z-[70] flex -translate-x-1/2 items-center gap-3 rounded-2xl border border-emerald-200 bg-white px-5 py-3 text-sm font-bold text-emerald-700 shadow-xl ${celebration.reducedMotion ? "" : "animate-bounce"}`}><span className="flex h-8 w-8 items-center justify-center rounded-full bg-emerald-100"><PartyPopper className="h-4 w-4" /></span>{celebration.label} <CheckCircle2 className="h-4 w-4" /></div> : null}
      <div className={`mx-auto grid max-w-[1600px] gap-5 lg:grid-cols-[280px_1fr] ${followUpMode ? "xl:grid-cols-[260px_minmax(0,1fr)]" : "xl:grid-cols-[260px_minmax(0,1fr)_320px]"}`}>
        <aside className="rounded-3xl border border-[#dbe8e8] bg-white p-6 shadow-card">
          <button type="button" onClick={() => navigate("/client/dashboard")} className="flex items-center gap-2 text-sm font-bold text-primary"><ArrowLeft className="h-4 w-4" /> Tableau de bord</button>
          <div className="mt-8 flex h-14 w-14 items-center justify-center rounded-2xl bg-primary/10 text-primary"><Target className="h-7 w-7" /></div>
          <h1 className="mt-4 text-2xl font-extrabold">Stratégie Marketing Digital</h1>
          <p className="mt-2 text-sm leading-6 text-slate-500">Un accompagnement progressif, fondé uniquement sur vos réponses et la bibliothèque Marketing.</p>
          {!followUpMode ? <div className="mt-7 rounded-2xl bg-[#edf7f6] p-4">
            <p className="text-xs font-bold uppercase tracking-wider text-primary">Étape active</p>
            <p className="mt-2 font-bold">{STAGE_LABELS[session?.stage] ?? session?.stage}</p>
            <p className="mt-2 text-xs text-slate-500">Confiance : {session?.confidence ?? "LOW"}</p>
            <div className="mt-3 h-2 overflow-hidden rounded-full bg-white">
              <div className="h-full rounded-full bg-primary transition-all" style={{ width: `${Math.max(8, ((WORKFLOW_STAGES.indexOf(session?.stage) + 1) / WORKFLOW_STAGES.length) * 100)}%` }} />
            </div>
            <p className="mt-2 text-xs text-slate-500">Étape {Math.max(1, WORKFLOW_STAGES.indexOf(session?.stage) + 1)} sur {WORKFLOW_STAGES.length}</p>
          </div> : <div className="mt-7 rounded-2xl bg-[#edf7f6] p-4"><p className="text-xs font-bold uppercase tracking-wider text-primary">Mode actif</p><p className="mt-2 font-bold">Exécution & suivi</p><p className="mt-2 text-xs leading-5 text-slate-500">Pilotez le plan d’action et mesurez les résultats obtenus.</p></div>}
          <div className="mt-5 space-y-2 text-xs text-slate-500">
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Une question principale à la fois</p>
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Aucun canal recommandé prématurément</p>
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Session sauvegardée automatiquement</p>
          </div>
          <button type="button" disabled={sending} onClick={restart} className="mt-6 flex w-full items-center justify-center gap-2 rounded-xl border border-[#ccdddd] px-4 py-3 text-sm font-bold text-primary transition hover:bg-[#edf7f6] disabled:opacity-50"><RefreshCw className="h-4 w-4" /> Nouvelle stratégie</button>
          {savedSessions.length > 0 ? <div className="mt-5 border-t border-[#e4eeee] pt-5"><p className="mb-3 flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-slate-400"><History className="h-4 w-4" /> Suivis enregistrés</p><div className="space-y-2">{savedSessions.map((item) => <div key={item.sessionId} className={`flex items-center gap-1 rounded-xl border pr-2 transition ${item.sessionId === session?.sessionId ? "border-primary bg-[#edf7f6]" : "border-[#dbe8e8] hover:border-primary/50"}`}><button type="button" onClick={() => void openSession(item.sessionId)} className="min-w-0 flex-1 px-3 py-3 text-left"><span className="block truncate text-sm font-bold text-[#173f46]">{item.title}</span><span className="mt-1 block text-[11px] text-slate-400">Diagnostic {item.diagnosticId ?? "non lié"} · {STAGE_LABELS[item.stage] ?? item.stage}</span></button><button type="button" disabled={sending} onClick={() => void removeSession(item)} aria-label={`Supprimer ${item.title}`} title="Supprimer ce suivi" className="rounded-lg p-2 text-slate-400 transition hover:bg-red-50 hover:text-red-600 disabled:opacity-50"><Trash2 className="h-4 w-4" /></button></div>)}</div></div> : null}
        </aside>

        <section className="flex min-h-[82vh] flex-col overflow-hidden rounded-3xl border border-[#dbe8e8] bg-white shadow-card">
          <header className="flex items-center gap-4 border-b border-[#e4eeee] px-6 py-5"><span className="flex h-11 w-11 items-center justify-center rounded-full bg-primary text-white"><Bot /></span><div><h2 className="font-bold">{followUpMode ? "Suivi de votre stratégie" : "Coach IA Marketing HWC"}</h2><p className="text-xs text-slate-500">{followUpMode ? "Passez de la stratégie à l’exécution, action par action" : "Votre stratégie se construit et se sauvegarde au fil de la conversation"}</p></div></header>
          {followUpMode ? (
            <div className="flex-1 overflow-y-auto p-6"><MarketingFollowUpDashboard dashboard={followUpDashboard} onToggleAction={(action) => updateAction(action, { completed: action.status !== "COMPLETED" })} onUpdateAction={updateAction} onAddKpi={addKpiMeasurement} onOpenCoach={() => setFollowUpChatOpen(true)} checkInsEnabled={checkInsEnabled} onToggleCheckIns={toggleCheckIns} loading={kpiSaving || sending} /></div>
          ) : deliverable ? (
            <div className="flex-1 space-y-6 overflow-y-auto p-6"><div><div className="mb-5 flex items-center gap-3"><Sparkles className="text-primary" /><h2 className="text-xl font-bold">Votre mini-stratégie</h2></div><pre className="whitespace-pre-wrap rounded-2xl bg-[#f7fbfb] p-6 font-sans text-sm leading-7">{deliverable}</pre></div><MarketingKpiDashboard dashboard={kpiDashboard} onAdd={addKpiMeasurement} checkInsEnabled={checkInsEnabled} onToggleCheckIns={toggleCheckIns} loading={kpiSaving} /><button type="button" onClick={continueFollowUp} className="flex w-full items-center justify-center gap-2 rounded-xl bg-primary px-4 py-3 font-bold text-white"><Bot className="h-4 w-4" /> Continuer le suivi avec le Coach</button></div>
          ) : (
            <>
              <div className="flex-1 space-y-4 overflow-y-auto p-6">
                {messages.map((message, index) => <div key={`${message.role}-${index}`} className={`flex ${message.role === "user" ? "justify-end" : "justify-start"}`}><div className={`max-w-[82%] rounded-2xl px-5 py-3 text-sm leading-6 ${message.role === "user" ? "bg-primary text-white" : "bg-[#edf7f6] text-[#173f46]"}`}>{message.imageDataUrl ? <AnnotatedMarketingImage src={message.imageDataUrl} name={message.imageName} annotations={message.imageAnnotations} /> : null}{message.content}{message.role === "assistant" && !message.resume && !message.reactionResult && index === messages.length - 1 && !sending ? <div className="mt-3 flex flex-wrap gap-2 border-t border-primary/10 pt-2"><button type="button" onClick={() => reactToMessage("CORRECT")} className="rounded-full bg-white px-3 py-1 text-[11px] font-bold text-primary">Correct</button><button type="button" onClick={() => reactToMessage("CLARIFY")} className="rounded-full bg-white px-3 py-1 text-[11px] font-bold text-amber-700">Pas tout à fait</button><button type="button" onClick={() => reactToMessage("REVISE")} className="rounded-full bg-white px-3 py-1 text-[11px] font-bold text-slate-600">À revoir</button></div> : null}</div></div>)}
                {sending ? <div className="flex items-center gap-2 text-sm text-slate-400"><Loader2 className="h-4 w-4 animate-spin" /> {statusMessage || "J'analyse votre réponse..."}</div> : null}
                {!sending && suggestions.length > 0 ? <div className="pl-1"><p className="mb-2 text-xs font-medium text-slate-400">Suggestions à adapter avant l'envoi</p><div className="flex flex-wrap gap-2">{suggestions.map((suggestion) => <button key={suggestion} type="button" onClick={() => setInput(suggestion)} className="rounded-full border border-primary/30 bg-white px-4 py-2 text-left text-xs font-semibold text-primary transition hover:border-primary hover:bg-[#edf7f6]">{suggestion}</button>)}</div></div> : null}
                <div ref={endRef} />
              </div>
              {session?.readyToFinalize ? <div className="border-t border-[#e4eeee] p-4"><button type="button" disabled={sending} onClick={finalize} className="btn btn-hero w-full"><Sparkles className="h-4 w-4" /> Générer ma mini-stratégie</button></div> : (
                <form onSubmit={submit} className="border-t border-[#e4eeee] p-4"><MarketingStructuredInput key={`${session.stage}-${inputSpec?.targetPath}`} spec={inputSpec} disabled={sending} onApply={setInput} />{selectedImage ? <div className="mb-3 flex items-center gap-3 rounded-xl bg-[#edf7f6] p-2"><img src={URL.createObjectURL(selectedImage)} alt="Aperçu" className="h-16 w-16 rounded-lg object-cover" /><span className="min-w-0 flex-1 truncate text-xs font-semibold">{selectedImage.name}</span><button type="button" onClick={() => setSelectedImage(null)} aria-label="Retirer l'image"><X className="h-4 w-4" /></button></div> : null}<div className="flex gap-3"><label className="flex h-[52px] w-[52px] cursor-pointer items-center justify-center rounded-xl border border-[#ccdddd] text-primary hover:bg-[#edf7f6]" title="Joindre une image"><ImagePlus className="h-5 w-5" /><input type="file" accept="image/png,image/jpeg,image/webp" className="hidden" onChange={(event) => { const file = event.target.files?.[0]; if (file && file.size <= 4 * 1024 * 1024) setSelectedImage(file); else if (file) toast.error("L'image ne doit pas dépasser 4 Mo."); event.target.value = ""; }} /></label><button type="button" onClick={toggleVoiceInput} aria-label={listening ? "Arrêter la dictée" : "Dicter la réponse"} className={`flex h-[52px] w-[52px] items-center justify-center rounded-xl border ${listening ? "border-red-400 bg-red-50 text-red-600" : "border-[#ccdddd] text-primary"}`}>{listening ? <MicOff className="h-5 w-5" /> : <Mic className="h-5 w-5" />}</button><textarea value={input} onChange={(event) => setInput(event.target.value)} onKeyDown={(event) => { if (event.key === "Enter" && !event.shiftKey) { event.preventDefault(); event.currentTarget.form?.requestSubmit(); } }} maxLength={4000} rows={2} placeholder={listening ? "Dictée en cours… Le texte ne sera pas envoyé automatiquement." : "Décrivez votre situation ou joignez une image..."} className="min-h-[52px] flex-1 resize-none rounded-xl border border-[#ccdddd] px-4 py-3 outline-none focus:border-primary" /><button type="submit" disabled={sending || (!input.trim() && !selectedImage)} aria-label="Envoyer" className="flex h-[52px] w-[52px] items-center justify-center rounded-xl bg-primary px-4 text-white disabled:opacity-50"><Send className="h-5 w-5" /></button></div><div className="mt-2 flex justify-between text-xs text-slate-400"><span>{listening ? "Relisez puis cliquez sur Envoyer" : "JPEG, PNG ou WebP · 4 Mo maximum"}</span><span>{input.length}/4000</span></div></form>
              )}
            </>
          )}
        </section>
        {!followUpMode ? <MarketingStrategyPanel state={session} onCorrect={correctState} onDecidePublicFinding={decidePublicFinding} disabled={sending} celebrationsEnabled={celebrationsEnabled} onToggleCelebrations={toggleCelebrations} onResearchPublicPresence={researchPublicPresence} researchingPresence={researchingPresence} /> : null}
      </div>
      {followUpChatOpen ? <MarketingFollowUpChat sessionId={session.sessionId} priority={followUpDashboard?.kpis?.nextPriority ?? followUpDashboard?.immediatePriority} onClose={() => setFollowUpChatOpen(false)} onSend={(message) => sendMarketingMessage(session.sessionId, message)} /> : null}
    </main>
  );
}
