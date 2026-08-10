package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import hwc_backend.coach.marketing.ingestion.MarketingKnowledgeIngestionService;
import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import hwc_backend.coach.marketing.service.MarketingRagServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.test.util.ReflectionTestUtils;

@EnabledIfEnvironmentVariable(named = "RUN_MARKETING_OPENAI_IT", matches = "true")
class MarketingRagOllamaIntegrationTests {

    @Test
    void ingestsAndRetrievesMarketingKnowledgeWithRealOpenAiEmbeddings() {
        var model = OpenAiEmbeddingModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("text-embedding-3-small")
                .build();
        var store = new InMemoryEmbeddingStore<TextSegment>();
        var ingestion = new MarketingKnowledgeIngestionService(model, store);

        var report = ingestion.ingest();
        assertThat(report.documentCount()).isEqualTo(14);
        assertThat(report.chunkCount()).isGreaterThan(20);
        assertThat(store.size()).isEqualTo(report.chunkCount());

        var rag = new MarketingRagServiceImpl(model, store);
        ReflectionTestUtils.setField(rag, "enabled", true);
        ReflectionTestUtils.setField(rag, "maxResults", 5);
        ReflectionTestUtils.setField(rag, "minScore", 0.45D);

        List<MarketingKnowledgeChunk> chunks = rag.retrieve(
                "Quels canaux recommander à un restaurant local qui veut davantage de réservations ?");

        assertThat(chunks).isNotEmpty().hasSizeLessThanOrEqualTo(5);
        assertThat(chunks).allSatisfy(chunk ->
                assertThat(chunk.metadata().get("service_id")).isEqualTo("MARKETING_STRATEGY"));
        assertThat(chunks).anySatisfy(chunk ->
                assertThat(chunk.content().toLowerCase()).containsAnyOf("canal", "restaurant", "local"));
    }
}
