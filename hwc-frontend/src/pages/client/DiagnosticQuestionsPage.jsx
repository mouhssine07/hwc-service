import { ArrowLeft, ArrowRight, CheckCircle2, Loader2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import {
  answerDiagnosticQuestion,
  finalizeDiagnostic,
  getDiagnosticQuestions,
} from "../../api/diagnosticApi.js";

export default function DiagnosticQuestionsPage() {
  const { diagnosticId } = useParams();
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [answers, setAnswers] = useState({});
  const [currentIndex, setCurrentIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    getDiagnosticQuestions()
      .then(setCategories)
      .catch(() => toast.error("Impossible de charger les questions"))
      .finally(() => setLoading(false));
  }, []);

  const questions = useMemo(
    () =>
      categories.flatMap((category) =>
        category.questions.map((question) => ({
          ...question,
          categoryId: category.id,
          categoryName: category.nom,
          categoryIcon: category.icone,
        })),
      ),
    [categories],
  );

  const currentQuestion = questions[currentIndex];
  const answeredCount = Object.keys(answers).length;
  const progress = questions.length > 0 ? Math.round((answeredCount / questions.length) * 100) : 0;
  const canFinalize = questions.length > 0 && answeredCount === questions.length;

  const selectOption = async (questionId, optionReponseId) => {
    setSaving(true);

    try {
      await answerDiagnosticQuestion(diagnosticId, { questionId, optionReponseId });
      setAnswers((current) => ({ ...current, [questionId]: optionReponseId }));

      if (currentIndex < questions.length - 1) {
        setCurrentIndex((index) => index + 1);
      }
    } catch {
      toast.error("Impossible d'enregistrer la reponse");
    } finally {
      setSaving(false);
    }
  };

  const handleFinalize = async () => {
    setSaving(true);

    try {
      await finalizeDiagnostic(diagnosticId);
      navigate(`/client/diagnostic/${diagnosticId}/resultat`, { replace: true });
    } catch (error) {
      const message =
        error.response?.status === 500
          ? "Veuillez repondre a toutes les questions avant de finaliser"
          : "Impossible de finaliser le diagnostic";
      toast.error(message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-muted/30">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </main>
    );
  }

  return (
    <main className="min-h-screen bg-muted/30 px-4 py-8">
      <div className="mx-auto max-w-5xl">
        <div className="mb-6 flex flex-wrap items-center justify-between gap-3">
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-lg border border-border bg-card px-3 text-sm font-semibold text-muted-foreground hover:bg-muted hover:text-primary"
            onClick={() => navigate("/client/diagnostic")}
          >
            <ArrowLeft className="h-4 w-4" />
            Retour
          </button>
          <span className="rounded-full bg-card px-4 py-2 text-sm font-semibold text-primary shadow-card">
            {answeredCount}/{questions.length} reponses
          </span>
        </div>

        <div className="mb-6 overflow-hidden rounded-full bg-card">
          <div className="h-3 rounded-full bg-primary transition-all duration-500" style={{ width: `${progress}%` }} />
        </div>

        <section className="rounded-2xl border border-border bg-card p-6 shadow-elevated md:p-8">
          <div className="mb-6 flex flex-wrap items-center justify-between gap-3">
            <div>
              <p className="text-sm font-semibold uppercase tracking-wider text-primary">
                {currentQuestion?.categoryName}
              </p>
              <h1 className="mt-2 font-display text-3xl font-bold text-foreground">
                Question {currentIndex + 1}
              </h1>
            </div>
            <div className="rounded-xl bg-muted px-4 py-3 text-sm font-semibold text-muted-foreground">
              {progress}% complete
            </div>
          </div>

          <p className="mb-7 text-xl font-semibold leading-8 text-foreground">{currentQuestion?.texte}</p>

          <div className="grid gap-3">
            {currentQuestion?.options.map((option) => {
              const selected = answers[currentQuestion.id] === option.id;

              return (
                <button
                  key={option.id}
                  type="button"
                  className={`flex min-h-16 items-center justify-between rounded-xl border px-4 py-3 text-left transition-all ${
                    selected
                      ? "border-primary bg-primary/10 text-primary"
                      : "border-border bg-background hover:border-primary/40 hover:bg-accent/30"
                  }`}
                  onClick={() => selectOption(currentQuestion.id, option.id)}
                  disabled={saving}
                >
                  <span className="text-sm font-semibold">{option.texte}</span>
                  {selected ? <CheckCircle2 className="h-5 w-5 shrink-0" /> : null}
                </button>
              );
            })}
          </div>

          <div className="mt-8 flex flex-wrap justify-between gap-3">
            <button
              type="button"
              className="btn btn-outline-hero"
              disabled={currentIndex === 0}
              onClick={() => setCurrentIndex((index) => Math.max(0, index - 1))}
            >
              <ArrowLeft className="mr-2 h-4 w-4" />
              Precedent
            </button>

            {canFinalize ? (
              <button className="btn btn-hero" type="button" onClick={handleFinalize} disabled={saving}>
                {saving ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : null}
                Finaliser
              </button>
            ) : (
              <button
                type="button"
                className="btn btn-outline-hero"
                disabled={currentIndex >= questions.length - 1}
                onClick={() => setCurrentIndex((index) => Math.min(questions.length - 1, index + 1))}
              >
                Suivant
                <ArrowRight className="ml-2 h-4 w-4" />
              </button>
            )}
          </div>
        </section>
      </div>
    </main>
  );
}
