package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.exception.CustomValidationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class ErrorController {
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception){
//        return ResponseEntity.status(exception.getStatusCode())
//                .body(WebResponse.<String>builder().status("fail")
//                        .error(exception.getReason()).build());
//    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<WebResponse<Map<String, String>>> validationException(CustomValidationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<Map<String, String>>builder()
                        .status("fail")
                        .error(exception.getErrors())
                        .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<WebResponse<String>> validationException(ValidationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status("fail")
                        .error(exception.getMessage())
                        .build());
    }
}
