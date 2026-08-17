export default function ChatMessage({ message }) {
  const isUser = message.role === "user";

  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"}`}>
      <div
        className={`max-w-[82%] rounded-lg px-4 py-3 text-sm leading-6 shadow-sm ${
          isUser
            ? "bg-primary text-primary-foreground"
            : "border border-border bg-card text-foreground"
        }`}
      >
        {isUser ? <p className="whitespace-pre-wrap">{message.contenu}</p> : <AssistantContent content={message.contenu} />}
      </div>
    </div>
  );
}

function AssistantContent({ content }) {
  const blocks = parseBlocks(content);

  return (
    <div className="space-y-3">
      {blocks.map((block, index) => {
        if (block.type === "heading") {
          return (
            <h3 key={`${block.type}-${index}`} className="border-b border-border pb-1 text-sm font-bold text-primary">
              {block.text}
            </h3>
          );
        }

        if (block.type === "bullet") {
          return (
            <div key={`${block.type}-${index}`} className="flex gap-2 rounded-md bg-muted/50 px-3 py-2">
              <span className="mt-2 h-1.5 w-1.5 shrink-0 rounded-full bg-primary" />
              <p className="text-sm leading-6 text-foreground">{block.text}</p>
            </div>
          );
        }

        if (block.type === "number") {
          return (
            <div key={`${block.type}-${index}`} className="flex gap-2 rounded-md border border-border px-3 py-2">
              <span className="flex h-6 w-6 shrink-0 items-center justify-center rounded-md bg-primary/10 text-xs font-bold text-primary">
                {block.number}
              </span>
              <p className="text-sm leading-6 text-foreground">{block.text}</p>
            </div>
          );
        }

        return (
          <p key={`${block.type}-${index}`} className="text-sm leading-6 text-foreground">
            {block.text}
          </p>
        );
      })}
    </div>
  );
}

function parseBlocks(content) {
  return String(content ?? "")
    .split(/\n+/)
    .map((line) => line.trim())
    .filter(Boolean)
    .map((line) => {
      if (line.startsWith("## ")) {
        return { type: "heading", text: line.replace(/^##\s+/, "") };
      }

      if (line.startsWith("- ")) {
        return { type: "bullet", text: line.replace(/^-\s+/, "") };
      }

      const numbered = line.match(/^(\d+)\.\s+(.*)$/);
      if (numbered) {
        return { type: "number", number: numbered[1], text: numbered[2] };
      }

      return { type: "paragraph", text: line.replace(/^#+\s*/, "") };
    });
}
