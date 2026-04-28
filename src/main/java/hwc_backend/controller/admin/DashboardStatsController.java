package hwc_backend.controller.admin;

import hwc_backend.dto.DashboardStatsDTO;
import hwc_backend.repository.DemandesContactRepository;
import hwc_backend.repository.PaysRepository;
import hwc_backend.repository.ServicesRepository;
import hwc_backend.repository.TemoignagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardStatsController {

    private final ServicesRepository servicesRepository;
    private final TemoignagesRepository temoignagesRepository;
    private final DemandesContactRepository demandesContactRepository;
    private final PaysRepository paysRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        return ResponseEntity.ok(new DashboardStatsDTO(
                servicesRepository.count(),
                temoignagesRepository.count(),
                demandesContactRepository.count(),
                paysRepository.count()
        ));
    }
}
