import { forwardRef } from "react";
import Spinner from "./Spinner.jsx";
import { cn } from "../../utils/cn.js";

const variants = {
  primary: "bg-primary text-primary-foreground hover:bg-primary/90",
  secondary: "bg-secondary text-secondary-foreground hover:bg-secondary/90",
  outline: "border border-border bg-card text-foreground hover:bg-muted",
  ghost: "text-foreground hover:bg-muted",
  danger: "bg-red-600 text-white hover:bg-red-700",
};

const sizes = {
  sm: "h-9 px-3 text-xs",
  md: "h-10 px-4 text-sm",
  lg: "h-12 px-5 text-base",
  icon: "h-10 w-10 p-0",
};

const Button = forwardRef(
  (
    {
      children,
      className,
      disabled,
      loading = false,
      size = "md",
      type = "button",
      variant = "primary",
      ...props
    },
    ref,
  ) => (
    <button
      className={cn(
        "inline-flex shrink-0 items-center justify-center gap-2 rounded-lg font-semibold transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-60",
        variants[variant],
        sizes[size],
        className,
      )}
      disabled={disabled || loading}
      ref={ref}
      type={type}
      {...props}
    >
      {loading ? <Spinner className="h-4 w-4" /> : null}
      {children}
    </button>
  ),
);

Button.displayName = "Button";

export default Button;
