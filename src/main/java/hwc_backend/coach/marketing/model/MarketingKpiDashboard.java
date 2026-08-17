package hwc_backend.coach.marketing.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public record MarketingKpiDashboard(List<Series> series, String comment, String nextPriority) {
    public record Series(String name, String unit, List<Point> points, String trend) { }
    public record Point(BigDecimal value, LocalDateTime measuredAt) { }
}
