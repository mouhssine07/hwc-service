import { MessageSquare } from "lucide-react";
import { useState } from "react";
import ChatbotWindow from "./ChatbotWindow.jsx";

export default function ChatbotButton() {
  const [open, setOpen] = useState(false);

  return (
    <>
      {open ? <ChatbotWindow onClose={() => setOpen(false)} /> : null}
      <button
        type="button"
        className="fixed bottom-5 right-4 z-50 inline-flex h-14 items-center gap-3 rounded-lg bg-primary px-4 text-sm font-bold text-primary-foreground shadow-elevated hover:bg-primary/90"
        onClick={() => setOpen((value) => !value)}
      >
        <MessageSquare className="h-5 w-5" />
        Assistant IA
      </button>
    </>
  );
}
