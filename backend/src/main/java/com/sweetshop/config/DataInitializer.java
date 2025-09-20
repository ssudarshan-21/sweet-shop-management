package com.sweetshop.config;

import com.sweetshop.entity.Role;
import com.sweetshop.entity.User;
import com.sweetshop.repository.RoleRepository;
import com.sweetshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Ensure USER role exists
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER", "Default user role")));

            // Ensure ADMIN role exists
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN", "Administrator role")));

            // Ensure admin user exists
            if (userRepository.findByEmail("admin@sweetshop.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@sweetshop.com");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole, userRole)); // ✅ Give both roles

                userRepository.save(admin);
                System.out.println("✅ Admin user created with email: admin@sweetshop.com / password: password");
            }
        };
    }
}
