package com.churniq.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(
            AdminUserRepository repository,
            PasswordEncoder encoder,
            @Value("${ADMIN_USERNAME:admin}") String username,
            @Value("${ADMIN_PASSWORD:}") String password) {

        return args -> {
            if (!repository.existsByUsername(username)) {
                if (password.isBlank()) {
                    throw new IllegalStateException(
                        "Set ADMIN_PASSWORD before starting ChurnIQ."
                    );
                }

                AdminUser admin = new AdminUser();
                admin.setUsername(username);
                admin.setPassword(encoder.encode(password));
                repository.save(admin);
                System.out.println("ChurnIQ administrator account created.");
            }
        };
    }
}
