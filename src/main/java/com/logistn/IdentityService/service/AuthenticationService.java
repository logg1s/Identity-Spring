package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.AuthenticationRequest;
import com.logistn.IdentityService.dto.request.IntrospectRequest;
import com.logistn.IdentityService.dto.response.AuthenticationResponse;
import com.logistn.IdentityService.dto.response.IntrospectResponse;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.secretKey}")
    protected String secretKey;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuth = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuth) {
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }
        String token = generateToken(request.getUsername());
        return new AuthenticationResponse(token);
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws ParseException, JOSEException {
        String token = request.getToken();
        JWSVerifier jwsVerifier = new MACVerifier(secretKey);
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean tokenValid = signedJWT.verify(jwsVerifier);
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return new IntrospectResponse(tokenValid && expireTime.after(new Date()));
    }

    private String generateToken(String username) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("long")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("username", username)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }
}
