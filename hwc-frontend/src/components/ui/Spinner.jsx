import { Loader2 } from "lucide-react";
import { cn } from "../../utils/cn.js";

export default function Spinner({ className, label = "Chargement" }) {
  return (
    <Loader2
      aria-label={label}
      className={cn("h-5 w-5 animate-spin text-current", className)}
      role="status"
    />
  );
}
