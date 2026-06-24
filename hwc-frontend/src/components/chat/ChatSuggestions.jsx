const suggestions = [
  "Pourquoi mon score le plus faible est prioritaire ?",
  "Quels axes dois-je traiter cette semaine ?",
  "Quel service HWC me recommandez-vous en premier ?",
];

export default function ChatSuggestions({ onSelect }) {
  return (
    <div className="space-y-2">
      {suggestions.map((suggestion) => (
        <button
          key={suggestion}
          type="button"
          className="w-full rounded-lg border border-border bg-muted/40 px-3 py-2 text-left text-xs font-semibold leading-5 text-muted-foreground hover:border-primary/40 hover:bg-accent/40 hover:text-primary"
          onClick={() => onSelect(suggestion)}
        >
          {suggestion}
        </button>
      ))}
    </div>
  );
}
