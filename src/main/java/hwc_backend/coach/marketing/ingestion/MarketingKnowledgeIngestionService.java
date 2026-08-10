package hwc_backend.coach.marketing.ingestion;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

@Service
@RequiredArgsConstructor
public class MarketingKnowledgeIngestionService {

    public static final String SERVICE_ID = "MARKETING_STRATEGY";
    private static final Logger log = LoggerFactory.getLogger(MarketingKnowledgeIngestionService.class);
    private static final Pattern FRONT_MATTER = Pattern.compile("(?s)^---\\s*\\R(.*?)\\R---\\s*\\R(.*)$");
    private static final Pattern HEADING = Pattern.compile("(?m)^(#{1,6})\\s+(.+?)\\s*$");
    private static final int MAX_CHARACTERS = 2400;
    private static final int OVERLAP_CHARACTERS = 250;

    private final EmbeddingModel marketingEmbeddingModel;
    private final EmbeddingStore<TextSegment> marketingEmbeddingStore;

    @Value("${coach.marketing.rag.enabled:false}")
    private boolean enabled;

    @EventListener(ApplicationReadyEvent.class)
    public void ingestOnStartup() {
        if (!enabled) {
            log.info("Marketing RAG ingestion skipped because coach.marketing.rag.enabled=false");
            return;
        }
        IngestionReport report = ingest();
        log.info("Marketing knowledge ingestion completed documents={} chunks={} serviceId={}",
                report.documentCount(), report.chunkCount(), SERVICE_ID);
    }

    public IngestionReport ingest() {
        List<ParsedDocument> documents = loadAllowedDocuments();
        List<TextSegment> segments = documents.stream().flatMap(document -> toSegments(document).stream()).toList();
        if (segments.isEmpty()) {
            throw new IllegalStateException("Aucun chunk Marketing à indexer");
        }

        List<Embedding> embeddings = marketingEmbeddingModel.embedAll(segments).content();
        List<String> ids = segments.stream().map(this::stableId).toList();
        marketingEmbeddingStore.removeAll();
        marketingEmbeddingStore.addAll(ids, embeddings, segments);

        documents.forEach(document -> log.info("Marketing document indexed documentId={} title={} chunks={}",
                document.metadata().get("document_id"), document.metadata().get("title"), toSegments(document).size()));
        return new IngestionReport(documents.size(), segments.size());
    }

    List<ParsedDocument> loadAllowedDocuments() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<Resource> resources = new ArrayList<>();
            resources.addAll(List.of(resolver.getResources("classpath*:coach-ia/global/*.md")));
            resources.addAll(List.of(resolver.getResources(
                    "classpath*:coach-ia/services/marketing-strategy/knowledge/*.md")));
            return resources.stream().map(this::parse).toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de charger la bibliothèque Marketing", exception);
        }
    }

    private ParsedDocument parse(Resource resource) {
        try (var input = resource.getInputStream()) {
            String raw = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            Matcher matcher = FRONT_MATTER.matcher(raw);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Front matter YAML manquant: " + resource.getFilename());
            }
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> metadata = yaml.load(matcher.group(1));
            validateMetadata(resource.getFilename(), metadata);
            return new ParsedDocument(resource.getFilename(), Map.copyOf(metadata), matcher.group(2).trim());
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de lire " + resource.getFilename(), exception);
        }
    }

    private void validateMetadata(String filename, Map<String, Object> metadata) {
        for (String key : List.of("document_id", "title", "service_id", "scope", "retrieval_tags", "version")) {
            if (!metadata.containsKey(key) || metadata.get(key) == null || metadata.get(key).toString().isBlank()) {
                throw new IllegalArgumentException("Métadonnée obligatoire absente " + key + " dans " + filename);
            }
        }
        String declaredService = metadata.get("service_id").toString();
        boolean globalDocument = filename != null && (filename.startsWith("00_") || filename.startsWith("01_regles"));
        if (!SERVICE_ID.equals(declaredService) && !(globalDocument && "GLOBAL".equals(declaredService))) {
            throw new IllegalArgumentException("Document hors périmètre Marketing refusé: " + filename);
        }
    }

    private List<TextSegment> toSegments(ParsedDocument document) {
        List<Section> sections = splitByHeading(document.body());
        List<TextSegment> segments = new ArrayList<>();
        for (Section section : sections) {
            List<String> parts = splitLongSection(section.content());
            for (int index = 0; index < parts.size(); index++) {
                Map<String, Object> values = new LinkedHashMap<>();
                values.put("document_id", document.metadata().get("document_id").toString());
                values.put("title", document.metadata().get("title").toString());
                values.put("service_id", SERVICE_ID);
                values.put("source_service_id", document.metadata().get("service_id").toString());
                values.put("section", section.title());
                values.put("scope", scalarList(document.metadata().get("scope")));
                values.put("retrieval_tags", scalarList(document.metadata().get("retrieval_tags")));
                values.put("version", document.metadata().get("version").toString());
                values.put("source_file", document.filename());
                values.put("chunk_index", index);
                String text = "# " + document.metadata().get("title") + "\n## " + section.title() + "\n" + parts.get(index);
                segments.add(TextSegment.from(text, Metadata.from(values)));
            }
        }
        return segments;
    }

    private List<Section> splitByHeading(String body) {
        Matcher matcher = HEADING.matcher(body);
        List<Section> sections = new ArrayList<>();
        String currentTitle = "Introduction";
        int contentStart = 0;
        while (matcher.find()) {
            String previous = body.substring(contentStart, matcher.start()).trim();
            if (!previous.isBlank()) {
                sections.add(new Section(currentTitle, previous));
            }
            currentTitle = matcher.group(2).trim();
            contentStart = matcher.end();
        }
        String remaining = body.substring(contentStart).trim();
        if (!remaining.isBlank()) {
            sections.add(new Section(currentTitle, remaining));
        }
        return sections;
    }

    private List<String> splitLongSection(String content) {
        if (content.length() <= MAX_CHARACTERS) {
            return List.of(content);
        }
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < content.length()) {
            int end = Math.min(start + MAX_CHARACTERS, content.length());
            if (end < content.length()) {
                int paragraph = content.lastIndexOf("\n\n", end);
                if (paragraph > start + MAX_CHARACTERS / 2) {
                    end = paragraph;
                }
            }
            chunks.add(content.substring(start, end).trim());
            if (end == content.length()) {
                break;
            }
            start = Math.max(end - OVERLAP_CHARACTERS, start + 1);
        }
        return chunks;
    }

    private String scalarList(Object value) {
        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).reduce((left, right) -> left + "," + right).orElse("");
        }
        return value.toString();
    }

    private String stableId(TextSegment segment) {
        String source = segment.metadata().getString("document_id") + "|"
                + segment.metadata().getString("section") + "|"
                + segment.metadata().getInteger("chunk_index");
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 indisponible", exception);
        }
    }

    public record IngestionReport(int documentCount, int chunkCount) { }
    record ParsedDocument(String filename, Map<String, Object> metadata, String body) { }
    private record Section(String title, String content) { }
}
