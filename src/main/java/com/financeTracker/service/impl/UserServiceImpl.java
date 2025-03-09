package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exception.EntityNotFoundException;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserStatus;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.financeTracker.model.type.EntityName.USER;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
    }


    @Override
    public void deleteUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        userDao.delete(user);
    }

    @Override
    public void blockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(UserStatus.BLOCKED);
        userDao.update(user);
    }

    @Override
    public List<User> showAll() {
        return userDao.findAll();
    }
}
