package com.financeTracker.service;

import com.financeTracker.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User getUserById(Long id);

    void deleteUserById(Long id);

    void blockedUserById(Long id);

    List<User> showAll();
}
