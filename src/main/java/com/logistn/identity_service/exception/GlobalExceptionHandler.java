package com.logistn.identity_service.exception;

import com.logistn.identity_service.dto.response.ApiResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Object getErrorMessage(FieldError error) {
        try {
            return ErrorMessage.valueOf(error.getDefaultMessage()).getMessage();
        } catch (IllegalArgumentException e) {
            return error.getDefaultMessage();
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handlingException(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.UNKNOWN_EXCEPTION;

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage() != null ? errorMessage.getMessage() : exception.getMessage())
                .build();

        return ResponseEntity.status(errorMessage.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlingException(NoResourceFoundException exception) {
        ErrorMessage errorMessage = ErrorMessage.NO_RESOURCE_FOUND_EXCEPTION;

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage() != null ? errorMessage.getMessage() : exception.getMessage())
                .build();

        return ResponseEntity.status(errorMessage.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAccessDenied() {
        ErrorMessage errorMessage = ErrorMessage.UNAUTHORIZED;

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage())
                .build();

        return ResponseEntity.status(errorMessage.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        ErrorMessage errorMessage = exception.getErrorMessage();

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage() != null ? errorMessage.getMessage() : exception.getMessage())
                .build();

        return ResponseEntity.status(errorMessage.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        Map<Object, Object> errorMap = exception.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        GlobalExceptionHandler::getErrorMessage,
                        (existing, replacement) -> existing));

        ErrorMessage errorMessage = ErrorMessage.VALIDATOR_EXCEPTION;
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage() != null ? errorMessage.getMessage() : exception.getMessage())
                .result(errorMap)
                .build();
        return ResponseEntity.status(errorMessage.getStatusCode()).body(apiResponse);
    }
}
