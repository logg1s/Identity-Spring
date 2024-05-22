package com.logistn.IdentityService.configuration;

import com.logistn.IdentityService.entity.Role;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.repository.RoleRepository;
import com.logistn.IdentityService.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Bean
    @ConditionalOnProperty(value = "app.testMode", havingValue = "false")
    ApplicationRunner applicationRunner(
            UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role role = roleRepository
                        .findById("ADMIN")
                        .orElseThrow(() -> new AppException(ErrorMessage.UNKNOWN_EXCEPTION));
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
