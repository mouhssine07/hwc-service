package hwc_backend.dto.coach;

import java.time.LocalDate;
import java.util.List;

public record CoachSemaineDTO(
        boolean disponible,
        String message,
        Long id,
        Long diagnosticId,
        LocalDate semaineDebut,
        LocalDate semaineFin,
        String conseilSemaine,
        int numeroSemaine,
        int dureeProgrammeSemaines,
        int progressionProgramme,
        String bilanSemaine,
        String insightIa,
        int objectifsCompletes,
        int objectifsTotal,
        List<CoachObjectifDTO> objectifs
) {
}
