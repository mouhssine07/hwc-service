package hwc_backend.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDTO {

    private Long conversationId;
    private Long diagnosticId;
    private Long coachObjectifId;

    @NotBlank
    @Size(max = 2000)
    private String message;
}
