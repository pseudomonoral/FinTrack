package com.financeTracker.service;

import com.financeTracker.model.entity.User;

/**
 * The SecurityService interface provides methods for user registration and authorization.
 * It handles user authentication by managing login credentials.
 */
public interface SecurityService {

    /**
     * Registers a new user with a login and password.
     *
     * @param login The login of the new user.
     * @param password The password for the new user.
     * @return User The registered user entity.
     */
    User registration(String login, String password);

    /**
     * Authorizes an existing user by verifying their login and password.
     *
     * @param login The login of the user.
     * @param password The password of the user.
     * @return User The authorized user entity.
     */
    User authorize(String login, String password);
}
