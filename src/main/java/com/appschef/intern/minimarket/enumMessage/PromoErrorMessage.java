package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum PromoErrorMessage {
    NOT_FOUND("Promo not found"),
    GAGAL_HAPUS("Failed to delete promo"),
    EXIST("Promo already registered"),
    JENIS_INVALID("Invalid promo type"),
    NILAI_INVALID("Invalid promo value");

    private final String message;

    PromoErrorMessage(String message) {
        this.message = message;
    }
}
