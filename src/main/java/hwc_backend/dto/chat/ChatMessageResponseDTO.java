package hwc_backend.dto.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDTO {

    private Long conversationId;
    private String role;
    private String contenu;
    private LocalDateTime dateEnvoi;
}
