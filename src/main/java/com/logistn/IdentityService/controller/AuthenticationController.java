package com.logistn.IdentityService.controller;

import com.logistn.IdentityService.dto.request.AuthenticationRequest;
import com.logistn.IdentityService.dto.request.IntrospectRequest;
import com.logistn.IdentityService.dto.response.ApiResponse;
import com.logistn.IdentityService.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
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
    ApiResponse<Object> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.authenticate(request));
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<Object> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authenticationService.introspect(request));
        return apiResponse;
    }
}
