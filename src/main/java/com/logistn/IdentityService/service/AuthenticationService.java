package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.AuthenticationRequest;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return false;
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
