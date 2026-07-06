package com.example.teamflow.config;

import com.example.teamflow.entity.Role;
import com.example.teamflow.entity.User;
import com.example.teamflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing default users...");
            createUser("Admin User", "admin@teamflow.com", "admin123", Role.ADMIN);
            createUser("Manager User", "manager@teamflow.com", "manager123", Role.MANAGER);
            createUser("Developer User", "developer@teamflow.com", "dev123", Role.DEVELOPER);
            createUser("Reviewer User", "reviewer@teamflow.com", "review123", Role.REVIEWER);
            log.info("Default users created successfully");
        }
    }

    private void createUser(String name, String email, String password, Role role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .active(true)
                .build();
        userRepository.save(user);
    }
}
