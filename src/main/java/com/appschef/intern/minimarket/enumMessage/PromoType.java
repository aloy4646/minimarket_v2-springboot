package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum PromoType {
    PERCENT("PERCENT"),
    FLAT_AMOUNT("FLAT AMOUNT");

    private final String type;

    PromoType(String type) {
        this.type = type;
    }
}
