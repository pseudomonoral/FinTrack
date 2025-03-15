package com.financeTracker.exceptions.authorization;

import com.financeTracker.constants.ErrorMessages;

public class InvalidPasswordException extends AuthorizationException {
    public InvalidPasswordException() {
        super(ErrorMessages.INVALID_PASSWORD);
    }
}