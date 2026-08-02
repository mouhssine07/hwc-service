package hwc_backend.scheduler;

import hwc_backend.service.CoachIAService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoachIAScheduler {

    private final CoachIAService coachIAService;

    @Scheduled(cron = "${coach.objectives-cron:0 0 8 * * MON}", zone = "${coach.time-zone:Africa/Casablanca}")
    public void generateMondayObjectives() {
        coachIAService.generateWeeklyObjectivesForActiveClients();
    }

    @Scheduled(cron = "${coach.summary-cron:0 0 17 * * FRI}", zone = "${coach.time-zone:Africa/Casablanca}")
    public void prepareFridaySummaries() {
        coachIAService.prepareWeeklySummaries();
    }
}
