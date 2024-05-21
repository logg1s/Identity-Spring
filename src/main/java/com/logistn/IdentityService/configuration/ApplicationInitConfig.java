package com.logistn.IdentityService.configuration;

import com.logistn.IdentityService.entity.Role;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.repository.RoleRepository;
import com.logistn.IdentityService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Value("${env.testMode}")
    private boolean isTestMode;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            if (!isTestMode && userRepository.findByUsername("admin").isEmpty()) {
                Role role = roleRepository.findById("ADMIN").orElseThrow(() -> new AppException(ErrorMessage.UNKNOWN_EXCEPTION));
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("First run app with Administrator account: admin");
            }
        };
    }
}
