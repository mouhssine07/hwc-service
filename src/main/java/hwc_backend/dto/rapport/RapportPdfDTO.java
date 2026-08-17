package hwc_backend.dto.rapport;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportPdfDTO {

    private Long id;
    private Long diagnosticId;
    private String fileName;
    private LocalDateTime dateGeneration;
}
