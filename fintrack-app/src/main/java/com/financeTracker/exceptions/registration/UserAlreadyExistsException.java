package com.financeTracker.exceptions.registration;

import com.financeTracker.constants.ErrorMessages;

public class UserAlreadyExistsException extends RegistrationException {
    public UserAlreadyExistsException() {
        super(ErrorMessages.USER_ALREADY_EXISTS);
    }
}