import { Send } from "lucide-react";
import { useState } from "react";

export default function ChatInput({ disabled, onSend }) {
  const [message, setMessage] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();
    const trimmed = message.trim();
    if (!trimmed || disabled) {
      return;
    }
    onSend(trimmed);
    setMessage("");
  };

  return (
    <form className="flex items-end gap-2 border-t border-border bg-card p-3" onSubmit={handleSubmit}>
      <textarea
        className="min-h-11 max-h-28 flex-1 resize-none rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/15"
        placeholder="Posez une question..."
        value={message}
        disabled={disabled}
        rows={1}
        onChange={(event) => setMessage(event.target.value)}
        onKeyDown={(event) => {
          if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            handleSubmit(event);
          }
        }}
      />
      <button
        type="submit"
        className="inline-flex h-11 w-11 shrink-0 items-center justify-center rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 disabled:cursor-not-allowed disabled:opacity-60"
        disabled={disabled || !message.trim()}
        aria-label="Envoyer"
      >
        <Send className="h-4 w-4" />
      </button>
    </form>
  );
}
