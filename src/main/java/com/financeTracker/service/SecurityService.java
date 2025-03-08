package com.financeTracker.service;

import com.financeTracker.model.entity.User;

import java.util.Optional;

public interface SecurityService {

    User register(String login, String password);

    Optional<User> authorize(String login, String password);
}
