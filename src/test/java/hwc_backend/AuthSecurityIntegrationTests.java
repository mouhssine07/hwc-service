package hwc_backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.UserRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTests {

    private static final String ADMIN_EMAIL = "admin@hwc.com";
    private static final String ADMIN_PASSWORD = "Admin@2026";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void ensureAdminUser() {
        Role adminRole = roleRepository.findByNom("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        User admin = userRepository.findByEmail(ADMIN_EMAIL).orElseGet(User::new);
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setNom("Admin");
        admin.setPrenom("HWC");
        admin.setActif(true);
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);
    }

    @Test
    void loginReturnsTokenAndAdminRouteRequiresIt() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/admin/health"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/admin/health")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/admin/services",
            "/api/admin/sous-services",
            "/api/admin/etiquettes",
            "/api/admin/temoignages",
            "/api/admin/certifications",
            "/api/admin/pays",
            "/api/admin/chiffres-cles",
            "/api/admin/clients-confiance",
            "/api/admin/demandes-contact",
            "/api/admin/service-fonctionnalites",
            "/api/admin/service-images",
            "/api/admin/sous-service-fonctionnalites",
            "/api/admin/sous-service-avantages",
            "/api/admin/sous-service-etapes",
            "/api/admin/sous-service-faqs",
            "/api/admin/accompagnements",
            "/api/admin/dashboard/stats"
    })
    void adminListEndpointsRequireValidToken(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get(endpoint)
                        .header("Authorization", "Bearer " + loginAndGetToken()))
                .andExpect(status().isOk());
    }

    @Test
    void adminCreateEndpointValidatesRequestBody() throws Exception {
        mockMvc.perform(post("/api/admin/services")
                        .header("Authorization", "Bearer " + loginAndGetToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titre": "",
                                  "description": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/public/services",
            "/api/public/temoignages",
            "/api/public/certifications",
            "/api/public/pays",
            "/api/public/chiffres-cles",
            "/api/public/clients-confiance",
            "/api/public/service-images"
    })
    void publicGetEndpointsDoNotRequireToken(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk());
    }

    @Test
    void publicContactEndpointDoesNotRequireTokenAndValidatesEmail() throws Exception {
        mockMvc.perform(post("/api/public/demandes-contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/public/demandes-contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "contact-public-test@hwc.com"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    private String loginAndGetToken() throws Exception {
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@hwc.com",
                                  "password": "Admin@2026"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(loginResponse);
        return json.get("token").asText();
    }
}
