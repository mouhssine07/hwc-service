import { useMemo, useState } from "react";

export default function MarketingStructuredInput({ spec, disabled, onApply }) {
  const [value, setValue] = useState(spec?.config?.min ?? 0);
  const [selected, setSelected] = useState([]);
  const [persona, setPersona] = useState({ age: 35, interests: [], need: "", objection: "" });
  const [point, setPoint] = useState({ x: 50, y: 50 });
  const text = useMemo(() => {
    if (!spec || spec.inputType === "free_text") return "";
    if (spec.inputType === "slider") return `${spec.label} : ${value} ${spec.config?.unit ?? ""}`;
    if (spec.inputType === "channel_picker") return `Canaux retenus : ${selected.join(", ")}`;
    if (spec.inputType === "persona_builder") return `Persona prioritaire : âge ${persona.age} ans, intérêts ${persona.interests.join(", ") || "à préciser"}, besoin ${persona.need || "à préciser"}, objection ${persona.objection || "à préciser"}.`;
    return `Positionnement : prix ${point.x}/100, différenciation ${100 - point.y}/100.`;
  }, [spec, value, selected, persona, point]);
  if (!spec || spec.inputType === "free_text") return null;
  const toggle = (item, list, setter) => setter(list.includes(item) ? list.filter((v) => v !== item) : [...list, item]);
  return <div className="mb-3 rounded-2xl border border-primary/20 bg-[#f7fbfb] p-4">
    <p className="mb-3 text-sm font-bold text-[#173f46]">{spec.label}</p>
    {spec.inputType === "slider" ? <><input className="w-full accent-primary" type="range" min={spec.config.min} max={spec.config.max} step={spec.config.step} value={value} onChange={(e) => setValue(e.target.value)} /><p className="text-center text-sm font-bold text-primary">{value} {spec.config.unit}</p></> : null}
    {spec.inputType === "channel_picker" ? <div className="flex flex-wrap gap-2">{spec.options.map((item) => <button type="button" key={item} onClick={() => toggle(item, selected, setSelected)} className={`rounded-xl border px-3 py-2 text-xs font-bold ${selected.includes(item) ? "border-primary bg-primary text-white" : "bg-white text-primary"}`}>{item}</button>)}</div> : null}
    {spec.inputType === "persona_builder" ? <div className="space-y-3"><label className="block text-xs">Âge : {persona.age}<input className="block w-full accent-primary" type="range" min="18" max="75" value={persona.age} onChange={(e) => setPersona({ ...persona, age: e.target.value })} /></label><div className="flex flex-wrap gap-2">{spec.options.map((item) => <button type="button" key={item} onClick={() => setPersona({ ...persona, interests: persona.interests.includes(item) ? persona.interests.filter((v) => v !== item) : [...persona.interests, item] })} className={`rounded-full border px-3 py-1 text-xs ${persona.interests.includes(item) ? "bg-primary text-white" : "bg-white"}`}>{item}</button>)}</div><input className="w-full rounded-lg border p-2 text-sm" placeholder="Besoin principal" value={persona.need} onChange={(e) => setPersona({ ...persona, need: e.target.value })} /><input className="w-full rounded-lg border p-2 text-sm" placeholder="Objection principale" value={persona.objection} onChange={(e) => setPersona({ ...persona, objection: e.target.value })} /></div> : null}
    {spec.inputType === "matrix_2x2" ? <div className="relative h-44 cursor-crosshair border bg-white" onClick={(e) => { const r=e.currentTarget.getBoundingClientRect(); setPoint({x:Math.round((e.clientX-r.left)/r.width*100),y:Math.round((e.clientY-r.top)/r.height*100)}); }}><div className="absolute left-1/2 top-0 h-full border-l"/><div className="absolute left-0 top-1/2 w-full border-t"/><span className="absolute h-4 w-4 -translate-x-1/2 -translate-y-1/2 rounded-full bg-primary" style={{left:`${point.x}%`,top:`${point.y}%`}}/><span className="absolute bottom-1 left-2 text-[10px]">Prix bas → premium</span><span className="absolute right-1 top-2 text-[10px]">Différenciation forte</span></div> : null}
    <button type="button" disabled={disabled || (spec.inputType === "channel_picker" && !selected.length)} onClick={() => { const data = spec.inputType === "slider" ? { value: Number(value), unit: spec.config.unit, targetPath: spec.targetPath } : spec.inputType === "channel_picker" ? { channels: selected, targetPath: spec.targetPath } : spec.inputType === "persona_builder" ? { ...persona, targetPath: spec.targetPath } : { ...point, targetPath: spec.targetPath }; window.sessionStorage.setItem("hwc_marketing_structured_input", JSON.stringify(data)); onApply(text); }} className="mt-3 w-full rounded-xl bg-primary px-4 py-2 text-sm font-bold text-white disabled:opacity-50">Utiliser cette réponse</button>
    <p className="mt-2 text-[11px] text-slate-400">Vous pouvez encore modifier le texte libre avant l’envoi.</p>
  </div>;
}
