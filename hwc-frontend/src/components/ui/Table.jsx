import EmptyState from "./EmptyState.jsx";
import Spinner from "./Spinner.jsx";
import { cn } from "../../utils/cn.js";

export default function Table({ columns = [], data = [], emptyText, keyField = "id", loading = false, rowActions }) {
  const hasActions = Boolean(rowActions);

  return (
    <div className="overflow-hidden rounded-lg border border-border bg-card">
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-border text-left text-sm">
          <thead className="bg-muted/60 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
            <tr>
              {columns.map((column) => (
                <th className={cn("px-4 py-3", column.className)} key={column.key} scope="col">
                  {column.header}
                </th>
              ))}
              {hasActions ? (
                <th className="px-4 py-3 text-right" scope="col">
                  Actions
                </th>
              ) : null}
            </tr>
          </thead>
          <tbody className="divide-y divide-border bg-card">
            {loading ? (
              <tr>
                <td className="px-4 py-10 text-center text-muted-foreground" colSpan={columns.length + (hasActions ? 1 : 0)}>
                  <span className="inline-flex items-center gap-2">
                    <Spinner className="h-4 w-4" />
                    Chargement...
                  </span>
                </td>
              </tr>
            ) : data.length > 0 ? (
              data.map((row, rowIndex) => (
                <tr className="transition-colors hover:bg-muted/40" key={row[keyField] ?? rowIndex}>
                  {columns.map((column) => (
                    <td className={cn("px-4 py-3 align-middle text-foreground", column.cellClassName)} key={column.key}>
                      {column.render ? column.render(row, rowIndex) : row[column.key]}
                    </td>
                  ))}
                  {hasActions ? <td className="px-4 py-3 text-right">{rowActions(row, rowIndex)}</td> : null}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={columns.length + (hasActions ? 1 : 0)}>
                  <EmptyState
                    className="m-4 min-h-40 border-0 bg-transparent"
                    description={emptyText ?? "Aucun element a afficher."}
                    title="Liste vide"
                  />
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
