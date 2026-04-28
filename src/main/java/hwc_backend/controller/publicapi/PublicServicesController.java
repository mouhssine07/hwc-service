package hwc_backend.controller.publicapi;

import hwc_backend.dto.ServicesDTO;
import hwc_backend.dto.SousServicesDTO;
import hwc_backend.service.ServicesService;
import hwc_backend.service.SousServicesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicServicesController {

    private final ServicesService servicesService;
    private final SousServicesService sousServicesService;

    @GetMapping("/services")
    public ResponseEntity<List<ServicesDTO>> getServices() {
        return ResponseEntity.ok(servicesService.getAll());
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ServicesDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(servicesService.getById(id));
    }

    @GetMapping("/sous-services/{id}")
    public ResponseEntity<SousServicesDTO> getSousServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(sousServicesService.getById(id));
    }
}
