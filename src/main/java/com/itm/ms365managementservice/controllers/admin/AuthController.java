package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.configs.JwtUtil;
import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.repositories.AdministratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
@AllArgsConstructor
public class AuthController {

    private final AdministratorRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String pwd = loginRequest.get("pwd");

        if (email == null || pwd == null) {
            return ResponseEntity.badRequest().body("email ou mot de passe manquant");
        }

        Administrator user = repository.findAll().stream()
                .filter(u -> u.getEmail().equals(email) && passwordEncoder.matches(pwd,u.getPwd()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", user
        ));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCredentials(@RequestBody Map<String, String> request) {
        String currentEmail = request.get("currentEmail");
        String newEmail = request.get("newEmail");
        String newPassword = request.get("newPassword");

        if (currentEmail == null || (newEmail == null && newPassword == null)) {
            return ResponseEntity.badRequest().body("Informations incomplètes pour la mise à jour");
        }

        Administrator user = repository.findByEmail(currentEmail);

        if (user == null) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        if (newEmail != null && !newEmail.isBlank()) {
            user.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPwd(passwordEncoder.encode(newPassword));
        }

        repository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Informations mises à jour avec succès",
                "user", user
        ));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(repository.findAll());
    }


}
