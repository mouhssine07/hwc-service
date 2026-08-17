package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.util.List;
import hwc_backend.coach.marketing.model.MarketingSessionSummary;
import com.fasterxml.jackson.databind.JsonNode;
import hwc_backend.coach.marketing.model.MarketingStateCorrectionView;

public interface MarketingSessionService {

    MarketingSessionState createSession(String email);

    List<MarketingSessionSummary> listSessions(String email);

    void deleteSession(String sessionId, String email);

    MarketingSessionState getState(String sessionId, String email);

    MarketingSessionState saveState(String sessionId, String email, MarketingSessionState state);

    void appendMessage(
            String sessionId,
            String email,
            MarketingSessionMessage.Role role,
            String content,
            String ragContextJson
    );

    void appendMessage(String sessionId, String email, MarketingSessionMessage.Role role,
                       String content, String ragContextJson, String imageDataUrl, String imageName);

    void appendMessage(String sessionId, String email, MarketingSessionMessage.Role role,
                       String content, String ragContextJson, String imageDataUrl, String imageName,
                       String imageAnnotationsJson);

    List<MarketingSessionMessage> getMessages(String sessionId, String email);

    List<MarketingSessionMessage> getRecentFollowUpMessages(String sessionId, String email, int limit);

    void saveDeliverable(String sessionId, String email, String deliverableJson, String deliverableMarkdown);

    String getDeliverableMarkdown(String sessionId, String email);

    MarketingSessionState correctState(String sessionId, String email, String path, JsonNode value);

    List<MarketingStateCorrectionView> getCorrections(String sessionId, String email);
}
