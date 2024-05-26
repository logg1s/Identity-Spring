package com.logistn.identity_service.controller;

import com.logistn.identity_service.dto.request.AuthenticationRequest;
import com.logistn.identity_service.dto.request.IntrospectRequest;
import com.logistn.identity_service.dto.request.LogoutRequest;
import com.logistn.identity_service.dto.request.RefreshTokenRequest;
import com.logistn.identity_service.dto.response.ApiResponse;
import com.logistn.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<Object> authenticate(@RequestBody @Valid AuthenticationRequest request) throws ParseException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.authenticate(request));
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<Object> introspect(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.introspect(request));
        return apiResponse;
    }

    @PostMapping("/logout")
    ApiResponse<Object> logout(@RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        authenticationService.logout(request);
        return apiResponse;
    }

    @PostMapping("/refresh")
    ApiResponse<Object> refresh(@RequestBody @Valid RefreshTokenRequest request) throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.refreshToken(request));
        return apiResponse;
    }
}
