package com.logistn.IdentityService.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    @Lazy
    private CustomJwtDecoder customJwtDecoder;

    private final String[] PUBLIC_POST_ENDPOINTS = {
        "/users", "/auth/login", "/auth/introspect", "/auth/logout", "/auth/refresh"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    //    private JwtDecoder jwtDecoder() {
    //        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), MacAlgorithm.HS512.toString());
    //        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
    //                .macAlgorithm(MacAlgorithm.HS512)
    //                .build();
    //    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
