package com.financeTracker.exception;

/**
 * Exception thrown to indicate authorization errors during user authentication.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */
public class AuthorizeException extends RuntimeException {

    /**
     * Constructs an AuthorizeException with a specific error message.
     *
     * @param message The error message detailing the cause of the authorization failure.
     */
    public AuthorizeException(String message) {
        super("Authorization error.\n" + message);
    }
}

