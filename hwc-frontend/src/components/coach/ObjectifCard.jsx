import { Check, Circle, Loader2, Save, Target } from "lucide-react";
import { useEffect, useState } from "react";

export default function ObjectifCard({ objectif, completing, onComplete, onProgress }) {
  const [quantity, setQuantity] = useState(objectif.quantiteRealisee ?? 0);
  const [comment, setComment] = useState(objectif.commentaireClient ?? "");

  useEffect(() => {
    setQuantity(objectif.quantiteRealisee ?? 0);
    setComment(objectif.commentaireClient ?? "");
  }, [objectif.quantiteRealisee, objectif.commentaireClient]);

  const target = objectif.quantiteCible ?? 1;
  const unit = objectif.unite ?? "action";

  return (
    <article className={`rounded-xl border p-5 shadow-card ${objectif.termine ? "border-primary/30 bg-primary/5" : "border-border bg-card"}`}>
      <div className="flex items-start gap-4">
        <div className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-full ${objectif.termine ? "bg-primary text-primary-foreground" : "bg-primary/10 text-primary"}`}>
          {objectif.termine ? <Check className="h-5 w-5" /> : <Target className="h-5 w-5" />}
        </div>
        <div className="min-w-0 flex-1">
          <div className="flex flex-wrap items-start justify-between gap-2">
            <p className="text-xs font-bold uppercase tracking-wider text-primary">Objectif {objectif.ordre}</p>
            <span className={`rounded-full px-2.5 py-1 text-xs font-bold ${objectif.termine ? "bg-primary/15 text-primary" : quantity > 0 ? "bg-amber-100 text-amber-700" : "bg-muted text-muted-foreground"}`}>
              {objectif.termine ? "Terminé" : quantity > 0 ? "En cours" : "À faire"}
            </span>
          </div>
          <h2 className={`mt-2 text-lg font-bold ${objectif.termine ? "text-muted-foreground line-through" : "text-foreground"}`}>{objectif.titre}</h2>
          <p className="mt-2 text-sm leading-6 text-muted-foreground">{objectif.description}</p>
          <p className="mt-4 rounded-lg bg-muted/60 px-3 py-2 text-xs font-semibold text-foreground/80">
            Résultat attendu : {target} {unit} — Réalisé : {quantity}/{target}
          </p>
          <div className="mt-4 grid gap-3 md:grid-cols-[10rem_1fr]">
            <div>
              <label htmlFor={`quantity-${objectif.id}`} className="mb-1 block text-xs font-bold">Quantité réalisée</label>
              <input id={`quantity-${objectif.id}`} type="number" min="0" max="1000000" value={quantity} onChange={(event) => setQuantity(Number(event.target.value))} className="h-10 w-full rounded-lg border border-border bg-background px-3 text-sm" />
            </div>
            <div>
              <label htmlFor={`comment-${objectif.id}`} className="mb-1 block text-xs font-bold">Résultat ou commentaire</label>
              <input id={`comment-${objectif.id}`} maxLength="1000" value={comment} onChange={(event) => setComment(event.target.value)} placeholder="Ex. 1 publication réalisée, retour obtenu..." className="h-10 w-full rounded-lg border border-border bg-background px-3 text-sm" />
            </div>
          </div>
          <div className="mt-4 flex flex-wrap gap-2">
            <button type="button" className="inline-flex h-10 items-center gap-2 rounded-lg border border-primary px-3 text-sm font-bold text-primary hover:bg-primary hover:text-primary-foreground disabled:opacity-60" disabled={completing} onClick={() => onProgress(objectif.id, { quantiteRealisee: quantity, commentaire: comment })}>
              {completing ? <Loader2 className="h-4 w-4 animate-spin" /> : <Save className="h-4 w-4" />} Enregistrer ma progression
            </button>
            {!objectif.termine ? (
              <button type="button" className="inline-flex h-10 items-center gap-2 rounded-lg bg-primary px-3 text-sm font-bold text-primary-foreground disabled:opacity-60" disabled={completing} onClick={() => onComplete(objectif.id)}>
                <Circle className="h-4 w-4" /> Marquer comme fait
              </button>
            ) : null}
          </div>
        </div>
      </div>
    </article>
  );
}
