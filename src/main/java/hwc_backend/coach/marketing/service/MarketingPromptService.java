package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingKnowledgeChunk;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class MarketingPromptService {

    private static final String ROOT = "coach-ia/services/marketing-strategy/";

    public String systemPrompt() {
        return read("prompts/system.md");
    }

    public String buildNextQuestionPrompt(
            MarketingSessionState state,
            String stateJson,
            String userMessage,
            List<MarketingKnowledgeChunk> chunks
    ) {
        return read("prompts/next-question.md")
                + "\n\nÉTAPE ACTIVE:\n" + state.getStage()
                + "\n\nÉTAT COMPLET:\n" + stateJson
                + "\n\nDERNIER MESSAGE CLIENT:\n" + userMessage
                + "\n\nCONTEXTE RAG MARKETING:\n" + formatChunks(chunks);
    }

    public String buildOpeningPrompt(
            MarketingSessionState state,
            String stateJson,
            List<MarketingKnowledgeChunk> chunks
    ) {
        return read("prompts/next-question.md")
                + "\n\nCAS PARTICULIER : la session vient de s'ouvrir et le client n'a encore envoyé aucun message."
                + " Accueille-le brièvement, pose la question correspondant à la première information manquante,"
                + " retourne l'intention SOCIAL, ne modifie pas l'état et génère 2 à 4 exemples de réponses"
                + " adaptés aux informations déjà connues dans son profil."
                + "\n\nÉTAPE ACTIVE:\n" + state.getStage()
                + "\n\nÉTAT COMPLET:\n" + stateJson
                + "\n\nDERNIER MESSAGE CLIENT:\n[AUCUN MESSAGE - OUVERTURE DE SESSION]"
                + "\n\nCONTEXTE RAG MARKETING:\n" + formatChunks(chunks);
    }

    public String buildSuggestedRepliesPrompt(MarketingSessionState state, String question) {
        return "Génère uniquement un JSON {\"suggestedReplies\":[...]} contenant 2 à 4 réponses "
                + "courtes et prêtes à envoyer à la question du Coach. Chaque réponse est écrite à la première "
                + "personne comme si le client parlait. Utilise l'état connu pour contextualiser les possibilités sans "
                + "inventer un fait. N'écris jamais de consigne, placeholder, exemple à remplacer ou commentaire."
                + "\n\nÉTAT CLIENT:\n" + state
                + "\n\nQUESTION EXACTE DU COACH:\n" + question;
    }

    public String buildFinalDeliverablePrompt(MarketingSessionState state, String stateJson) {
        return read("prompts/final-deliverable.md")
                + "\n\nÉTAT VALIDÉ DE LA SESSION:\n" + stateJson
                + "\n\nTEMPLATE À RESPECTER:\n" + deliverableTemplate();
    }

    public String deliverableTemplate() {
        return read("templates/mini_strategie_marketing.md");
    }

    private String formatChunks(List<MarketingKnowledgeChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return "Aucun chunk récupéré.";
        }
        return chunks.stream()
                .map(chunk -> "[document=" + chunk.metadata().get("document_id")
                        + ", section=" + chunk.metadata().get("section")
                        + ", score=" + String.format("%.4f", chunk.score()) + "]\n" + chunk.content())
                .reduce((left, right) -> left + "\n\n---\n\n" + right)
                .orElse("");
    }

    private String read(String relativePath) {
        try (var input = new ClassPathResource(ROOT + relativePath).getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException exception) {
            throw new IllegalStateException("Ressource Coach Marketing introuvable: " + relativePath, exception);
        }
    }
}
