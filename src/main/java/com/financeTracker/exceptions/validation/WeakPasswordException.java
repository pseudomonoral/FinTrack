package com.financeTracker.exceptions.validation;

import com.financeTracker.exceptions.ErrorMessages;
import com.financeTracker.exceptions.registration.RegistrationException;

public class WeakPasswordException extends RegistrationException {
  public WeakPasswordException() {
    super(ErrorMessages.WEAK_PASSWORD);
  }
}