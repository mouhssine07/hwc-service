package hwc_backend.coach.marketing.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSessionState {

    @Builder.Default
    private String serviceId = "MARKETING_STRATEGY";
    private String sessionId;
    @Builder.Default
    private MarketingStrategyStage stage = MarketingStrategyStage.COMPANY_DISCOVERY;
    @Builder.Default
    private Company company = new Company();
    @Builder.Default
    private Objectives objectives = new Objectives();
    @Builder.Default
    private Audit audit = new Audit();
    @Builder.Default
    private TargetAudience targetAudience = new TargetAudience();
    @Builder.Default
    private Positioning positioning = new Positioning();
    @Builder.Default
    private Budget budget = new Budget();
    @Builder.Default
    private List<Map<String, Object>> recommendedChannels = new ArrayList<>();
    @Builder.Default
    private List<Map<String, Object>> weeklyActions = new ArrayList<>();
    @Builder.Default
    private List<Map<String, Object>> kpis = new ArrayList<>();
    @Builder.Default
    private List<String> missingInformation = new ArrayList<>();
    @Builder.Default
    private List<String> assumptions = new ArrayList<>();
    @Builder.Default
    private Confidence confidence = Confidence.LOW;
    private boolean completed;

    public enum Confidence { LOW, MEDIUM, HIGH }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Company {
        private String sector;
        private List<String> productsOrServices = new ArrayList<>();
        private String businessModel;
        private String location;
        private String size;
        private String marketingMaturity;
        private List<String> mainCompetitors = new ArrayList<>();
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Objectives {
        private String type;
        private Object currentValue;
        private Object targetValue;
        private String deadline;
        private String smartStatement;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Audit {
        private Object website;
        private List<String> currentChannels = new ArrayList<>();
        private Map<String, Object> currentResults = new LinkedHashMap<>();
        private Boolean trackingAvailable;
        private Boolean crmAvailable;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class TargetAudience {
        private List<String> segments = new ArrayList<>();
        private Map<String, Object> primaryPersona;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Positioning {
        private String valueProposition;
        private List<String> differentiators = new ArrayList<>();
        private List<String> proofPoints = new ArrayList<>();
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Budget {
        private BigDecimal monthlyAmount;
        private String currency;
        private List<String> teamResources = new ArrayList<>();
        private BigDecimal weeklyTimeHours;
        private String leadHandlingCapacity;
    }
}
