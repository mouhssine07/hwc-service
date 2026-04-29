import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getPublicService, getPublicServiceImages, getPublicSousService } from "../api/publicContentApi.js";
import ServiceDetailPage from "../components/ServiceDetailPage.jsx";

export default function PublicServicePage() {
  const { serviceId } = useParams();
  const [service, setService] = useState(null);
  const [heroImage, setHeroImage] = useState("");
  const [error, setError] = useState(false);

  useEffect(() => {
    let ignore = false;

    async function loadService() {
      try {
        const [serviceData, images] = await Promise.all([getPublicService(serviceId), getPublicServiceImages()]);
        if (!ignore) {
          setService(serviceData);
          setHeroImage(Array.isArray(images) ? images.find((item) => item.serviceId === serviceData.id)?.imageUrl ?? "" : "");
        }
      } catch {
        if (!ignore) {
          setError(true);
        }
      }
    }

    loadService();

    return () => {
      ignore = true;
    };
  }, [serviceId]);

  if (error) {
    return <PublicContentError />;
  }

  if (!service) {
    return <PublicContentLoading />;
  }

  return (
    <ServiceDetailPage
      eyebrow="Service HWC"
      finalCta="Parlons de ce service"
      heroImage={heroImage}
      introText={service.description}
      introTitle={service.titre}
      subtitle={service.accroche || service.description}
      title={service.titre}
      bullets={service.etiquettes?.map((item) => item.nom) ?? []}
    />
  );
}

export function PublicSousServicePage() {
  const { sousServiceId } = useParams();
  const [sousService, setSousService] = useState(null);
  const [error, setError] = useState(false);

  useEffect(() => {
    let ignore = false;

    async function loadSousService() {
      try {
        const data = await getPublicSousService(sousServiceId);
        if (!ignore) {
          setSousService(data);
        }
      } catch {
        if (!ignore) {
          setError(true);
        }
      }
    }

    loadSousService();

    return () => {
      ignore = true;
    };
  }, [sousServiceId]);

  if (error) {
    return <PublicContentError />;
  }

  if (!sousService) {
    return <PublicContentLoading />;
  }

  return (
    <ServiceDetailPage
      eyebrow={sousService.serviceTitre || "Sous-service HWC"}
      finalCta="Parlons de ce besoin"
      introText={sousService.descriptionComplete || sousService.description || sousService.accroche}
      introTitle={sousService.titre}
      subtitle={sousService.accroche || sousService.description}
      title={sousService.titre}
    />
  );
}

function PublicContentLoading() {
  return (
    <section className="pt-32">
      <div className="section-container py-16 text-center text-muted-foreground">Chargement...</div>
    </section>
  );
}

function PublicContentError() {
  return (
    <section className="pt-32">
      <div className="section-container py-16 text-center">
        <h1 className="mb-4 font-display text-4xl font-bold">Contenu indisponible</h1>
        <p className="mb-8 text-muted-foreground">La page demandee n'est pas disponible pour le moment.</p>
        <Link className="btn btn-hero" to="/">
          Retour a l'accueil
        </Link>
      </div>
    </section>
  );
}
