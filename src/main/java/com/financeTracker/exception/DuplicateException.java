package com.financeTracker.exception;

/**
 * Exception thrown to indicate that an attempt to create a duplicate workspace has occurred.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */
public class DuplicateException extends RuntimeException {

    /**
     * Constructs a DuplicateException with a specific error message.
     *
     * @param message The message indicating the reason for the duplicate workspace creation attempt.
     */
    public DuplicateException(String message) {
        super("workspace " + message + " booked");
    }
}

