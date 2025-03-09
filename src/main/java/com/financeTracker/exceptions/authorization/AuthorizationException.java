package com.financeTracker.exceptions.authorization;

/**
 * Exception thrown to indicate authorization errors during user authentication.
 * Extends RuntimeException to ensure it can be thrown without explicit handling.
 */

public abstract class AuthorizationException extends RuntimeException {

    /**
     * Constructs an AuthorizeException with a specific error message.
     *
     * @param message The error message detailing the cause of the authorization failure.
     */
    public AuthorizationException(String message) {
        super(message);
    }
}

