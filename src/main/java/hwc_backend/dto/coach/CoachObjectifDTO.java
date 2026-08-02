package hwc_backend.dto.coach;

import java.time.LocalDateTime;

public record CoachObjectifDTO(
        Long id,
        Integer ordre,
        String titre,
        String description,
        String indicateur,
        Integer quantiteCible,
        Integer quantiteRealisee,
        String unite,
        String commentaireClient,
        boolean termine,
        LocalDateTime dateCompletion,
        LocalDateTime dateMiseAJour
) {
}
