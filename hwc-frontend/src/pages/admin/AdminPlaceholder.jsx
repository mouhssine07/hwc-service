import { Wrench } from "lucide-react";

export default function AdminPlaceholder() {
  return (
    <div className="card-elevated p-8">
      <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-secondary/10">
        <Wrench className="h-6 w-6 text-secondary" />
      </div>
      <h2 className="mb-2 text-xl font-semibold text-foreground">
        Page en préparation
      </h2>
      <p className="max-w-2xl text-muted-foreground">
        Le layout dashboard est prêt. Les écrans CRUD seront ajoutés dans la prochaine phase, page par page.
      </p>
    </div>
  );
}
