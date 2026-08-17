package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingImageInput;
import java.util.function.Consumer;

public interface MarketingLanguageModelService {

    String generateJson(String systemPrompt, String userPrompt);

    default String generateJson(String systemPrompt, String userPrompt, MarketingImageInput image) {
        return generateJson(systemPrompt, userPrompt);
    }

    default String streamText(String systemPrompt, String userPrompt, Consumer<String> tokenConsumer) {
        String text = generateJson(systemPrompt, userPrompt);
        tokenConsumer.accept(text);
        return text;
    }

    default String generateJsonWithWebSearch(String systemPrompt, String userPrompt) {
        return generateJson(systemPrompt, userPrompt);
    }
}
