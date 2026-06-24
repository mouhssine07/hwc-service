import { Loader2, MessageSquare, X } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { listChatConversations, listChatMessages, sendChatMessage } from "../../api/chatApi.js";
import { getClientDashboard } from "../../api/dashboardApi.js";
import ChatInput from "./ChatInput.jsx";
import ChatMessage from "./ChatMessage.jsx";
import ChatSuggestions from "./ChatSuggestions.jsx";

const ACTIVE_CONVERSATION_KEY = "hwc-active-chat-conversation-id";

export default function ChatbotWindow({ onClose }) {
  const [conversationId, setConversationId] = useState(null);
  const [diagnosticId, setDiagnosticId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(true);
  const [sending, setSending] = useState(false);
  const scrollRef = useRef(null);

  useEffect(() => {
    let mounted = true;

    async function initializeChat() {
      try {
        const [dashboard, conversations] = await Promise.all([getClientDashboard(), listChatConversations()]);
        if (!mounted) {
          return;
        }

        if (dashboard?.disponible && dashboard.dernierDiagnosticId) {
          setDiagnosticId(dashboard.dernierDiagnosticId);
        }

        const storedConversationId = Number(window.localStorage.getItem(ACTIVE_CONVERSATION_KEY));
        const activeConversation =
          conversations.find((conversation) => conversation.id === storedConversationId) ?? conversations[0];

        if (activeConversation) {
          setConversationId(activeConversation.id);
          setDiagnosticId(activeConversation.diagnosticId ?? dashboard?.dernierDiagnosticId ?? null);
          window.localStorage.setItem(ACTIVE_CONVERSATION_KEY, String(activeConversation.id));
          setMessages(await listChatMessages(activeConversation.id));
        }
      } catch {
        if (mounted) {
          setDiagnosticId(null);
        }
      } finally {
        if (mounted) {
          setLoadingHistory(false);
        }
      }
    }

    initializeChat();

    return () => {
      mounted = false;
    };
  }, []);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth", block: "end" });
  }, [messages, sending]);

  const handleSend = async (content) => {
    const optimisticMessage = {
      id: `local-${Date.now()}`,
      role: "user",
      contenu: content,
    };

    setMessages((current) => [...current, optimisticMessage]);
    setSending(true);

    try {
      const response = await sendChatMessage({
        conversationId,
        diagnosticId: conversationId ? null : diagnosticId,
        message: content,
      });
      setConversationId(response.conversationId);
      window.localStorage.setItem(ACTIVE_CONVERSATION_KEY, String(response.conversationId));
      const serverMessages = await listChatMessages(response.conversationId);
      setMessages(serverMessages);
    } catch {
      toast.error("Impossible de contacter l'assistant IA");
      setMessages((current) => current.filter((message) => message.id !== optimisticMessage.id));
    } finally {
      setSending(false);
    }
  };

  return (
    <section className="fixed bottom-24 right-4 z-50 flex h-[min(620px,calc(100vh-7rem))] w-[min(420px,calc(100vw-2rem))] flex-col overflow-hidden rounded-lg border border-border bg-card shadow-elevated">
      <header className="flex h-14 items-center justify-between border-b border-border bg-primary px-4 text-primary-foreground">
        <div className="flex min-w-0 items-center gap-2">
          <MessageSquare className="h-5 w-5 shrink-0" />
          <div className="min-w-0">
            <h2 className="truncate text-sm font-bold">Assistant HWC</h2>
            <p className="truncate text-xs text-primary-foreground/80">Analyse diagnostic avec Ollama local</p>
          </div>
        </div>
        <button
          type="button"
          className="inline-flex h-9 w-9 items-center justify-center rounded-lg hover:bg-white/10"
          onClick={onClose}
          aria-label="Fermer"
        >
          <X className="h-4 w-4" />
        </button>
      </header>

      <div className="flex-1 space-y-3 overflow-y-auto bg-muted/30 p-4">
        {loadingHistory ? (
          <div className="flex items-center gap-2 rounded-lg border border-border bg-card p-4 text-sm text-muted-foreground shadow-sm">
            <Loader2 className="h-4 w-4 animate-spin text-primary" />
            Chargement de votre conversation...
          </div>
        ) : null}

        {!loadingHistory && !messages.length ? (
          <div className="rounded-lg border border-border bg-card p-4 shadow-sm">
            <h3 className="text-sm font-bold text-foreground">Comment puis-je vous aider ?</h3>
            <p className="mt-1 text-xs leading-5 text-muted-foreground">
              {diagnosticId
                ? `Je peux expliquer le diagnostic #${diagnosticId}, ses scores et ses recommandations HWC.`
                : "Je peux vous guider apres un diagnostic finalise."}
            </p>
            <div className="mt-4">
              <ChatSuggestions onSelect={handleSend} />
            </div>
          </div>
        ) : null}

        {!loadingHistory && messages.map((message) => (
          <ChatMessage key={message.id} message={message} />
        ))}

        {sending ? (
          <div className="flex justify-start">
            <div className="inline-flex items-center gap-2 rounded-lg border border-border bg-card px-4 py-3 text-sm text-muted-foreground shadow-sm">
              <Loader2 className="h-4 w-4 animate-spin text-primary" />
              Analyse en cours...
            </div>
          </div>
        ) : null}
        <div ref={scrollRef} />
      </div>

      <ChatInput disabled={sending || loadingHistory} onSend={handleSend} />
    </section>
  );
}
