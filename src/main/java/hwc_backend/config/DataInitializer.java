package hwc_backend.config;

import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByNom("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        roleRepository.findByNom("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

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
    }
}
