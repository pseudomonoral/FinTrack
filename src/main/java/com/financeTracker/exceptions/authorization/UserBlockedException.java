package com.financeTracker.exceptions.authorization;

import com.financeTracker.util.ErrorMessages;

public class UserBlockedException extends AuthorizationException {
    public UserBlockedException() {
        super(ErrorMessages.USER_BLOCKED);
    }
}