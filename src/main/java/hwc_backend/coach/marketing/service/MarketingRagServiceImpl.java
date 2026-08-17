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
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MarketingRagServiceImpl implements MarketingRagService {

    private static final Logger log = LoggerFactory.getLogger(MarketingRagServiceImpl.class);

    private final EmbeddingModel marketingEmbeddingModel;
    private final EmbeddingStore<TextSegment> marketingEmbeddingStore;
    private final MarketingLexicalIndex lexicalIndex;

    public MarketingRagServiceImpl(
            EmbeddingModel marketingEmbeddingModel,
            EmbeddingStore<TextSegment> marketingEmbeddingStore) {
        this(marketingEmbeddingModel, marketingEmbeddingStore, new MarketingLexicalIndex());
    }

    @Autowired
    public MarketingRagServiceImpl(
            EmbeddingModel marketingEmbeddingModel,
            EmbeddingStore<TextSegment> marketingEmbeddingStore,
            MarketingLexicalIndex lexicalIndex) {
        this.marketingEmbeddingModel = marketingEmbeddingModel;
        this.marketingEmbeddingStore = marketingEmbeddingStore;
        this.lexicalIndex = lexicalIndex;
    }

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
                .maxResults(Math.max(maxResults * 3, maxResults))
                .minScore(minScore)
                .filter(metadataKey("service_id").isEqualTo(MarketingKnowledgeIngestionService.SERVICE_ID))
                .build();

        var vectorMatches = marketingEmbeddingStore.search(request).matches();
        var lexicalMatches = lexicalIndex.search(question, Math.max(maxResults * 3, maxResults));
        Map<String, HybridCandidate> candidates = new LinkedHashMap<>();
        for (int index = 0; index < vectorMatches.size(); index++) {
            var match = vectorMatches.get(index);
            String canonicalId = canonicalId(match.embedded());
            candidates.computeIfAbsent(canonicalId, ignored -> new HybridCandidate(
                    match.embeddingId(), match.embedded(), match.score(), 0D))
                    .addVectorRank(index + 1);
        }
        for (int index = 0; index < lexicalMatches.size(); index++) {
            var match = lexicalMatches.get(index);
            candidates.computeIfAbsent(match.id(), ignored -> new HybridCandidate(
                    match.id(), match.segment(), 0D, match.score()))
                    .addLexicalRank(index + 1, match.score());
        }
        double bestFusion = candidates.values().stream().mapToDouble(HybridCandidate::fusionScore).max().orElse(1D);
        List<MarketingKnowledgeChunk> chunks = candidates.values().stream()
                .filter(candidate -> MarketingKnowledgeIngestionService.SERVICE_ID.equals(
                        candidate.segment.metadata().getString("service_id")))
                .sorted(java.util.Comparator.comparingDouble(HybridCandidate::fusionScore).reversed())
                .limit(maxResults)
                .map(candidate -> {
                    Map<String, Object> metadata = new LinkedHashMap<>(candidate.segment.metadata().toMap());
                    metadata.put("retrieval_method", candidate.method());
                    metadata.put("vector_score", candidate.vectorScore);
                    metadata.put("lexical_score", candidate.lexicalScore);
                    return new MarketingKnowledgeChunk(candidate.id, candidate.segment.text(),
                            bestFusion == 0D ? 0D : candidate.fusionScore() / bestFusion, Map.copyOf(metadata));
                }).toList();

        chunks.forEach(chunk -> log.info(
                "Marketing RAG chunk retrieved documentId={} section={} score={} serviceId={}",
                chunk.metadata().get("document_id"), chunk.metadata().get("section"),
                chunk.score(), chunk.metadata().get("service_id")));
        return chunks;
    }

    private String canonicalId(TextSegment segment) {
        Map<String, Object> metadata = segment.metadata().toMap();
        String source = metadata.getOrDefault("document_id", "") + "|"
                + metadata.getOrDefault("section", "") + "|"
                + metadata.getOrDefault("chunk_index", "");
        try {
            return java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible d'identifier le chunk hybride", exception);
        }
    }

    private static final class HybridCandidate {
        private final String id;
        private final TextSegment segment;
        private final double vectorScore;
        private double lexicalScore;
        private double fusionScore;
        private boolean vector;
        private boolean lexical;

        private HybridCandidate(String id, TextSegment segment, double vectorScore, double lexicalScore) {
            this.id = id;
            this.segment = segment;
            this.vectorScore = vectorScore;
            this.lexicalScore = lexicalScore;
        }

        private void addVectorRank(int rank) { vector = true; fusionScore += 0.55D / (60D + rank); }
        private void addLexicalRank(int rank, double score) {
            lexical = true;
            lexicalScore = Math.max(lexicalScore, score);
            fusionScore += 0.45D / (60D + rank);
        }
        private double fusionScore() { return fusionScore; }
        private String method() { return vector && lexical ? "hybrid" : vector ? "vector" : "lexical"; }
    }
}
