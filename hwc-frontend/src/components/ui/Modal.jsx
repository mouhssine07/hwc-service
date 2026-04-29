import { X } from "lucide-react";
import { createPortal } from "react-dom";
import Button from "./Button.jsx";
import { cn } from "../../utils/cn.js";

export default function Modal({ children, className, description, onClose, open, title }) {
  if (!open) {
    return null;
  }

  return createPortal(
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4">
      <button
        aria-label="Fermer"
        className="absolute inset-0 bg-foreground/50 backdrop-blur-sm"
        onClick={onClose}
        type="button"
      />
      <div
        aria-modal="true"
        className={cn("relative max-h-[90vh] w-full max-w-lg overflow-y-auto rounded-lg border border-border bg-card p-6 shadow-elevated", className)}
        role="dialog"
      >
        <div className="mb-5 flex items-start justify-between gap-4">
          <div>
            {title ? <h2 className="text-xl font-semibold text-foreground">{title}</h2> : null}
            {description ? <p className="mt-1 text-sm leading-6 text-muted-foreground">{description}</p> : null}
          </div>
          <Button aria-label="Fermer" onClick={onClose} size="icon" variant="ghost">
            <X className="h-5 w-5" />
          </Button>
        </div>
        {children}
      </div>
    </div>,
    document.body,
  );
}
