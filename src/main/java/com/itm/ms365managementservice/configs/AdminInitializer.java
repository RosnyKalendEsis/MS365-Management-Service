package com.itm.ms365managementservice.configs;

import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.entities.Role;
import com.itm.ms365managementservice.repositories.AdministratorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdministratorRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        boolean adminExists = repository.findAll().stream()
                .anyMatch(u -> u.getRole() == Role.SUPER_ADMIN);

        if (!adminExists) {
            Administrator admin = new Administrator();
            admin.setEmail("admin@itmafrica.com");
            admin.setPwd(passwordEncoder.encode("Admin1234"));
            admin.setRole(Role.SUPER_ADMIN);
            admin.setDisplayName("Super Admin");
            admin.setUserPrincipalName("admin@itmafrica.com");
            admin.setStatus("ACTIF");

            repository.save(admin);
        }
    }
}