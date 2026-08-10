package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.model.MarketingStrategyStage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MarketingStageEvaluator {

    public MarketingSessionState evaluateAfterAnswer(MarketingSessionState state) {
        completeDerivedFields(state);
        List<String> missing = missingFor(state.getStage(), state);
        state.setMissingInformation(new ArrayList<>(missing));
        if (missing.isEmpty() && state.getStage() != MarketingStrategyStage.FINAL_DELIVERABLE
                && state.getStage() != MarketingStrategyStage.COMPLETED) {
            MarketingStrategyStage previous = state.getStage();
            state.setStage(next(previous));
            state.setMissingInformation(new ArrayList<>(missingFor(state.getStage(), state)));
        }
        state.setConfidence(confidence(state));
        state.setCompleted(state.getStage() == MarketingStrategyStage.COMPLETED);
        return state;
    }

    private void completeDerivedFields(MarketingSessionState state) {
        if (state.getStage() != MarketingStrategyStage.OBJECTIVES) return;
        MarketingSessionState.Objectives objectives = state.getObjectives();
        if (objectives.getTargetValue() != null
                && objectives.getDeadline() != null && !objectives.getDeadline().isBlank()
                && objectives.getType() != null && !objectives.getType().isBlank()
                && (objectives.getSmartStatement() == null || objectives.getSmartStatement().isBlank())) {
            objectives.setSmartStatement("Objectif " + objectives.getType() + " : atteindre "
                    + objectives.getTargetValue() + " " + objectives.getDeadline() + ".");
        }
    }

    public List<String> missingFor(MarketingStrategyStage stage, MarketingSessionState state) {
        List<String> missing = new ArrayList<>();
        switch (stage) {
            case COMPANY_DISCOVERY -> {
                required(missing, "company.sector", state.getCompany().getSector());
                requiredList(missing, "company.productsOrServices", state.getCompany().getProductsOrServices());
                required(missing, "company.businessModel", state.getCompany().getBusinessModel());
                required(missing, "company.location", state.getCompany().getLocation());
                required(missing, "company.size", state.getCompany().getSize());
            }
            case OBJECTIVES -> {
                required(missing, "objectives.type", state.getObjectives().getType());
                requiredObject(missing, "objectives.targetValue", state.getObjectives().getTargetValue());
                required(missing, "objectives.deadline", state.getObjectives().getDeadline());
                required(missing, "objectives.smartStatement", state.getObjectives().getSmartStatement());
            }
            case CURRENT_AUDIT -> {
                requiredObject(missing, "audit.website", state.getAudit().getWebsite());
                requiredObject(missing, "audit.trackingAvailable", state.getAudit().getTrackingAvailable());
            }
            case TARGET_AUDIENCE -> {
                requiredList(missing, "targetAudience.segments", state.getTargetAudience().getSegments());
                if (!MarketingTargetAudienceAnswerNormalizer.hasText(
                        state.getTargetAudience().getPrimaryPersona(), "primaryNeed", "need", "besoin")) {
                    missing.add("targetAudience.primaryPersona.primaryNeed");
                }
                if (!MarketingTargetAudienceAnswerNormalizer.hasText(
                        state.getTargetAudience().getPrimaryPersona(), "mainObjection", "objection", "objectionMajeure")) {
                    missing.add("targetAudience.primaryPersona.mainObjection");
                }
            }
            case POSITIONING -> required(missing, "positioning.valueProposition", state.getPositioning().getValueProposition());
            case CHANNEL_SELECTION -> requiredList(missing, "recommendedChannels", state.getRecommendedChannels());
            case BUDGET_AND_RESOURCES -> {
                requiredObject(missing, "budget.monthlyAmount", state.getBudget().getMonthlyAmount());
                requiredObject(missing, "budget.weeklyTimeHours", state.getBudget().getWeeklyTimeHours());
                required(missing, "budget.leadHandlingCapacity", state.getBudget().getLeadHandlingCapacity());
            }
            case ACTION_PLAN -> requiredList(missing, "weeklyActions", state.getWeeklyActions());
            case KPI_SELECTION -> requiredList(missing, "kpis", state.getKpis());
            case FINAL_DELIVERABLE, COMPLETED -> { }
        }
        return missing;
    }

    public boolean canFinalize(MarketingSessionState state) {
        if (state.getStage() != MarketingStrategyStage.FINAL_DELIVERABLE) {
            return false;
        }
        for (MarketingStrategyStage stage : List.of(
                MarketingStrategyStage.COMPANY_DISCOVERY,
                MarketingStrategyStage.OBJECTIVES,
                MarketingStrategyStage.CURRENT_AUDIT,
                MarketingStrategyStage.TARGET_AUDIENCE,
                MarketingStrategyStage.POSITIONING,
                MarketingStrategyStage.CHANNEL_SELECTION,
                MarketingStrategyStage.BUDGET_AND_RESOURCES,
                MarketingStrategyStage.ACTION_PLAN,
                MarketingStrategyStage.KPI_SELECTION)) {
            if (!missingFor(stage, state).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public String fallbackQuestion(MarketingSessionState state) {
        String missing = state.getMissingInformation().stream().findFirst().orElse("");
        return switch (missing) {
            case "company.sector" -> "Quelle est l'activité principale de votre entreprise ?";
            case "company.productsOrServices" -> "Quels produits ou services commercialisez-vous principalement ?";
            case "company.businessModel" -> "Vendez-vous principalement à des particuliers, à des entreprises, ou aux deux ?";
            case "company.location" -> "Dans quelle ville ou zone géographique exercez-vous ?";
            case "company.size" -> "Quelle est la taille de votre entreprise et de votre équipe ?";
            case "objectives.type" -> "Quel résultat marketing souhaitez-vous obtenir en priorité ?";
            case "objectives.targetValue" -> "Quelle cible mesurable souhaitez-vous atteindre ?";
            case "objectives.deadline" -> "À quelle échéance souhaitez-vous atteindre cet objectif ?";
            case "objectives.smartStatement" -> "Pouvez-vous confirmer cet objectif sous une forme mesurable et datée ?";
            case "audit.website" -> "Disposez-vous actuellement d'un site web, et quel rôle joue-t-il ?";
            case "audit.trackingAvailable" -> "Mesurez-vous actuellement les conversions générées par vos actions marketing ?";
            case "targetAudience.segments" -> "Quel segment de clientèle voulez-vous cibler en priorité ?";
            case "targetAudience.primaryPersona.primaryNeed" -> "Quel est le besoin principal de cette cible ?";
            case "targetAudience.primaryPersona.mainObjection" -> "Quelle est l'objection ou le frein principal de cette cible avant d'acheter ?";
            case "positioning.valueProposition" -> "Pourquoi cette cible devrait-elle choisir votre offre plutôt qu'une alternative ?";
            case "recommendedChannels" -> "Parmi les canaux justifiés par l'audit, lesquels pouvez-vous réellement exploiter ?";
            case "budget.monthlyAmount" -> "Quel budget marketing mensuel pouvez-vous consacrer à ce plan ?";
            case "budget.weeklyTimeHours" -> "Combien d'heures votre équipe peut-elle consacrer au marketing chaque semaine ?";
            case "budget.leadHandlingCapacity" -> "Quelle est votre capacité actuelle à traiter rapidement de nouveaux prospects ?";
            case "weeklyActions" -> "Qui peut être responsable des premières actions du plan sur quatre semaines ?";
            case "kpis" -> "Quel indicateur de résultat permettra de confirmer que l'objectif est atteint ?";
            default -> "Souhaitez-vous vérifier les informations avant de générer votre mini-stratégie Marketing Digital ?";
        };
    }

    public String transitionMessage(MarketingStrategyStage previousStage, MarketingSessionState state) {
        if (previousStage == state.getStage()) {
            return "Merci, j'ai bien noté votre réponse. " + fallbackQuestion(state);
        }
        String acknowledgement = switch (previousStage) {
            case COMPANY_DISCOVERY -> "Merci, j'ai maintenant une première compréhension de votre entreprise.";
            case OBJECTIVES -> "Parfait, votre priorité marketing est maintenant plus claire.";
            case CURRENT_AUDIT -> "Merci, cela me donne une vision plus réaliste de votre situation actuelle.";
            case TARGET_AUDIENCE -> "Très bien, votre cible prioritaire est maintenant mieux définie.";
            case POSITIONING -> "Votre proposition de valeur devient plus claire.";
            case CHANNEL_SELECTION -> "Les canaux retenus sont cohérents avec les informations recueillies.";
            case BUDGET_AND_RESOURCES -> "Merci, je peux maintenant tenir compte de vos moyens réels.";
            case ACTION_PLAN -> "Le plan d'action prend forme.";
            case KPI_SELECTION -> "Nous avons maintenant les indicateurs nécessaires pour mesurer les résultats.";
            case FINAL_DELIVERABLE, COMPLETED -> "Votre stratégie est prête.";
        };
        return acknowledgement + " " + fallbackQuestion(state);
    }

    private MarketingStrategyStage next(MarketingStrategyStage stage) {
        return MarketingStrategyStage.values()[stage.ordinal() + 1];
    }

    private MarketingSessionState.Confidence confidence(MarketingSessionState state) {
        int missing = state.getMissingInformation().size();
        if (missing == 0 && state.getStage().ordinal() >= MarketingStrategyStage.CHANNEL_SELECTION.ordinal()) {
            return MarketingSessionState.Confidence.HIGH;
        }
        return missing <= 1 ? MarketingSessionState.Confidence.MEDIUM : MarketingSessionState.Confidence.LOW;
    }

    private void required(List<String> missing, String path, String value) {
        if (value == null || value.isBlank()) missing.add(path);
    }

    private void requiredObject(List<String> missing, String path, Object value) {
        if (value == null) missing.add(path);
    }

    private void requiredList(List<String> missing, String path, List<?> value) {
        if (value == null || value.isEmpty()) missing.add(path);
    }
}
