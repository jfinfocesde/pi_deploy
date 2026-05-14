package com.example.demo_basic.config;

import com.example.demo_basic.model.entity.Usuario;
import com.example.demo_basic.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // Crear usuario administrador inicial
                Usuario admin = Usuario.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ROLE_ADMIN")
                        .build();

                // Crear usuario común inicial
                Usuario user = Usuario.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .role("ROLE_USER")
                        .build();

                usuarioRepository.save(admin);
                usuarioRepository.save(user);

                System.out.println("Usuarios iniciales creados en la base de datos.");
            }
        };
    }
}
