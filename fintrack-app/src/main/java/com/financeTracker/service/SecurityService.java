package com.financeTracker.service;

import com.financeTracker.dto.auth.AuthRequestDto;
import com.financeTracker.exceptions.authorization.InvalidPasswordException;
import com.financeTracker.exceptions.authorization.UserBlockedException;
import com.financeTracker.exceptions.authorization.UserNotFoundException;
import com.financeTracker.exceptions.registration.UserAlreadyExistsException;
import com.financeTracker.model.entity.User;

/**
 * The SecurityService interface provides methods for user registration and authorization.
 * It handles user authentication by managing login credentials.
 */

public interface SecurityService {

    /**
     * Registers a new user using the provided authentication request data.
     *
     * @param authRequestDto the DTO containing login and password for registration.
     * @return the newly created {@link User}.
     * @throws UserAlreadyExistsException if a user with the same login already exists.
     */
    User registration(AuthRequestDto authRequestDto);

    /**
     * Authorizes a user based on the provided credentials.
     *
     * @param authRequestDto the DTO containing login and password for authorization.
     * @return the authorized {@link User}.
     * @throws UserNotFoundException    if the user with the specified login does not exist.
     * @throws UserBlockedException     if the user is blocked.
     * @throws InvalidPasswordException if the provided password is incorrect.
     */
    User authorize(AuthRequestDto authRequestDto);
}

