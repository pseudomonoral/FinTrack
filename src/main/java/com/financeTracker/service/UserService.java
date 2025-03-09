package com.financeTracker.service;

import com.financeTracker.model.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    void deleteUserById(Long id);

    void updateUser(User user);

    void blockedUserById(Long id);

    void unblockedUserById(Long id);

    List<User> showAll();
}
