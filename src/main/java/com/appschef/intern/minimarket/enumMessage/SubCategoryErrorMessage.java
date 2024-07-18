package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum SubCategoryErrorMessage {
    NOT_FOUND("Subcategory not found"),
    EXIST("Subcategory already registered"),
    FAILED_DELETE("Failed to delete subcategory");

    private final String message;

    SubCategoryErrorMessage(String message) {
        this.message = message;
    }
}
