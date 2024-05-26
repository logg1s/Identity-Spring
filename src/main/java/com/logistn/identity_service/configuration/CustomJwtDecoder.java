package com.logistn.identity_service.configuration;

import com.logistn.identity_service.exception.AppException;
import com.logistn.identity_service.exception.ErrorMessage;
import com.logistn.identity_service.repository.ValidTokenRepository;
import com.logistn.identity_service.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final AuthenticationService authenticationService;

    private final ValidTokenRepository validTokenRepository;

    public CustomJwtDecoder(
            @Lazy AuthenticationService authenticationService, @Lazy ValidTokenRepository validTokenRepository) {
        this.authenticationService = authenticationService;
        this.validTokenRepository = validTokenRepository;
    }

    @Value("${jwt.key}")
    private String secretKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = authenticationService.decodeToken(token);
            String accessTokenId = signedJWT.getJWTClaimsSet().getJWTID();
            validTokenRepository
                    .findById(accessTokenId)
                    .orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
        } catch (Exception e) {
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), MacAlgorithm.HS512.toString());
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build()
                .decode(token);
    }
}
