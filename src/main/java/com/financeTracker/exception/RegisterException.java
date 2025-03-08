package com.financeTracker.exception;

/**
 * Exception thrown to indicate errors during user registration.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */
public class RegisterException extends RuntimeException {

    /**
     * Constructs a RegisterException with a specific error message.
     *
     * @param message The message indicating the reason for the registration error.
     */
    public RegisterException(String message) {
        super("Registration error.\n" + message);
    }
}

