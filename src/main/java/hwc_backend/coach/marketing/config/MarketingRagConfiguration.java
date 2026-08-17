package hwc_backend.coach.marketing.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketingRagConfiguration {

    @Bean
    EmbeddingModel marketingEmbeddingModel(
            @Value("${openai.api-key}") String apiKey,
            @Value("${coach.marketing.rag.embedding-model:text-embedding-3-small}") String modelName
    ) {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }

    @Bean
    EmbeddingStore<TextSegment> marketingEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}
