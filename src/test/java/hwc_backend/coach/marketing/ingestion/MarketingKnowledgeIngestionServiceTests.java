package hwc_backend.coach.marketing.ingestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import java.util.List;
import org.junit.jupiter.api.Test;

class MarketingKnowledgeIngestionServiceTests {

    @SuppressWarnings("unchecked")
    @Test
    void loadsOnlyGlobalAndMarketingKnowledgeDocuments() {
        MarketingKnowledgeIngestionService service = new MarketingKnowledgeIngestionService(
                mock(EmbeddingModel.class),
                mock(EmbeddingStore.class)
        );

        List<MarketingKnowledgeIngestionService.ParsedDocument> documents = service.loadAllowedDocuments();

        assertThat(documents).hasSize(14);
        assertThat(documents).allSatisfy(document -> {
            assertThat(document.metadata()).containsKeys(
                    "document_id", "title", "service_id", "scope", "retrieval_tags", "version");
            assertThat(document.metadata().get("service_id")).isIn("GLOBAL", "MARKETING_STRATEGY");
            assertThat(document.filename()).doesNotContain("performance", "commerciale", "organisationnelle");
        });
        assertThat(documents).filteredOn(document -> "MARKETING_STRATEGY".equals(document.metadata().get("service_id")))
                .hasSize(12);
    }
}
