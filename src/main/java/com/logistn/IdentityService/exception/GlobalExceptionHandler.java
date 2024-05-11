package com.logistn.IdentityService.exception;

import com.logistn.IdentityService.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Object getErrorMessage(FieldError error) {
        try {
            return ErrorMessage.valueOf(error.getDefaultMessage()).getMessage();
        } catch (IllegalArgumentException e) {
            String s = "Invalid key error enum: " + error.getDefaultMessage();
            System.out.println(s);
            return s;
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handlingException(Exception exception) {
        ErrorMessage errorMessage = ErrorMessage.UNKNOWN_EXCEPTION;

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorMessage.getCode());
        apiResponse.setMessage(errorMessage.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlingException(NoResourceFoundException exception) {
        ErrorMessage errorMessage = ErrorMessage.NO_RESOURCE_FOUND_EXCEPTION;

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorMessage.getCode());
        apiResponse.setMessage(errorMessage.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        ErrorMessage errorMessage = exception.getErrorMessage();

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorMessage.getCode());
        apiResponse.setMessage(errorMessage.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<Object, Object> errorMap = exception.getFieldErrors().stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        GlobalExceptionHandler::getErrorMessage,
                        (existing, replacement) -> existing));

        ErrorMessage errorMessage = ErrorMessage.VALIDATOR_EXCEPTION;
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorMessage.getCode());
        apiResponse.setMessage(errorMessage.getMessage());
        apiResponse.setResult(errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
