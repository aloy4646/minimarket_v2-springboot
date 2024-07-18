package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum StockErrorMessage {
    INVALID_TYPE("Invalid stock type"),
    EXCEEDS_CURRENT_STOCK("Stock quantity exceeds current stock"),
    TYPE_QUANTITY_MISMATCH("Type and quantity do not match");

    private final String message;

    StockErrorMessage(String message) {
        this.message = message;
    }
}
