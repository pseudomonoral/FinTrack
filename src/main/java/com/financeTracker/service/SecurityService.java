package com.financeTracker.service;

import com.financeTracker.model.entity.User;

public interface SecurityService {

    User registeration(String login, String password);

    User authorize(String login, String password);
}
