package hwc_backend.coach.marketing.service;

import hwc_backend.coach.marketing.model.MarketingMessageResponse;
import hwc_backend.coach.marketing.model.MarketingSessionState;
import hwc_backend.coach.marketing.entity.MarketingSessionMessage;
import java.util.List;
import hwc_backend.coach.marketing.model.MarketingImageInput;
import java.util.function.Consumer;

public interface MarketingStrategyService {

    MarketingMessageResponse startSession(String email);

    MarketingMessageResponse processMessage(String sessionId, String email, String message);

    MarketingMessageResponse processMessage(String sessionId, String email, String message, MarketingImageInput image);

    MarketingMessageResponse processMessageStreaming(
            String sessionId, String email, String message, Consumer<String> tokenConsumer);

    MarketingSessionState getSession(String sessionId, String email);

    List<MarketingSessionMessage> getMessages(String sessionId, String email);
}
