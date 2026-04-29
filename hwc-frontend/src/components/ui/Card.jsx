import { cn } from "../../utils/cn.js";

export function Card({ children, className, ...props }) {
  return (
    <section className={cn("rounded-lg border border-border bg-card p-5 shadow-sm", className)} {...props}>
      {children}
    </section>
  );
}

export function CardHeader({ children, className, ...props }) {
  return (
    <div className={cn("mb-4 flex flex-col gap-1", className)} {...props}>
      {children}
    </div>
  );
}

export function CardTitle({ children, className, ...props }) {
  return (
    <h3 className={cn("text-lg font-semibold text-foreground", className)} {...props}>
      {children}
    </h3>
  );
}

export function CardDescription({ children, className, ...props }) {
  return (
    <p className={cn("text-sm leading-6 text-muted-foreground", className)} {...props}>
      {children}
    </p>
  );
}

export function CardContent({ children, className, ...props }) {
  return (
    <div className={cn("space-y-4", className)} {...props}>
      {children}
    </div>
  );
}

export default Card;
