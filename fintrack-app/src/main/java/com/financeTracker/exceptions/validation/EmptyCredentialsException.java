package com.financeTracker.exceptions.validation;

import com.financeTracker.constants.ErrorMessages;
import com.financeTracker.exceptions.registration.RegistrationException;

public class EmptyCredentialsException extends RegistrationException {
    public EmptyCredentialsException() {
        super(ErrorMessages.EMPTY_CREDENTIALS);
    }
}