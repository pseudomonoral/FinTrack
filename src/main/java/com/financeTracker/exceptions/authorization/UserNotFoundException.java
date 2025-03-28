package com.financeTracker.exceptions.authorization;

import com.financeTracker.constants.ErrorMessages;

public class UserNotFoundException extends AuthorizationException {
    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }
}
