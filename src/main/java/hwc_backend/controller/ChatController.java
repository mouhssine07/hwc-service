package hwc_backend.controller;

import hwc_backend.dto.chat.ChatConversationDTO;
import hwc_backend.dto.chat.ChatMessageDTO;
import hwc_backend.dto.chat.ChatMessageRequestDTO;
import hwc_backend.dto.chat.ChatMessageResponseDTO;
import hwc_backend.service.ChatService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/message")
    public ResponseEntity<ChatMessageResponseDTO> sendMessage(
            @Valid @RequestBody ChatMessageRequestDTO request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(chatService.sendMessage(request, authentication.getName()));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ChatConversationDTO>> conversations(Authentication authentication) {
        return ResponseEntity.ok(chatService.getConversations(authentication.getName()));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<ChatMessageDTO>> messages(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(chatService.getMessages(id, authentication.getName()));
    }
}
