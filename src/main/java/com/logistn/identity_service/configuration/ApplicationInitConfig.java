package com.logistn.identity_service.configuration;

import com.logistn.identity_service.entity.Role;
import com.logistn.identity_service.entity.User;
import com.logistn.identity_service.exception.AppException;
import com.logistn.identity_service.exception.ErrorMessage;
import com.logistn.identity_service.repository.RoleRepository;
import com.logistn.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Value("${app.highestPrivilege.roleName}")
    private String roleName;

    @Value("${app.highestPrivilege.password}")
    private String username;

    @Value("${app.highestPrivilege.password}")
    private String password;

    @Bean
    @ConditionalOnProperty(value = "app.testMode", havingValue = "false")
    ApplicationRunner applicationRunner(
            UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername(username).isEmpty()) {
                Role role = roleRepository
                        .findById(roleName)
                        .orElseThrow(() -> new AppException(ErrorMessage.UNKNOWN_EXCEPTION));
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                User user = User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("First run app with Administrator account: admin");
            }
        };
    }
}
