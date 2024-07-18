package com.appschef.intern.minimarket.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException {
    private Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super("Input tidak valid");
        this.errors = errors;
    }

}
