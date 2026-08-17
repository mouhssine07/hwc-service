package hwc_backend.controller;

import hwc_backend.dto.dashboard.DashboardClientDTO;
import hwc_backend.service.DashboardClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/dashboard")
@RequiredArgsConstructor
public class DashboardClientController {

    private final DashboardClientService dashboardClientService;

    @GetMapping
    public ResponseEntity<DashboardClientDTO> dashboard(Authentication authentication) {
        return ResponseEntity.ok(dashboardClientService.getDashboard(authentication.getName()));
    }
}
