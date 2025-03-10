package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.financeTracker.model.type.EntityName.USER;
import static com.financeTracker.model.type.UserStatus.*;

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
    public void updateUser(User user) {
        if (userDao.findById(user.getId()).isPresent()) {
            String login = user.getLogin();
            if (userDao.findByLogin(login).isPresent()){
                throw new EntityAlreadyExistException(USER, user.getId());
            }
            userDao.update(user);
        }else {
            throw new EntityNotFoundException(USER, user.getId());
        }
    }

    @Override
    public void blockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(BLOCKED);
        userDao.update(user);
    }

    @Override
    public void unblockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(ACTIVE);
        userDao.update(user);
    }

    @Override
    public List<User> showAll() {
        return userDao.findAll();
    }
}
