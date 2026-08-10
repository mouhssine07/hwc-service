package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import java.util.List;
import hwc_backend.coach.marketing.model.MarketingSessionSummary;

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

    List<MarketingSessionMessage> getMessages(String sessionId, String email);

    void saveDeliverable(String sessionId, String email, String deliverableJson, String deliverableMarkdown);

    String getDeliverableMarkdown(String sessionId, String email);
}
