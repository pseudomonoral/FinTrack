package com.financeTracker.exceptions.authorization;

import com.financeTracker.util.ErrorMessages;

public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super(ErrorMessages.INVALID_PASSWORD);
    }
}