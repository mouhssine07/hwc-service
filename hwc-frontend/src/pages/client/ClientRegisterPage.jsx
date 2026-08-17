import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRight, Building2, Loader2 } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { z } from "zod";
import { clientRegister } from "../../api/clientAuthApi.js";
import useClientAuthStore from "../../store/clientAuthStore.js";

const registerSchema = z.object({
  email: z.string().email("Email invalide"),
  password: z.string().min(6, "Minimum 6 caracteres"),
  nom: z.string().min(2, "Nom requis"),
  prenom: z.string().min(2, "Prenom requis"),
  entreprise: z.string().min(2, "Entreprise requise"),
  secteur: z.string().min(2, "Secteur requis"),
  tailleEntreprise: z.string().optional(),
  telephone: z.string().optional(),
});

export default function ClientRegisterPage() {
  const navigate = useNavigate();
  const isClientAuthenticated = useClientAuthStore((state) => state.isClientAuthenticated);
  const [loading, setLoading] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      email: "",
      password: "",
      nom: "",
      prenom: "",
      entreprise: "",
      secteur: "",
      tailleEntreprise: "",
      telephone: "",
    },
  });

  if (isClientAuthenticated) {
    return <Navigate to="/client/diagnostic" replace />;
  }

  const onSubmit = async (values) => {
    setLoading(true);

    try {
      await clientRegister(values);
      toast.success("Compte client cree. Connectez-vous pour continuer.");
      navigate("/client/login", { replace: true });
    } catch (error) {
      const message =
        error.response?.status === 409
          ? "Un compte existe deja avec cet email"
          : "Impossible de creer le compte client";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  const inputClass =
    "h-12 w-full rounded-lg border border-border bg-background px-4 text-sm outline-none transition-colors focus:border-primary focus:ring-2 focus:ring-primary/20";

  return (
    <main className="min-h-screen bg-muted/30 px-4 py-10">
      <div className="mx-auto flex min-h-[calc(100vh-5rem)] w-full max-w-5xl items-center justify-center">
        <section className="grid w-full overflow-hidden rounded-2xl border border-border bg-card shadow-elevated lg:grid-cols-[0.9fr_1.1fr]">
          <div className="relative hidden bg-primary p-10 text-primary-foreground lg:block">
            <div
              className="absolute inset-0 bg-cover bg-center opacity-20"
              style={{ backgroundImage: "url(https://images.unsplash.com/photo-1556761175-b413da4baf72?w=1200)" }}
            />
            <div className="absolute inset-0 bg-gradient-to-br from-primary to-secondary/80" />
            <div className="relative z-10 flex h-full flex-col justify-between">
              <Link to="/" className="w-fit">
                <img
                  src="/images/Logo_HWC-J-9-1DQr.png"
                  alt="Harmony Works Consulting"
                  className="h-12 w-fit brightness-0 invert"
                />
              </Link>
              <div>
                <p className="mb-3 text-sm font-semibold uppercase tracking-wider text-primary-foreground/70">
                  Espace client
                </p>
                <h1 className="font-display text-4xl font-bold leading-tight">
                  Lancez votre diagnostic et obtenez une lecture claire de vos priorites.
                </h1>
              </div>
            </div>
          </div>

          <div className="p-6 md:p-8">
            <div className="mb-7">
              <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-primary/10">
                <Building2 className="h-6 w-6 text-primary" />
              </div>
              <h2 className="font-display text-3xl font-bold text-foreground">Inscription client</h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Les informations entreprise personnalisent votre diagnostic.
              </p>
            </div>

            <form className="grid gap-4 md:grid-cols-2" onSubmit={handleSubmit(onSubmit)}>
              {[
                ["prenom", "Prenom"],
                ["nom", "Nom"],
                ["email", "Email"],
                ["password", "Mot de passe"],
                ["entreprise", "Entreprise"],
                ["secteur", "Secteur"],
                ["tailleEntreprise", "Taille entreprise"],
                ["telephone", "Telephone"],
              ].map(([name, label]) => (
                <div key={name} className={name === "email" || name === "password" ? "" : ""}>
                  <label className="mb-2 block text-sm font-semibold text-foreground" htmlFor={name}>
                    {label}
                  </label>
                  <input
                    id={name}
                    type={name === "password" ? "password" : name === "email" ? "email" : "text"}
                    autoComplete={name === "password" ? "new-password" : undefined}
                    className={inputClass}
                    {...register(name)}
                  />
                  {errors[name] ? <p className="mt-2 text-sm text-red-600">{errors[name].message}</p> : null}
                </div>
              ))}

              <button className="btn btn-hero h-12 text-base md:col-span-2" type="submit" disabled={loading}>
                {loading ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : null}
                Creer mon compte
                {!loading ? <ArrowRight className="ml-2 h-5 w-5" /> : null}
              </button>
            </form>

            <p className="mt-5 text-center text-sm text-muted-foreground">
              Deja inscrit ?{" "}
              <Link className="font-semibold text-primary hover:underline" to="/client/login">
                Se connecter
              </Link>
            </p>
          </div>
        </section>
      </div>
    </main>
  );
}
