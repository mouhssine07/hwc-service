package hwc_backend.coach.marketing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MarketingActionItemView(
        Long id,
        String title,
        int weekNumber,
        LocalDate dueDate,
        String status,
        String proofReference,
        LocalDateTime completedAt
) { }
