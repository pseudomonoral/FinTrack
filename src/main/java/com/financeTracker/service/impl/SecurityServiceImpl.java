package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exceptions.authorization.InvalidPasswordException;
import com.financeTracker.exceptions.authorization.UserBlockedException;
import com.financeTracker.exceptions.authorization.UserNotFoundException;
import com.financeTracker.exceptions.registration.UserAlreadyExistsException;
import com.financeTracker.exceptions.validation.EmptyCredentialsException;
import com.financeTracker.exceptions.validation.WeakPasswordException;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;


import static com.financeTracker.model.type.UserStatus.BLOCKED;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDao userDAO;

    /**
     * Registers a new user with the provided login and password.
     * Throws NotValidArgumentException if the login or password is invalid.
     * Throws RegisterException if a user with the same login already exists.
     *
     * @param login    the login for the new user
     * @param password the password for the new user
     * @return the registered user
     */
    @Override
    public User registration(String login, String password) {
        if (login == null || password == null || login.isBlank() || password.isBlank()) {
            throw new EmptyCredentialsException();
        }
        if (password.length() < 5) {
            throw new WeakPasswordException();
        }
        if (userDAO.findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User newUser = User.builder()
                .login(login)
                .password(password)
                .build();
        newUser.onCreate();
        return userDAO.save(newUser);
    }


    /**
     * Authorizes a user with the provided login and password.
     * Throws AuthorizeException if the login is not found or the password is incorrect.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return an Optional containing the authorized user if successful, or empty if not
     */
    @Override
    public User authorize(String login, String password) {
        User user = userDAO.findByLogin(login)
                .orElseThrow(UserNotFoundException::new);

        if (user.getUserStatus().equals(BLOCKED)) {
            throw new UserBlockedException();
        }

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }

        return user;
    }
}
