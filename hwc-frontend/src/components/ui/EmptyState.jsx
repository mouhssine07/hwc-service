import { Inbox } from "lucide-react";
import Button from "./Button.jsx";
import { cn } from "../../utils/cn.js";

export default function EmptyState({
  action,
  children,
  className,
  description = "Aucune donnee disponible pour le moment.",
  icon: Icon = Inbox,
  title = "Aucun resultat",
}) {
  return (
    <div
      className={cn(
        "flex min-h-48 flex-col items-center justify-center rounded-lg border border-dashed border-border bg-card p-8 text-center",
        className,
      )}
    >
      <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-lg bg-muted text-muted-foreground">
        <Icon className="h-6 w-6" />
      </div>
      <h3 className="text-base font-semibold text-foreground">{title}</h3>
      <p className="mt-2 max-w-md text-sm leading-6 text-muted-foreground">{description}</p>
      {children}
      {action ? (
        <Button className="mt-5" onClick={action.onClick} variant={action.variant ?? "primary"}>
          {action.label}
        </Button>
      ) : null}
    </div>
  );
}
