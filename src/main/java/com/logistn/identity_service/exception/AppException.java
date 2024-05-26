package com.logistn.identity_service.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public AppException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
