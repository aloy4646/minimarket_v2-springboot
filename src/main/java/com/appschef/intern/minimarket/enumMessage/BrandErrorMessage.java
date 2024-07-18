package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum BrandErrorMessage {
    NOT_FOUND("Brand not found"),
    EXIST("Brand already registered"),
    GAGAL_HAPUS("Failed to delete brand");

    private final String message;

    BrandErrorMessage(String message) {
        this.message = message;
    }
}
