package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum MembersErrorMessage {
    NOT_FOUND("Member not found"),
    GAGAL_HAPUS("Failed to delete member");

    private final String message;

    MembersErrorMessage(String message) {
        this.message = message;
    }

}
