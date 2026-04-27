import { MessageCircle } from "lucide-react";

export default function FloatingContact() {
  const openWhatsApp = () => {
    const phone = "+212600000000";
    const message = "Bonjour, je souhaite en savoir plus sur vos services.";
    window.open(`https://wa.me/${phone}?text=${encodeURIComponent(message)}`, "_blank");
  };

  return (
    <button
      className="fixed bottom-6 right-6 z-50 flex h-14 w-14 items-center justify-center rounded-full bg-[#25D366] text-white shadow-elevated transition-transform hover:scale-110 animate-pulse-glow"
      onClick={openWhatsApp}
      aria-label="Contacter sur WhatsApp"
    >
      <MessageCircle className="h-6 w-6" />
    </button>
  );
}
