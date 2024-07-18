package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum CategoriesErrorMessage {
    NOT_FOUND("Category not found"),
    EXIST("Category already registered"),
    GAGAL_HAPUS("Failed to delete category");

    private final String message;

    CategoriesErrorMessage(String message) {
        this.message = message;
    }
}
