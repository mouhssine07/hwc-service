import { ArrowLeft, Bot, CheckCircle2, History, ImagePlus, Loader2, RefreshCw, Send, Sparkles, Target, Trash2, X } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import {
  createMarketingSession,
  deleteMarketingSession,
  finalizeMarketingSession,
  getMarketingDeliverable,
  getMarketingMessages,
  getMarketingSession,
  listMarketingSessions,
  sendMarketingMessage,
} from "../../api/marketingCoachApi.js";

const STORAGE_KEY = "hwc_marketing_strategy_session_id";

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
  const endRef = useRef(null);

  const refreshSessions = async () => setSavedSessions(await listMarketingSessions());

  const openSession = async (sessionId) => {
    setLoading(true);
    try {
      const [state, history] = await Promise.all([getMarketingSession(sessionId), getMarketingMessages(sessionId)]);
      window.localStorage.setItem(STORAGE_KEY, sessionId);
      setSession({ sessionId: state.sessionId, stage: state.stage, missingInformation: state.missingInformation, confidence: state.confidence, readyToFinalize: state.stage === "FINAL_DELIVERABLE" });
      setMessages(history.map(({ role, content, imageDataUrl, imageName }) => ({ role, content, imageDataUrl, imageName })));
      setSuggestions([]);
      setDeliverable(state.stage === "COMPLETED" ? await getMarketingDeliverable(sessionId) : "");
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
          setSession(created);
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
          setSession({
            sessionId: state.sessionId,
            stage: state.stage,
            missingInformation: state.missingInformation,
            confidence: state.confidence,
            readyToFinalize: state.stage === "FINAL_DELIVERABLE",
          });
          setMessages(history.map(({ role, content }) => ({ role, content })));
          if (state.stage === "COMPLETED") {
            setDeliverable(await getMarketingDeliverable(storedId));
          }
          return;
        } catch {
          window.localStorage.removeItem(STORAGE_KEY);
        }
      }
      const created = await createMarketingSession();
      if (!active) return;
      window.localStorage.setItem(STORAGE_KEY, created.sessionId);
      setSession(created);
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

  const sendMessage = async (rawMessage) => {
    const message = rawMessage.trim();
    if ((!message && !selectedImage) || !session || sending) return;
    const imageDataUrl = selectedImage ? URL.createObjectURL(selectedImage) : null;
    const effectiveMessage = message || "Analyse cette image dans le contexte de ma stratégie Marketing.";
    setMessages((items) => [...items, { role: "user", content: effectiveMessage, imageDataUrl, imageName: selectedImage?.name }]);
    setInput("");
    setSuggestions([]);
    setSending(true);
    try {
      const response = await sendMarketingMessage(session.sessionId, effectiveMessage, selectedImage);
      setSelectedImage(null);
      setSession(response);
      setMessages((items) => [...items, { role: "assistant", content: response.message }]);
      setSuggestions(response.suggestedReplies ?? []);
      await refreshSessions();
    } catch (error) {
      setInput(message);
      toast.error(error.response?.data?.message ?? "Le Coach n'a pas pu analyser cette réponse.");
    } finally {
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
      setSession(created);
      setMessages([{ role: "assistant", content: created.message }]);
      setDeliverable("");
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
      await refreshSessions();
      toast.success("Votre mini-stratégie Marketing Digital est prête.");
    } catch (error) {
      toast.error(error.response?.data?.message ?? "Le livrable ne peut pas encore être généré.");
    } finally {
      setSending(false);
    }
  };

  if (loading) return <main className="flex min-h-screen items-center justify-center bg-[#f5f9f9]"><Loader2 className="h-8 w-8 animate-spin text-primary" /></main>;

  return (
    <main className="min-h-screen bg-[#f5f9f9] px-4 py-6 text-[#092f38] md:px-8">
      <div className="mx-auto grid max-w-7xl gap-5 lg:grid-cols-[280px_1fr]">
        <aside className="rounded-3xl border border-[#dbe8e8] bg-white p-6 shadow-card">
          <button type="button" onClick={() => navigate("/client/dashboard")} className="flex items-center gap-2 text-sm font-bold text-primary"><ArrowLeft className="h-4 w-4" /> Tableau de bord</button>
          <div className="mt-8 flex h-14 w-14 items-center justify-center rounded-2xl bg-primary/10 text-primary"><Target className="h-7 w-7" /></div>
          <h1 className="mt-4 text-2xl font-extrabold">Stratégie Marketing Digital</h1>
          <p className="mt-2 text-sm leading-6 text-slate-500">Un accompagnement progressif, fondé uniquement sur vos réponses et la bibliothèque Marketing.</p>
          <div className="mt-7 rounded-2xl bg-[#edf7f6] p-4">
            <p className="text-xs font-bold uppercase tracking-wider text-primary">Étape active</p>
            <p className="mt-2 font-bold">{STAGE_LABELS[session?.stage] ?? session?.stage}</p>
            <p className="mt-2 text-xs text-slate-500">Confiance : {session?.confidence ?? "LOW"}</p>
            <div className="mt-3 h-2 overflow-hidden rounded-full bg-white">
              <div className="h-full rounded-full bg-primary transition-all" style={{ width: `${Math.max(8, ((WORKFLOW_STAGES.indexOf(session?.stage) + 1) / WORKFLOW_STAGES.length) * 100)}%` }} />
            </div>
            <p className="mt-2 text-xs text-slate-500">Étape {Math.max(1, WORKFLOW_STAGES.indexOf(session?.stage) + 1)} sur {WORKFLOW_STAGES.length}</p>
          </div>
          <div className="mt-5 space-y-2 text-xs text-slate-500">
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Une question principale à la fois</p>
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Aucun canal recommandé prématurément</p>
            <p className="flex gap-2"><CheckCircle2 className="h-4 w-4 shrink-0 text-primary" /> Session sauvegardée automatiquement</p>
          </div>
          <button type="button" disabled={sending} onClick={restart} className="mt-6 flex w-full items-center justify-center gap-2 rounded-xl border border-[#ccdddd] px-4 py-3 text-sm font-bold text-primary transition hover:bg-[#edf7f6] disabled:opacity-50"><RefreshCw className="h-4 w-4" /> Nouvelle stratégie</button>
          {savedSessions.length > 0 ? <div className="mt-5 border-t border-[#e4eeee] pt-5"><p className="mb-3 flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-slate-400"><History className="h-4 w-4" /> Suivis enregistrés</p><div className="space-y-2">{savedSessions.map((item) => <div key={item.sessionId} className={`flex items-center gap-1 rounded-xl border pr-2 transition ${item.sessionId === session?.sessionId ? "border-primary bg-[#edf7f6]" : "border-[#dbe8e8] hover:border-primary/50"}`}><button type="button" onClick={() => void openSession(item.sessionId)} className="min-w-0 flex-1 px-3 py-3 text-left"><span className="block truncate text-sm font-bold text-[#173f46]">{item.title}</span><span className="mt-1 block text-[11px] text-slate-400">Diagnostic {item.diagnosticId ?? "non lié"} · {STAGE_LABELS[item.stage] ?? item.stage}</span></button><button type="button" disabled={sending} onClick={() => void removeSession(item)} aria-label={`Supprimer ${item.title}`} title="Supprimer ce suivi" className="rounded-lg p-2 text-slate-400 transition hover:bg-red-50 hover:text-red-600 disabled:opacity-50"><Trash2 className="h-4 w-4" /></button></div>)}</div></div> : null}
        </aside>

        <section className="flex min-h-[82vh] flex-col overflow-hidden rounded-3xl border border-[#dbe8e8] bg-white shadow-card">
          <header className="flex items-center gap-4 border-b border-[#e4eeee] px-6 py-5"><span className="flex h-11 w-11 items-center justify-center rounded-full bg-primary text-white"><Bot /></span><div><h2 className="font-bold">Coach IA Marketing HWC</h2><p className="text-xs text-slate-500">Votre stratégie se construit et se sauvegarde au fil de la conversation</p></div></header>
          {deliverable ? (
            <div className="flex-1 overflow-y-auto p-6"><div className="mb-5 flex items-center gap-3"><Sparkles className="text-primary" /><h2 className="text-xl font-bold">Votre mini-stratégie</h2></div><pre className="whitespace-pre-wrap rounded-2xl bg-[#f7fbfb] p-6 font-sans text-sm leading-7">{deliverable}</pre><button type="button" onClick={() => setDeliverable("")} className="mt-5 flex w-full items-center justify-center gap-2 rounded-xl bg-primary px-4 py-3 font-bold text-white"><Bot className="h-4 w-4" /> Continuer le suivi avec le Coach</button></div>
          ) : (
            <>
              <div className="flex-1 space-y-4 overflow-y-auto p-6">
                {messages.map((message, index) => <div key={`${message.role}-${index}`} className={`flex ${message.role === "user" ? "justify-end" : "justify-start"}`}><div className={`max-w-[82%] rounded-2xl px-5 py-3 text-sm leading-6 ${message.role === "user" ? "bg-primary text-white" : "bg-[#edf7f6] text-[#173f46]"}`}>{message.imageDataUrl ? <img src={message.imageDataUrl} alt={message.imageName ?? "Image envoyée au Coach"} className="mb-3 max-h-72 rounded-xl object-contain" /> : null}{message.content}</div></div>)}
                {sending ? <div className="flex items-center gap-2 text-sm text-slate-400"><Loader2 className="h-4 w-4 animate-spin" /> Analyse en cours...</div> : null}
                {!sending && suggestions.length > 0 ? <div className="pl-1"><p className="mb-2 text-xs font-medium text-slate-400">Suggestions à adapter avant l'envoi</p><div className="flex flex-wrap gap-2">{suggestions.map((suggestion) => <button key={suggestion} type="button" onClick={() => setInput(suggestion)} className="rounded-full border border-primary/30 bg-white px-4 py-2 text-left text-xs font-semibold text-primary transition hover:border-primary hover:bg-[#edf7f6]">{suggestion}</button>)}</div></div> : null}
                <div ref={endRef} />
              </div>
              {session?.readyToFinalize ? <div className="border-t border-[#e4eeee] p-4"><button type="button" disabled={sending} onClick={finalize} className="btn btn-hero w-full"><Sparkles className="h-4 w-4" /> Générer ma mini-stratégie</button></div> : (
                <form onSubmit={submit} className="border-t border-[#e4eeee] p-4">{selectedImage ? <div className="mb-3 flex items-center gap-3 rounded-xl bg-[#edf7f6] p-2"><img src={URL.createObjectURL(selectedImage)} alt="Aperçu" className="h-16 w-16 rounded-lg object-cover" /><span className="min-w-0 flex-1 truncate text-xs font-semibold">{selectedImage.name}</span><button type="button" onClick={() => setSelectedImage(null)} aria-label="Retirer l'image"><X className="h-4 w-4" /></button></div> : null}<div className="flex gap-3"><label className="flex h-[52px] w-[52px] cursor-pointer items-center justify-center rounded-xl border border-[#ccdddd] text-primary hover:bg-[#edf7f6]" title="Joindre une image"><ImagePlus className="h-5 w-5" /><input type="file" accept="image/png,image/jpeg,image/webp" className="hidden" onChange={(event) => { const file = event.target.files?.[0]; if (file && file.size <= 4 * 1024 * 1024) setSelectedImage(file); else if (file) toast.error("L'image ne doit pas dépasser 4 Mo."); event.target.value = ""; }} /></label><textarea value={input} onChange={(event) => setInput(event.target.value)} onKeyDown={(event) => { if (event.key === "Enter" && !event.shiftKey) { event.preventDefault(); event.currentTarget.form?.requestSubmit(); } }} maxLength={4000} rows={2} placeholder="Décrivez votre situation ou joignez une image..." className="min-h-[52px] flex-1 resize-none rounded-xl border border-[#ccdddd] px-4 py-3 outline-none focus:border-primary" /><button type="submit" disabled={sending || (!input.trim() && !selectedImage)} aria-label="Envoyer" className="flex h-[52px] w-[52px] items-center justify-center rounded-xl bg-primary px-4 text-white disabled:opacity-50"><Send className="h-5 w-5" /></button></div><div className="mt-2 flex justify-between text-xs text-slate-400"><span>JPEG, PNG ou WebP · 4 Mo maximum</span><span>{input.length}/4000</span></div></form>
              )}
            </>
          )}
        </section>
      </div>
    </main>
  );
}
