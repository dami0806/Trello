package com.sparta.trello.domain.common;

import com.sparta.trello.domain.role.entity.Role;
import com.sparta.trello.domain.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            if (roleRepository.findByRoleName("ROLE_USER") == null) {
                roleRepository.save(new Role(null, "ROLE_USER", new HashSet<>()));
            }
            if (roleRepository.findByRoleName("ROLE_MANAGER") == null) {
                roleRepository.save(new Role(null, "ROLE_MANAGER", new HashSet<>()));
            }
            if (roleRepository.findByRoleName("ROLE_MEMBER") == null) {
                roleRepository.save(new Role(null, "ROLE_MEMBER", new HashSet<>()));
            }
        };
    }
}