import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRight, Loader2, LockKeyhole } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { z } from "zod";
import { login as loginRequest } from "../../api/authApi.js";
import useAuthStore from "../../store/authStore.js";

const loginSchema = z.object({
  email: z.string().email("Email invalide"),
  password: z.string().min(6, "Le mot de passe doit contenir au moins 6 caractères"),
});

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const authLogin = useAuthStore((state) => state.login);
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const [loading, setLoading] = useState(false);
  const redirectTo = location.state?.from?.pathname ?? "/admin";

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

  if (isAuthenticated) {
    return <Navigate to="/admin" replace />;
  }

  const onSubmit = async (values) => {
    setLoading(true);

    try {
      const response = await loginRequest(values.email, values.password);
      const user = {
        email: response.email,
        nom: response.nom,
        prenom: response.prenom,
        roles: response.roles,
      };

      authLogin(response.token, user);
      toast.success("Connexion réussie");
      navigate(redirectTo, { replace: true });
    } catch (error) {
      const message =
        error.response?.status === 401
          ? "Email ou mot de passe incorrect"
          : "Impossible de se connecter au serveur";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-muted/30">
      <div className="grid min-h-screen lg:grid-cols-[1fr_0.9fr]">
        <section className="relative hidden overflow-hidden bg-primary lg:block">
          <div
            className="absolute inset-0 bg-cover bg-center opacity-25"
            style={{
              backgroundImage:
                "url(https://images.unsplash.com/photo-1552664730-d307ca884978?w=1600)",
            }}
          />
          <div className="absolute inset-0 bg-gradient-to-br from-primary via-primary/90 to-secondary/80" />
          <div className="relative z-10 flex h-full flex-col justify-between p-12 text-primary-foreground">
            <img
              src="/images/Logo_HWC-J-9-1DQr.png"
              alt="Harmony Works Consulting"
              className="h-14 w-fit brightness-0 invert"
            />
            <div className="max-w-xl">
              <p className="mb-4 text-sm font-semibold uppercase tracking-wider text-primary-foreground/70">
                Dashboard Admin
              </p>
              <h1 className="font-display text-5xl font-bold leading-tight">
                Gérez le contenu HWC depuis un espace sécurisé.
              </h1>
            </div>
          </div>
        </section>

        <section className="flex items-center justify-center px-4 py-10">
          <div className="w-full max-w-md rounded-2xl border border-border bg-card p-8 shadow-elevated">
            <div className="mb-8 text-center">
              <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-secondary/10">
                <LockKeyhole className="h-7 w-7 text-secondary" />
              </div>
              <h2 className="font-display text-3xl font-bold text-foreground">
                Connexion admin
              </h2>
              <p className="mt-2 text-sm text-muted-foreground">
                Accédez au back-office Harmony Works Consulting.
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
                  className="h-12 w-full rounded-lg border border-border bg-background px-4 text-sm outline-none transition-colors focus:border-secondary focus:ring-2 focus:ring-secondary/20"
                  {...register("email")}
                />
                {errors.email ? (
                  <p className="mt-2 text-sm text-red-600">{errors.email.message}</p>
                ) : null}
              </div>

              <div>
                <label className="mb-2 block text-sm font-semibold text-foreground" htmlFor="password">
                  Mot de passe
                </label>
                <input
                  id="password"
                  type="password"
                  autoComplete="current-password"
                  className="h-12 w-full rounded-lg border border-border bg-background px-4 text-sm outline-none transition-colors focus:border-secondary focus:ring-2 focus:ring-secondary/20"
                  {...register("password")}
                />
                {errors.password ? (
                  <p className="mt-2 text-sm text-red-600">{errors.password.message}</p>
                ) : null}
              </div>

              <button className="btn btn-hero h-12 w-full text-base" type="submit" disabled={loading}>
                {loading ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : null}
                Se connecter
                {!loading ? <ArrowRight className="ml-2 h-5 w-5" /> : null}
              </button>
            </form>
          </div>
        </section>
      </div>
    </main>
  );
}
