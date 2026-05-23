package hwc_backend.controller;

import hwc_backend.dto.auth.AuthResponseDTO;
import hwc_backend.dto.auth.LoginRequestDTO;
import hwc_backend.dto.auth.UserDTO;
import hwc_backend.dto.clientauth.RegisterClientDTO;
import hwc_backend.entity.Role;
import hwc_backend.entity.User;
import hwc_backend.repository.RoleRepository;
import hwc_backend.repository.UserRepository;
import hwc_backend.security.JwtUtil;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/auth")
@RequiredArgsConstructor
public class ClientAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterClientDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Role clientRole = roleRepository.findByNom("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("ROLE_CLIENT not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEntreprise(request.getEntreprise());
        user.setSecteur(request.getSecteur());
        user.setTailleEntreprise(request.getTailleEntreprise());
        user.setTelephone(request.getTelephone());
        user.setActif(true);
        user.setRoles(Set.of(clientRole));

        return ResponseEntity.status(HttpStatus.CREATED).body(toUserDTO(userRepository.save(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        boolean isClient = user.getRoles().stream().anyMatch(role -> "ROLE_CLIENT".equals(role.getNom()));
        if (!isClient) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setDateDerniereConnexion(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(toAuthResponse(user, token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        return ResponseEntity.ok(toUserDTO(user));
    }

    private AuthResponseDTO toAuthResponse(User user, String token) {
        return new AuthResponseDTO(
                token,
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roleNames(user)
        );
    }

    private UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getEntreprise(),
                user.getSecteur(),
                user.getTailleEntreprise(),
                user.getTelephone(),
                user.isActif(),
                roleNames(user)
        );
    }

    private Set<String> roleNames(User user) {
        return user.getRoles().stream()
                .map(Role::getNom)
                .collect(Collectors.toSet());
    }
}
