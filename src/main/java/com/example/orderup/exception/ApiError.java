package com.example.orderup.exception;

import java.time.Instant;

public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;

    public ApiError(Instant timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
