package hwc_backend.coach.marketing.model;

import java.util.List;
import java.util.Map;

public record MarketingInputSpec(
        String inputType,
        String targetPath,
        String label,
        Map<String, Object> config,
        List<String> options
) { }
