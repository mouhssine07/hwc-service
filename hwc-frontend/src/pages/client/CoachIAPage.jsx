import { ArrowLeft, ArrowRight, BarChart3, Bot, CalendarDays, ChevronDown, Info, Loader2, Settings, Sparkles, Target } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { completeCoachObjective, getCoachHistory, getCurrentCoachWeek, updateCoachObjectiveProgress } from "../../api/coachApi.js";
import { getDiagnosticHistory } from "../../api/diagnosticApi.js";
import ChatbotWindow from "../../components/chat/ChatbotWindow.jsx";
import useClientAuthStore from "../../store/clientAuthStore.js";

export default function CoachIAPage() {
  const navigate = useNavigate();
  const clientUser = useClientAuthStore((state) => state.clientUser);
  const [week, setWeek] = useState(null);
  const [history, setHistory] = useState([]);
  const [diagnostics, setDiagnostics] = useState([]);
  const [selectedDiagnosticId, setSelectedDiagnosticId] = useState("");
  const [loading, setLoading] = useState(true);
  const [completingId, setCompletingId] = useState(null);
  const [focusedObjective, setFocusedObjective] = useState(null);
  const initialLoadStarted = useRef(false);

  const loadCoach = async (diagnosticId) => {
    const [currentWeek, allWeeks] = await Promise.all([getCurrentCoachWeek(diagnosticId), getCoachHistory(diagnosticId)]);
    setWeek(currentWeek); setHistory(allWeeks);
  };

  useEffect(() => {
    if (initialLoadStarted.current) return;
    initialLoadStarted.current = true;
    getDiagnosticHistory().then((items) => {
      const finalized = items.filter((item) => item.statut === "TERMINE");
      setDiagnostics(finalized);
      const latestId = finalized[0]?.diagnosticId;
      setSelectedDiagnosticId(latestId ? String(latestId) : "");
      return loadCoach(latestId);
    }).catch((error) => toast.error(error.response?.data?.message ?? "Impossible de charger votre Coach IA"))
      .finally(() => setLoading(false));
  }, []);

  const handleDiagnosticChange = async (event) => {
    const diagnosticId = event.target.value; setSelectedDiagnosticId(diagnosticId); setLoading(true);
    try { await loadCoach(diagnosticId); } catch (error) { toast.error(error.response?.data?.message ?? "Impossible de charger ce diagnostic."); }
    finally { setLoading(false); }
  };

  const updateLocalWeek = (updatedWeek) => {
    setWeek(updatedWeek); setHistory((items) => items.map((item) => (item.id === updatedWeek.id ? updatedWeek : item)));
  };

  const handleComplete = async (objectiveId) => {
    setCompletingId(objectiveId);
    try { updateLocalWeek(await completeCoachObjective(objectiveId)); toast.success("Objectif validé. Continuez ainsi !"); }
    catch { toast.error("Impossible de mettre à jour cet objectif."); } finally { setCompletingId(null); }
  };

  const handleProgress = async (objectiveId, progression) => {
    setCompletingId(objectiveId);
    try { updateLocalWeek(await updateCoachObjectiveProgress(objectiveId, progression)); toast.success("Progression enregistrée."); }
    catch (error) { toast.error(error.response?.data?.message ?? "Impossible d'enregistrer la progression."); } finally { setCompletingId(null); }
  };

  if (loading) return <main className="min-h-screen bg-[#f8fbfb] px-4 py-10"><div className="mx-auto flex max-w-5xl items-center gap-3 rounded-2xl border bg-white p-6"><Loader2 className="h-5 w-5 animate-spin text-primary" /> Chargement de votre Coach IA...</div></main>;

  if (!week?.disponible) return <main className="min-h-screen bg-[#f8fbfb] px-4 py-8"><div className="mx-auto max-w-4xl"><button type="button" className="mb-6 inline-flex items-center gap-2 font-bold text-slate-500" onClick={() => navigate("/client/diagnostic")}><ArrowLeft className="h-4 w-4" /> Espace diagnostic</button><section className="rounded-3xl border bg-white p-8 shadow-elevated"><Sparkles className="h-9 w-9 text-primary" /><p className="mt-5 text-sm font-bold uppercase tracking-wider text-primary">Coach IA</p><h1 className="mt-2 text-3xl font-bold">Votre plan hebdomadaire arrive après votre diagnostic</h1><p className="mt-4 text-slate-500">{week?.message ?? "Finalisez votre diagnostic pour recevoir trois objectifs adaptés à vos priorités."}</p><button type="button" className="btn btn-hero mt-6" onClick={() => navigate("/client/diagnostic")}>Lancer un diagnostic</button></section></div></main>;

  const percentage = week.objectifsTotal ? Math.round((week.objectifsCompletes / week.objectifsTotal) * 100) : 0;
  const firstName = clientUser?.prenom ?? clientUser?.nom ?? "client";
  const icons = [Target, CalendarDays, BarChart3];
  const diagnostic = diagnostics.find((item) => String(item.diagnosticId) === String(selectedDiagnosticId));

  return <main className="coach-reference-page h-screen overflow-hidden bg-[#f9fcfc] p-0 text-[#092f38] xl:p-[2px]">
    <div className="relative mx-auto h-full max-w-[1572px] overflow-hidden rounded-none border-[#8ba6a9] bg-white shadow-[0_20px_60px_rgba(16,54,61,.08)] xl:rounded-[20px] xl:border">
      <div className="pointer-events-none absolute right-0 top-0 h-[520px] w-[650px] bg-[radial-gradient(circle_at_70%_45%,#effafa_0,transparent_60%)]" />
      <div className="relative flex h-full flex-col px-4 py-3 sm:px-6 md:px-[5.6vw] md:py-[2vh]">
        <header className="grid grid-cols-[70px_1fr_70px] items-start md:grid-cols-[110px_1fr_180px]">
          <img src="/images/coach/coach-robot.png" alt="Coach IA" className="h-[82px] w-[82px] object-contain md:h-[105px] md:w-[105px]" />
          <div className="text-center"><h1 className="text-[22px] font-extrabold uppercase tracking-[.22em] text-[#117b79] md:text-[35px]">Mon Coach IA</h1><p className="mt-1 hidden text-[16px] text-[#6e8491] sm:block">Votre assistant intelligent pour des performances marketing exceptionnelles</p></div>
          <div className="flex items-center justify-end gap-3 pt-2"><div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#e0efee] font-bold text-[#117b79]">{firstName[0]?.toUpperCase()}</div><span className="hidden text-[18px] font-semibold md:block">{firstName} {clientUser?.nom?.[0] ? `${clientUser.nom[0]}.` : ""}</span><ChevronDown className="hidden h-5 w-5 md:block" /></div>
        </header>

        <section className="mt-[1vh] grid min-h-0 flex-1 grid-cols-[1.15fr_.75fr] items-center gap-3 lg:grid-cols-[1.05fr_.7fr_1fr] lg:gap-6">
          <div className="lg:pl-[72px]"><h2 className="text-[36px] font-bold leading-[1.2] md:text-[44px]">Bonjour {firstName} !</h2><p className="mt-4 max-w-[500px] text-[36px] font-bold leading-[1.25] md:text-[43px]">Prêt à <span className="text-[#117b79]">optimiser</span> vos performances ?</p><p className="mt-6 max-w-[480px] text-[16px] leading-7 text-[#6e8491]">Votre assistant IA analyse vos données marketing en temps réel et vous accompagne vers de meilleurs résultats.</p><div className="mt-4 h-[3px] w-10 rounded bg-[#117b79]" /></div>
          <div className="mx-auto w-full max-w-[325px] rounded-[24px] border border-[#dfe9eb] bg-white px-8 py-7 text-center shadow-[0_15px_40px_rgba(25,66,75,.08)]"><p className="flex items-center justify-center gap-3 text-[18px] font-bold">Progrès semaine <Info className="h-4 w-4 text-[#77909a]" /></p><div className="relative mx-auto mt-5 flex h-[150px] w-[150px] items-center justify-center rounded-full" style={{ background: `conic-gradient(#117b79 ${percentage * 3.6}deg,#e8eeee 0)` }}><div className="flex h-[125px] w-[125px] items-center justify-center rounded-full bg-white text-[32px] font-bold">{percentage}%</div></div><p className="mt-3 text-[20px] font-semibold">{diagnostic?.scoreGlobal ?? week.progressionProgramme ?? 0} / 100</p><p className="text-[14px] text-[#6e8491]">Objectif hebdomadaire</p></div>
          <div className="relative hidden h-full max-h-[35vh] lg:block"><div className="absolute inset-0 rounded-[50%] bg-[#f1fafa] opacity-80" /><img src="/images/coach/coach-robot.png" alt="Robot Coach IA" className="absolute inset-0 mx-auto h-full w-full object-contain" /></div>
        </section>

        <section className="mt-5"><h2 className="text-[18px] font-bold uppercase">Démarrez la conversation</h2><div className="mt-3 h-[3px] w-10 bg-[#117b79]" /><div className="mt-4 grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {week.objectifs.slice(0, 3).map((objectif, index) => { const Icon = icons[index] ?? Target; return <article key={objectif.id} className="relative min-h-[315px] rounded-[20px] border border-[#e1eaeb] bg-white p-7 shadow-[0_12px_30px_rgba(24,65,73,.07)]"><div className="flex min-h-[70px] items-center gap-5"><span className="flex h-14 w-14 shrink-0 items-center justify-center rounded-xl bg-[#e8f5f4] text-[#117b79]"><Icon className="h-8 w-8" /></span><h3 className="text-[17px] font-bold leading-6">{objectif.titre}</h3></div><div className="mt-3 border-t border-[#e4ebec]" /><p className="mt-4 text-[15px] font-medium text-[#117b79]">{objectif.quantiteRealisee ?? 0} / {objectif.quantiteCible ?? 1} {objectif.unite ?? "actions"}</p><div className="absolute bottom-7 left-7 right-7 flex items-end justify-between"><div className="flex h-14 items-end gap-2">{[35,55,42,70,58,82,66].map((height, i) => <span key={i} className="w-2.5 bg-[#117b79] opacity-25" style={{ height }} />)}</div><button type="button" aria-label={`Ouvrir ${objectif.titre}`} onClick={() => setFocusedObjective(objectif)} className="flex h-11 w-11 items-center justify-center rounded-full bg-[#168b88] text-white"><ArrowRight /></button></div></article>; })}
          <article className="relative min-h-[315px] rounded-[20px] border border-[#e1eaeb] bg-white p-7 shadow-[0_12px_30px_rgba(24,65,73,.07)]"><div className="flex min-h-[70px] items-center gap-5"><span className="flex h-14 w-14 items-center justify-center rounded-xl bg-[#e8f5f4] text-[#117b79]"><BarChart3 className="h-8 w-8" /></span><h3 className="text-[17px] font-bold">Préparation de mon score total</h3></div><div className="mt-3 border-t border-[#e4ebec]" /><p className="mt-4 text-[15px] font-medium text-[#117b79]">Analyse prédictive</p><div className="absolute bottom-7 left-7 right-7 flex items-end justify-between"><div className="flex h-14 items-end gap-2">{[20,38,25,46,60,34,72,57].map((height, i) => <span key={i} className="w-2.5 bg-[#117b79]" style={{ height, opacity: .25 + i * .07 }} />)}</div><button type="button" aria-label="Ouvrir le tableau de bord" onClick={() => navigate("/client/dashboard")} className="flex h-11 w-11 items-center justify-center rounded-full bg-[#168b88] text-white"><ArrowRight /></button></div></article>
        </div></section>

        <footer className="mt-6 flex min-h-[72px] items-center justify-between rounded-[20px] border border-[#e2eaeb] bg-white px-7 shadow-[0_10px_25px_rgba(24,65,73,.06)]"><button type="button" onClick={() => navigate("/client/dashboard")} className="flex items-center gap-5 text-[16px] font-semibold text-[#117b79]"><ArrowLeft /> Retour au tableau de bord</button><div className="hidden items-center gap-3 md:flex"><select aria-label="Diagnostic suivi" value={selectedDiagnosticId} onChange={handleDiagnosticChange} className="rounded-lg border-0 bg-transparent text-sm text-[#117b79] outline-none">{diagnostics.map((item) => <option key={item.diagnosticId} value={item.diagnosticId}>Diagnostic #{item.diagnosticId}</option>)}</select><Settings className="h-6 w-6 text-[#117b79]" /><span className="text-[16px] font-semibold text-[#117b79]">Paramètres</span></div></footer>
      </div>
      {focusedObjective ? <><button type="button" aria-label="Fermer le suivi" className="fixed inset-0 z-[60] bg-[#092f38]/35 backdrop-blur-sm" onClick={() => setFocusedObjective(null)} /><ChatbotWindow focused coachObjective={focusedObjective} initialDiagnosticId={Number(selectedDiagnosticId)} onClose={() => setFocusedObjective(null)} /></> : null}
    </div>
  </main>;
}
