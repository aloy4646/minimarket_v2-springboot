package com.appschef.intern.minimarket.enumMessage;

import lombok.Getter;

@Getter
public enum ErrorDateMessage {
    INVALID_END_DATE("Invalid end date"),
    START_OR_END_DATE_NULL("start_date and end_date must be provided");

    private final String message;

    ErrorDateMessage(String message) {
        this.message = message;
    }
}
