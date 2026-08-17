import { Bot, Loader2, Send, X } from "lucide-react";
import { useEffect, useState } from "react";
import { getMarketingFollowUpMessages } from "../../api/marketingCoachApi.js";

export default function MarketingFollowUpChat({ sessionId, priority, onClose, onSend }) {
  const [input, setInput] = useState("");
  const [sending, setSending] = useState(false);
  const [suggestions, setSuggestions] = useState([]);
  const [messages, setMessages] = useState([{
    role: "assistant",
    content: `Je connais votre plan d’action et vos derniers KPI. Votre priorité actuelle est : ${priority ?? "commencer la première action du plan"}. Quelle question ponctuelle souhaitez-vous traiter ?`,
  }]);

  useEffect(() => {
    let active = true;
    getMarketingFollowUpMessages(sessionId, 20)
      .then((history) => {
        if (!active || !history.length) return;
        setMessages(history.map(({ role, content }) => ({ role, content })));
      })
      .catch(() => { /* Le message d'accueil local reste disponible. */ });
    return () => { active = false; };
  }, [sessionId]);

  const submit = async (event) => {
    event.preventDefault();
    const message = input.trim();
    if (!message || sending) return;
    setMessages((current) => [...current, { role: "user", content: message }]);
    setInput("");
    setSending(true);
    try {
      const response = await onSend(message);
      setMessages((current) => [...current, { role: "assistant", content: response.message }]);
      setSuggestions(response.suggestedReplies ?? []);
    } catch (error) {
      const detail = error?.response?.data?.message;
      setMessages((current) => [...current, { role: "assistant", content: detail
        ? `Le Coach n’a pas pu répondre : ${detail}`
        : "Je n’ai pas pu répondre pour le moment. Vérifiez que le backend a bien été redémarré." }]);
    } finally {
      setSending(false);
    }
  };

  return <div className="fixed inset-0 z-[80] flex items-end justify-end bg-[#092f38]/20 p-3 backdrop-blur-[2px] sm:p-6" role="dialog" aria-modal="true" aria-label="Coach de suivi Marketing">
    <section className="flex h-[min(620px,calc(100vh-3rem))] w-full max-w-md flex-col overflow-hidden rounded-2xl border border-[#dbe8e8] bg-white shadow-2xl">
      <header className="flex items-center justify-between bg-primary px-4 py-3 text-white"><div className="flex items-center gap-3"><span className="flex h-9 w-9 items-center justify-center rounded-full bg-white/15"><Bot className="h-5 w-5" /></span><div><h3 className="text-sm font-extrabold">Coach de suivi</h3><p className="text-[11px] text-white/75">Question ponctuelle · contexte déjà chargé</p></div></div><button type="button" onClick={onClose} aria-label="Fermer le Coach" className="rounded-lg p-2 hover:bg-white/10"><X className="h-4 w-4" /></button></header>
      <div className="flex-1 space-y-3 overflow-y-auto p-4">{messages.map((message, index) => <div key={`${message.role}-${index}`} className={`flex ${message.role === "user" ? "justify-end" : "justify-start"}`}><p className={`max-w-[85%] whitespace-pre-wrap rounded-2xl px-4 py-3 text-sm leading-6 ${message.role === "user" ? "bg-primary text-white" : "bg-[#edf7f6] text-[#173f46]"}`}>{message.content}</p></div>)}{sending ? <p className="flex items-center gap-2 text-xs text-slate-400"><Loader2 className="h-4 w-4 animate-spin" /> Le Coach analyse votre suivi réel…</p> : null}{!sending && suggestions.length ? <div className="flex flex-wrap gap-2">{suggestions.map((suggestion) => <button key={suggestion} type="button" onClick={() => setInput(suggestion)} className="rounded-full border border-primary/25 px-3 py-1.5 text-xs font-semibold text-primary">{suggestion}</button>)}</div> : null}</div>
      <form onSubmit={submit} className="border-t border-[#e4eeee] p-3"><div className="flex gap-2"><textarea value={input} onChange={(event) => setInput(event.target.value)} onKeyDown={(event) => { if (event.key === "Enter" && !event.shiftKey) { event.preventDefault(); event.currentTarget.form?.requestSubmit(); } }} rows={2} maxLength={4000} placeholder="Posez une question sur une action ou un KPI…" className="min-h-[48px] flex-1 resize-none rounded-xl border border-[#ccdddd] px-3 py-2 text-sm outline-none focus:border-primary" /><button type="submit" disabled={sending || !input.trim()} aria-label="Envoyer" className="flex w-12 items-center justify-center rounded-xl bg-primary text-white disabled:opacity-50"><Send className="h-4 w-4" /></button></div></form>
    </section>
  </div>;
}
