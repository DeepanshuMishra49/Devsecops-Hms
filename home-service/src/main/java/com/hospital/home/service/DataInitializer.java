package com.hospital.home.service;

import com.hospital.home.model.User;
import com.hospital.home.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Initialize default admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Admin");
            admin.setRole("ROLE_ADMIN");
            admin.setProvider("LOCAL");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Default admin user initialized (admin/admin123)");
        }

        // Initialize default regular user if not exists
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@hospital.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Normal User");
            user.setRole("ROLE_USER");
            user.setProvider("LOCAL");
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("Default user initialized (user/user123)");
        }
    }
}
