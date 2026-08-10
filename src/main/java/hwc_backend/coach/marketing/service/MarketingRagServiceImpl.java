package hwc_backend.coach.marketing.service;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import hwc_backend.coach.marketing.ingestion.MarketingKnowledgeIngestionService;
import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketingRagServiceImpl implements MarketingRagService {

    private static final Logger log = LoggerFactory.getLogger(MarketingRagServiceImpl.class);

    private final EmbeddingModel marketingEmbeddingModel;
    private final EmbeddingStore<TextSegment> marketingEmbeddingStore;

    @Value("${coach.marketing.rag.enabled:false}")
    private boolean enabled;

    @Value("${coach.marketing.rag.max-results:5}")
    private int maxResults;

    @Value("${coach.marketing.rag.min-score:0.65}")
    private double minScore;

    @Override
    public List<MarketingKnowledgeChunk> retrieve(String question) {
        if (!enabled) {
            throw new IllegalStateException("Le RAG Marketing est désactivé");
        }
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("La question RAG est obligatoire");
        }

        Embedding queryEmbedding = marketingEmbeddingModel.embed(question.trim()).content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .minScore(minScore)
                .filter(metadataKey("service_id").isEqualTo(MarketingKnowledgeIngestionService.SERVICE_ID))
                .build();

        List<MarketingKnowledgeChunk> chunks = marketingEmbeddingStore.search(request).matches().stream()
                .map(match -> new MarketingKnowledgeChunk(
                        match.embeddingId(),
                        match.embedded().text(),
                        match.score(),
                        match.embedded().metadata().toMap()
                ))
                .toList();

        chunks.forEach(chunk -> log.info(
                "Marketing RAG chunk retrieved documentId={} section={} score={} serviceId={}",
                chunk.metadata().get("document_id"), chunk.metadata().get("section"),
                chunk.score(), chunk.metadata().get("service_id")));
        return chunks;
    }
}
