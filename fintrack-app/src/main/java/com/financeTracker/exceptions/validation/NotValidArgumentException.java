package com.financeTracker.exceptions.validation;

/**
 * Exception thrown to indicate that an argument provided to a method or operation is not valid.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */
public abstract class NotValidArgumentException extends RuntimeException {

    /**
     * Constructs a NotValidArgumentException with a specific error message.
     *
     * @param message The message indicating the reason why the argument is considered not valid.
     */
    public NotValidArgumentException(String message) {
        super(message);
    }
}
