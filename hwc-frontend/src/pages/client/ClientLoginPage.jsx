import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRight, Loader2, LockKeyhole, UserPlus } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { z } from "zod";
import { clientLogin as loginRequest } from "../../api/clientAuthApi.js";
import useClientAuthStore from "../../store/clientAuthStore.js";

const loginSchema = z.object({
  email: z.string().email("Email invalide"),
  password: z.string().min(6, "Le mot de passe doit contenir au moins 6 caracteres"),
});

export default function ClientLoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const authLogin = useClientAuthStore((state) => state.clientLogin);
  const isClientAuthenticated = useClientAuthStore((state) => state.isClientAuthenticated);
  const [loading, setLoading] = useState(false);
  const redirectTo = location.state?.from?.pathname ?? "/client/diagnostic";

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  if (isClientAuthenticated) {
    return <Navigate to="/client/diagnostic" replace />;
  }

  const onSubmit = async (values) => {
    setLoading(true);

    try {
      const response = await loginRequest(values.email, values.password);
      authLogin(response.token, {
        email: response.email,
        nom: response.nom,
        prenom: response.prenom,
        roles: response.roles,
      });
      toast.success("Connexion client reussie");
      navigate(redirectTo, { replace: true });
    } catch (error) {
      const message =
        error.response?.status === 401 || error.response?.status === 403
          ? "Email ou mot de passe incorrect"
          : "Impossible de se connecter au serveur";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-muted/30">
      <div className="grid min-h-screen lg:grid-cols-[0.95fr_1.05fr]">
        <section className="relative hidden overflow-hidden bg-primary lg:block">
          <div
            className="absolute inset-0 bg-cover bg-center opacity-25"
            style={{
              backgroundImage:
                "url(https://images.unsplash.com/photo-1551434678-e076c223a692?w=1600)",
            }}
          />
          <div className="absolute inset-0 bg-gradient-to-br from-primary via-primary/90 to-secondary/80" />
          <div className="relative z-10 flex h-full flex-col justify-between p-12 text-primary-foreground">
            <Link to="/" className="w-fit">
              <img
                src="/images/Logo_HWC-J-9-1DQr.png"
                alt="Harmony Works Consulting"
                className="h-14 w-fit brightness-0 invert"
              />
            </Link>
            <div className="max-w-xl">
              <p className="mb-4 text-sm font-semibold uppercase tracking-wider text-primary-foreground/70">
                Diagnostic intelligent
              </p>
              <h1 className="font-display text-5xl font-bold leading-tight">
                Accedez a votre espace client et pilotez votre maturite.
              </h1>
            </div>
          </div>
        </section>

        <section className="flex items-center justify-center px-4 py-10">
          <div className="w-full max-w-md rounded-2xl border border-border bg-card p-8 shadow-elevated">
            <div className="mb-8 text-center">
              <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
                <LockKeyhole className="h-7 w-7 text-primary" />
              </div>
              <h2 className="font-display text-3xl font-bold text-foreground">Connexion client</h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Retrouvez votre diagnostic et vos resultats HWC.
              </p>
            </div>

            <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
              <div>
                <label className="mb-2 block text-sm font-semibold text-foreground" htmlFor="email">
                  Email
                </label>
                <input
                  id="email"
                  type="email"
                  autoComplete="email"
                  className="h-12 w-full rounded-lg border border-border bg-background px-4 text-sm outline-none transition-colors focus:border-primary focus:ring-2 focus:ring-primary/20"
                  {...register("email")}
                />
                {errors.email ? <p className="mt-2 text-sm text-red-600">{errors.email.message}</p> : null}
              </div>

              <div>
                <label className="mb-2 block text-sm font-semibold text-foreground" htmlFor="password">
                  Mot de passe
                </label>
                <input
                  id="password"
                  type="password"
                  autoComplete="current-password"
                  className="h-12 w-full rounded-lg border border-border bg-background px-4 text-sm outline-none transition-colors focus:border-primary focus:ring-2 focus:ring-primary/20"
                  {...register("password")}
                />
                {errors.password ? <p className="mt-2 text-sm text-red-600">{errors.password.message}</p> : null}
              </div>

              <button className="btn btn-hero h-12 w-full text-base" type="submit" disabled={loading}>
                {loading ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : null}
                Se connecter
                {!loading ? <ArrowRight className="ml-2 h-5 w-5" /> : null}
              </button>
            </form>

            <Link
              to="/client/register"
              className="mt-5 flex h-11 items-center justify-center gap-2 rounded-lg border border-border text-sm font-semibold text-primary transition-colors hover:bg-accent/40"
            >
              <UserPlus className="h-4 w-4" />
              Creer un compte client
            </Link>
          </div>
        </section>
      </div>
    </main>
  );
}
