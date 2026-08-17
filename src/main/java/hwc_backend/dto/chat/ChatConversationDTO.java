package hwc_backend.dto.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatConversationDTO {

    private Long id;
    private Long diagnosticId;
    private String titre;
    private LocalDateTime dateCreation;
}
