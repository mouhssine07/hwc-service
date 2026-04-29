import { ChevronLeft, ChevronRight } from "lucide-react";
import Button from "./Button.jsx";

export default function Pagination({ page = 0, size = 10, totalElements = 0, totalPages, onPageChange }) {
  const pages = totalPages ?? Math.max(1, Math.ceil(totalElements / size));
  const current = page + 1;
  const from = totalElements === 0 ? 0 : page * size + 1;
  const to = Math.min(totalElements, (page + 1) * size);

  return (
    <div className="flex flex-col gap-3 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
      <p>
        {from}-{to} sur {totalElements}
      </p>
      <div className="flex items-center gap-2">
        <Button disabled={page <= 0} onClick={() => onPageChange(page - 1)} size="sm" variant="outline">
          <ChevronLeft className="h-4 w-4" />
          Precedent
        </Button>
        <span className="min-w-20 text-center font-medium text-foreground">
          {current} / {pages}
        </span>
        <Button disabled={current >= pages} onClick={() => onPageChange(page + 1)} size="sm" variant="outline">
          Suivant
          <ChevronRight className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}
