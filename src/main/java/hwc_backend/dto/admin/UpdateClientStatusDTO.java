package hwc_backend.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateClientStatusDTO {

    @NotNull
    private Boolean actif;
}
