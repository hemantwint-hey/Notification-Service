package org.example.notificationservice.exception;

public class DuplicateIdempotencyKeyException extends RuntimeException {
    public DuplicateIdempotencyKeyException(String idempotencyKey, Throwable cause) {
        super("A notification already exists for idempotency key: " + idempotencyKey, cause);
    }
}
