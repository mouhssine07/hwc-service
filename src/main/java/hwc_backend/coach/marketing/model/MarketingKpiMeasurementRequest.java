package hwc_backend.coach.marketing.model;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record MarketingKpiMeasurementRequest(@NotBlank @Size(max=120) String name, @NotNull BigDecimal value, @Size(max=40) String unit, LocalDateTime measuredAt) { }
