package hwc_backend.service;

import hwc_backend.dto.coach.CoachSemaineDTO;
import hwc_backend.dto.coach.CoachObjectifProgressionDTO;
import java.util.List;

public interface CoachIAService {

    CoachSemaineDTO getCurrentWeek(String email, Long diagnosticId);

    CoachSemaineDTO completeObjective(Long objectiveId, String email);

    CoachSemaineDTO updateObjectiveProgress(Long objectiveId, CoachObjectifProgressionDTO progression, String email);

    List<CoachSemaineDTO> getHistory(String email, Long diagnosticId);

    void generateWeeklyObjectivesForActiveClients();

    void prepareWeeklySummaries();
}
