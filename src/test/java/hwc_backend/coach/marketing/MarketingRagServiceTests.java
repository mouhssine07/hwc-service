package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import hwc_backend.coach.marketing.service.MarketingRagServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MarketingRagServiceTests {

    @Test
    void alwaysFiltersSearchResultsByMarketingServiceId() {
        Embedding vector = Embedding.from(new float[]{1F, 0F, 0F});
        EmbeddingModel model = mock(EmbeddingModel.class);
        when(model.embed("Quels canaux choisir ?")).thenReturn(Response.from(vector));

        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
        store.add("marketing", vector, segment("Règles Marketing", "MARKETING_STRATEGY"));
        store.add("commercial", vector, segment("Règles commerciales", "COMMERCIAL_PERFORMANCE"));

        MarketingRagServiceImpl service = new MarketingRagServiceImpl(model, store);
        ReflectionTestUtils.setField(service, "enabled", true);
        ReflectionTestUtils.setField(service, "maxResults", 5);
        ReflectionTestUtils.setField(service, "minScore", 0D);

        List<MarketingKnowledgeChunk> chunks = service.retrieve("Quels canaux choisir ?");

        assertThat(chunks).extracting(MarketingKnowledgeChunk::id).containsExactly("marketing");
        assertThat(chunks).allSatisfy(chunk ->
                assertThat(chunk.metadata().get("service_id")).isEqualTo("MARKETING_STRATEGY"));
    }

    private TextSegment segment(String content, String serviceId) {
        return TextSegment.from(content, Metadata.from(java.util.Map.of(
                "document_id", "TEST-001",
                "section", "Test",
                "service_id", serviceId
        )));
    }
}
