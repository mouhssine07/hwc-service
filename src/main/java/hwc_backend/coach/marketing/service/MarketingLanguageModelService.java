package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingImageInput;

public interface MarketingLanguageModelService {

    String generateJson(String systemPrompt, String userPrompt);

    default String generateJson(String systemPrompt, String userPrompt, MarketingImageInput image) {
        return generateJson(systemPrompt, userPrompt);
    }
}
