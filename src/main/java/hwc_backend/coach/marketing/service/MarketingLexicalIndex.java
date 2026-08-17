package hwc_backend.coach.marketing.service;

import dev.langchain4j.data.segment.TextSegment;
import hwc_backend.coach.marketing.ingestion.MarketingKnowledgeIngestionService;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.springframework.stereotype.Component;

@Component
public class MarketingLexicalIndex {

    private static final Pattern NON_WORD = Pattern.compile("[^a-z0-9]+");
    private static final Set<String> STOP_WORDS = Set.of("avec", "dans", "pour", "quel", "quelle", "quels",
            "quelles", "comment", "nous", "vous", "votre", "notre", "des", "les", "une", "sur", "qui");
    private volatile List<IndexedChunk> chunks = List.of();

    public synchronized void replace(List<TextSegment> segments) {
        List<IndexedChunk> indexed = new ArrayList<>();
        for (TextSegment segment : segments) {
            if (!MarketingKnowledgeIngestionService.SERVICE_ID.equals(
                    segment.metadata().getString("service_id"))) continue;
            String id = stableId(segment);
            String searchable = segment.text() + " " + metadata(segment, "title") + " "
                    + metadata(segment, "retrieval_tags");
            indexed.add(new IndexedChunk(id, segment, frequencies(tokenize(searchable))));
        }
        chunks = List.copyOf(indexed);
    }

    public List<LexicalMatch> search(String query, int limit) {
        List<String> queryTerms = tokenize(query);
        if (queryTerms.isEmpty() || chunks.isEmpty()) return List.of();
        Map<String, Integer> documentFrequency = new HashMap<>();
        for (String term : new HashSet<>(queryTerms)) {
            int count = (int) chunks.stream().filter(chunk -> chunk.frequencies().containsKey(term)).count();
            documentFrequency.put(term, count);
        }
        double averageLength = chunks.stream().mapToInt(chunk -> chunk.termCount()).average().orElse(1D);
        List<LexicalMatch> matches = new ArrayList<>();
        for (IndexedChunk chunk : chunks) {
            double score = bm25(queryTerms, chunk, documentFrequency, averageLength);
            if (score > 0D) matches.add(new LexicalMatch(chunk.id(), chunk.segment(), score));
        }
        return matches.stream().sorted(Comparator.comparingDouble(LexicalMatch::score).reversed())
                .limit(Math.max(1, limit)).toList();
    }

    private double bm25(List<String> queryTerms, IndexedChunk chunk, Map<String, Integer> df, double averageLength) {
        double score = 0D;
        double k1 = 1.5D;
        double b = 0.75D;
        for (String term : new HashSet<>(queryTerms)) {
            int frequency = chunk.frequencies().getOrDefault(term, 0);
            if (frequency == 0) continue;
            double idf = Math.log(1D + (chunks.size() - df.getOrDefault(term, 0) + 0.5D)
                    / (df.getOrDefault(term, 0) + 0.5D));
            double denominator = frequency + k1 * (1D - b + b * chunk.termCount() / averageLength);
            score += idf * frequency * (k1 + 1D) / denominator;
        }
        return score;
    }

    private Map<String, Integer> frequencies(List<String> terms) {
        Map<String, Integer> result = new LinkedHashMap<>();
        terms.forEach(term -> result.merge(term, 1, Integer::sum));
        return result;
    }

    private String stableId(TextSegment segment) {
        String source = metadata(segment, "document_id") + "|" + metadata(segment, "section") + "|"
                + metadata(segment, "chunk_index");
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Impossible d'identifier le chunk lexical", exception);
        }
    }

    private String metadata(TextSegment segment, String key) {
        Object value = segment.metadata().toMap().get(key);
        return value == null ? "" : value.toString();
    }

    private List<String> tokenize(String value) {
        String normalized = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
        return NON_WORD.splitAsStream(normalized)
                .filter(term -> term.length() > 2 && !STOP_WORDS.contains(term))
                .toList();
    }

    private record IndexedChunk(String id, TextSegment segment, Map<String, Integer> frequencies) {
        int termCount() { return frequencies.values().stream().mapToInt(Integer::intValue).sum(); }
    }

    public record LexicalMatch(String id, TextSegment segment, double score) { }
}
