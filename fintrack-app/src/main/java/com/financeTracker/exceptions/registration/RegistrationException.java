package com.financeTracker.exceptions.registration;

/**
 * Exception thrown to indicate errors during user registration.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */
public abstract class RegistrationException extends RuntimeException {

    /**
     * Constructs a RegisterException with a specific error message.
     *
     * @param message The message indicating the reason for the registration error.
     */
    public RegistrationException(String message) {
        super(message);
    }
}

