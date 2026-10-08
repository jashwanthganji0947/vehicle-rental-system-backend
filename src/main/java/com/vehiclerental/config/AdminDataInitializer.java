package com.vehiclerental.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vehiclerental.entity.User;
import com.vehiclerental.repository.UserRepository;

@Configuration
public class AdminDataInitializer {

    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "jashwanthganji@gmail.com";
            String adminPassword = "Admin@123";

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();

                admin.setName("Admin");
                admin.setEmail(adminEmail);
                admin.setPhone("9999999999");

                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );

                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println(
                        "Admin account created successfully"
                );
            }
        };
    }
}