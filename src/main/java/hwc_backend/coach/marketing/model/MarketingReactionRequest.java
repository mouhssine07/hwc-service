package hwc_backend.coach.marketing.model;

import jakarta.validation.constraints.NotNull;

public record MarketingReactionRequest(@NotNull Reaction reaction) {
    public enum Reaction { CORRECT, CLARIFY, REVISE }
}
