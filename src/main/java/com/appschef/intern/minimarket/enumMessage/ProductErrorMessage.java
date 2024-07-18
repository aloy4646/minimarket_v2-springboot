package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum ProductErrorMessage {
    NOT_FOUND("Product not found"),
    EXIST("Product already registered"),
    GAGAL_HAPUS("Failed to delete produk");

    private final String message;

    ProductErrorMessage(String message) {
        this.message = message;
    }
}
