package com.logistn.IdentityService.configuration;

import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.repository.ValidTokenRepository;
import com.logistn.IdentityService.service.AuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    @Lazy
    private AuthenticationService authenticationService;

    @Autowired
    @Lazy
    private ValidTokenRepository validTokenRepository;

    @Value("${jwt.Key}")
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
