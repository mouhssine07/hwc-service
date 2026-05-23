package hwc_backend.config;

import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.entity.CategorieDiagnostic;
import hwc_backend.repository.CategorieDiagnosticRepository;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${ADMIN_EMAIL:admin@hwc.com}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:Admin@2026}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategorieDiagnosticRepository categorieDiagnosticRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByNom("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        roleRepository.findByNom("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

        roleRepository.findByNom("ROLE_CLIENT")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_CLIENT")));

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setNom("Admin");
            admin.setPrenom("HWC");
            admin.setActif(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }

        seedCategorie("Maturite digitale", "Presence en ligne, outils, process digitaux", "laptop", new BigDecimal("20.00"), 1);
        seedCategorie("Performance commerciale", "Acquisition, conversion, fidelisation clients", "trending-up", new BigDecimal("25.00"), 2);
        seedCategorie("Leadership & Management", "Style de management, communication, vision dirigeants", "users", new BigDecimal("15.00"), 3);
        seedCategorie("Performance organisationnelle", "Structure interne, processus, agilite operationnelle", "settings", new BigDecimal("15.00"), 4);
        seedCategorie("Marketing & Communication", "Positionnement de marque, branding, contenus publies", "megaphone", new BigDecimal("25.00"), 5);
    }

    private void seedCategorie(String nom, String description, String icone, BigDecimal poids, int ordre) {
        categorieDiagnosticRepository.findByNom(nom)
                .orElseGet(() -> categorieDiagnosticRepository.save(
                        new CategorieDiagnostic(null, nom, description, icone, poids, ordre)
                ));
    }
}
