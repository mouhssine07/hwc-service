package hwc_backend.service;

import hwc_backend.dto.chat.ChatConversationDTO;
import hwc_backend.dto.chat.ChatMessageDTO;
import hwc_backend.dto.chat.ChatMessageRequestDTO;
import hwc_backend.dto.chat.ChatMessageResponseDTO;
import java.util.List;

public interface ChatService {

    ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, String email);

    List<ChatConversationDTO> getConversations(String email);

    List<ChatMessageDTO> getMessages(Long conversationId, String email);
}
