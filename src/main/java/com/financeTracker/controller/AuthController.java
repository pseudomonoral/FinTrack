package com.financeTracker.controller;

import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param login    The login username for the new user.
     * @param password The password for the new user.
     * @return The registered User object.
     */
    public User register(String login, String password) {
        return securityService.registration(login, password);
    }

    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    The login username of the user to authorize.
     * @param password The password of the user to authorize.
     * @return An Optional containing the authorized User object, if authorization is successful.
     */
    public User authorize(String login, String password) {
        return securityService.authorize(login, password);
    }
}
