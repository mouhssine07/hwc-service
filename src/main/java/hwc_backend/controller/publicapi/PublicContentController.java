package hwc_backend.controller.publicapi;

import hwc_backend.dto.CertificationsDTO;
import hwc_backend.dto.ChiffresClesDTO;
import hwc_backend.dto.ClientsConfianceDTO;
import hwc_backend.dto.PaysDTO;
import hwc_backend.dto.TemoignagesDTO;
import hwc_backend.service.CertificationsService;
import hwc_backend.service.ChiffresClesService;
import hwc_backend.service.ClientsConfianceService;
import hwc_backend.service.PaysService;
import hwc_backend.service.TemoignagesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicContentController {

    private final TemoignagesService temoignagesService;
    private final CertificationsService certificationsService;
    private final PaysService paysService;
    private final ChiffresClesService chiffresClesService;
    private final ClientsConfianceService clientsConfianceService;

    @GetMapping("/temoignages")
    public ResponseEntity<List<TemoignagesDTO>> getTemoignages() {
        return ResponseEntity.ok(temoignagesService.getAll());
    }

    @GetMapping("/certifications")
    public ResponseEntity<List<CertificationsDTO>> getCertifications() {
        return ResponseEntity.ok(certificationsService.getAll());
    }

    @GetMapping("/pays")
    public ResponseEntity<List<PaysDTO>> getPays() {
        return ResponseEntity.ok(paysService.getAll());
    }

    @GetMapping("/chiffres-cles")
    public ResponseEntity<List<ChiffresClesDTO>> getChiffresCles() {
        return ResponseEntity.ok(chiffresClesService.getAll());
    }

    @GetMapping("/clients-confiance")
    public ResponseEntity<List<ClientsConfianceDTO>> getClientsConfiance() {
        return ResponseEntity.ok(clientsConfianceService.getAll());
    }
}
