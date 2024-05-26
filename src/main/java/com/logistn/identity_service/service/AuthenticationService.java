package com.logistn.identity_service.service;

import com.logistn.identity_service.dto.request.AuthenticationRequest;
import com.logistn.identity_service.dto.request.IntrospectRequest;
import com.logistn.identity_service.dto.request.LogoutRequest;
import com.logistn.identity_service.dto.request.RefreshTokenRequest;
import com.logistn.identity_service.dto.response.AuthenticationResponse;
import com.logistn.identity_service.dto.response.IntrospectResponse;
import com.logistn.identity_service.dto.response.RefreshTokenResponse;
import com.logistn.identity_service.entity.Permission;
import com.logistn.identity_service.entity.Role;
import com.logistn.identity_service.entity.User;
import com.logistn.identity_service.entity.ValidToken;
import com.logistn.identity_service.exception.AppException;
import com.logistn.identity_service.exception.ErrorMessage;
import com.logistn.identity_service.repository.UserRepository;
import com.logistn.identity_service.repository.ValidTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ValidTokenRepository validTokenRepository;

    @NonFinal
    @Value("${jwt.key}")
    private String secretKey;

    @NonFinal
    @Value("${jwt.atTime}")
    private int atTime;

    @NonFinal
    @Value("${jwt.rtTime}")
    private int rtTime;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ParseException {
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
        boolean isAuth = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuth) {
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }
        WrappedTokens wrappedTokens = saveValidToken(user, null);
        return new AuthenticationResponse(
                wrappedTokens.accessToken.serialize(), wrappedTokens.refreshToken.serialize());
    }

    private WrappedTokens saveValidToken(User user, String prevToken) throws ParseException {
        SignedJWT accessToken = createToken(user, atTime, ChronoUnit.MINUTES);
        SignedJWT refreshToken = createToken(user, rtTime, ChronoUnit.DAYS);
        ValidToken validToken = ValidToken.builder()
                .accessTokenId(accessToken.getJWTClaimsSet().getJWTID())
                .refreshTokenId(refreshToken.getJWTClaimsSet().getJWTID())
                .prevTokenId(prevToken)
                .refreshTime(
                        new Date(Instant.now().plus(rtTime, ChronoUnit.DAYS).toEpochMilli()))
                .build();
        validTokenRepository.save(validToken);
        return new WrappedTokens(accessToken, refreshToken);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {

            SignedJWT accessToken = decodeToken(request.getAccessToken());
            ValidToken validToken = validTokenRepository
                    .findById(accessToken.getJWTClaimsSet().getJWTID())
                    .orElse(null);

        return new IntrospectResponse(validToken == null);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signRefreshToken = decodeToken(request.getRefreshToken());
        ValidToken currentValidToken = validTokenRepository
                .findByRefreshTokenId(signRefreshToken.getJWTClaimsSet().getJWTID())
                .orElse(null);
        if (currentValidToken == null) {
            SignedJWT signAccessToken = decodeToken(request.getAccessToken());
            currentValidToken = validTokenRepository
                    .findByPrevTokenId(signAccessToken.getJWTClaimsSet().getJWTID())
                    .orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
            validTokenRepository.delete(currentValidToken);
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }
        User user = userRepository
                .findByUsername(signRefreshToken.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
        WrappedTokens wrappedTokens = saveValidToken(user, currentValidToken.getAccessTokenId());
        validTokenRepository.delete(currentValidToken);
        return new RefreshTokenResponse(wrappedTokens.accessToken.serialize(), wrappedTokens.refreshToken.serialize());
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT accessToken = decodeToken(request.getAccessToken());
        ValidToken validToken = validTokenRepository
                .findById(accessToken.getJWTClaimsSet().getJWTID())
                .orElseThrow(() -> new AppException(ErrorMessage.UNAUTHENTICATED));
        if (validToken != null) {
            validTokenRepository.delete(validToken);
        }
    }

    public SignedJWT decodeToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(secretKey);

        if (!signedJWT.verify(jwsVerifier)) {
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }

        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        Date expireTime = jwtClaimsSet.getExpirationTime();

        if (expireTime == null || expireTime.before(new Date())) {
            throw new AppException(ErrorMessage.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private SignedJWT createToken(User user, long amountToAdd, TemporalUnit temporalUnit) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .jwtID(UUID.randomUUID().toString())
                .issuer("long")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(amountToAdd, temporalUnit).toEpochMilli()))
                .claim("scope", buildRoles(user))
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT;
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new AppException(ErrorMessage.UNKNOWN_EXCEPTION);
        }
    }

    private String buildRoles(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!user.getRoles().isEmpty()) {
            Set<String> set = new HashSet<>();
            for (Role role : user.getRoles()) {
                set.add("ROLE_" + role.getName());
                for (Permission permission : role.getPermissions()) {
                    set.add(permission.getName());
                }
            }
            for (String str : set) {
                stringJoiner.add(str);
            }
        }
        return stringJoiner.toString();
    }

    private record WrappedTokens(SignedJWT accessToken, SignedJWT refreshToken) {}
}
