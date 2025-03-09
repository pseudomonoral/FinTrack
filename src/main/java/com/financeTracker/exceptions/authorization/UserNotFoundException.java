package com.financeTracker.exceptions.authorization;

import com.financeTracker.exceptions.ErrorMessages;

public class UserNotFoundException extends AuthorizationException {
    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }
}
