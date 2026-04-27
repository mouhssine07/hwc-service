export default function SectionHeading({ title, subtitle, centered = true }) {
  return (
    <div className={`mb-12 ${centered ? "text-center" : ""}`}>
      <h2 className="mb-4 font-display text-3xl font-bold md:text-4xl lg:text-5xl">
        <span className="text-gradient">{title}</span>
      </h2>
      {subtitle ? (
        <p className={`text-lg text-muted-foreground ${centered ? "mx-auto max-w-2xl" : "max-w-2xl"}`}>
          {subtitle}
        </p>
      ) : null}
    </div>
  );
}
