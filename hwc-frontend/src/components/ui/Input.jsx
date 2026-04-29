import { forwardRef } from "react";
import { cn } from "../../utils/cn.js";

const Input = forwardRef(({ className, error, helperText, id, label, ...props }, ref) => {
  const describedBy = error ? `${id}-error` : helperText ? `${id}-helper` : undefined;

  return (
    <div className="space-y-2">
      {label ? (
        <label className="block text-sm font-semibold text-foreground" htmlFor={id}>
          {label}
        </label>
      ) : null}
      <input
        aria-describedby={describedBy}
        aria-invalid={error ? "true" : "false"}
        className={cn(
          "h-11 w-full rounded-lg border border-input bg-background px-3 text-sm text-foreground outline-none transition-colors placeholder:text-muted-foreground focus:border-secondary focus:ring-2 focus:ring-secondary/20 disabled:cursor-not-allowed disabled:bg-muted disabled:text-muted-foreground",
          error && "border-red-500 focus:border-red-500 focus:ring-red-500/20",
          className,
        )}
        id={id}
        ref={ref}
        {...props}
      />
      {error ? (
        <p className="text-sm text-red-600" id={`${id}-error`}>
          {error}
        </p>
      ) : helperText ? (
        <p className="text-sm text-muted-foreground" id={`${id}-helper`}>
          {helperText}
        </p>
      ) : null}
    </div>
  );
});

Input.displayName = "Input";

export default Input;
