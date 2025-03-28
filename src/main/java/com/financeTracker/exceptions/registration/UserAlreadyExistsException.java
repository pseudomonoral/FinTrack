package com.financeTracker.exceptions.registration;

import com.financeTracker.util.ErrorMessages;

public class UserAlreadyExistsException extends RegistrationException {
    public UserAlreadyExistsException() {
        super(ErrorMessages.USER_ALREADY_EXISTS);
    }
}