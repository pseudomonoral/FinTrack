package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exception.AuthorizeException;
import com.financeTracker.exception.NotValidArgumentException;
import com.financeTracker.exception.RegisterException;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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
    public User register(String login, String password) {
        if (login == null || password == null || login.isEmpty()
                || password.isEmpty() || login.isBlank() || password.isBlank()) {
            throw new NotValidArgumentException("логин и пароль не должны быть пустыми");
        }
        if (password.length() < 5) {
            throw new NotValidArgumentException("Длина пароля должна составлять не менее 5 символов.");
        }
        Optional<User> optionalUser = userDAO.findByLogin(login);
        if (optionalUser.isPresent()) {
            throw new RegisterException("Пользователь с таким логином уже существует.");
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
    public Optional<User> authorize(String login, String password) {
        Optional<User> user = userDAO.findByLogin(login);
        if (user.isEmpty()) {
            throw new AuthorizeException("Нет пользователя с таким логином");
        }
        if (!user.get().getPassword().equals(password)) {
            throw new AuthorizeException("Не правильный пароль");
        }
        return user;
    }

}
