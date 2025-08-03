package com.itm.ms365managementservice.configs;

import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.entities.Role;
import com.itm.ms365managementservice.repositories.AdministratorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdministratorRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        boolean adminExist = repository.findAll().stream()
                .anyMatch(u -> u.getRole() == Role.ADMIN);

        if (!adminExist) {
            Administrator admin = new Administrator();
            admin.setId(UUID.randomUUID().toString());
            admin.setEmail("admin@itm.cd");
            admin.setPwd(passwordEncoder.encode("Admin1234"));
            admin.setRole(Role.ADMIN);

            repository.save(admin);
        }
    }
}
