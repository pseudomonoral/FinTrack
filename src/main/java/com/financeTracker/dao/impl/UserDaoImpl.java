package com.financeTracker.dao.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;

import java.util.*;

public class UserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();

    private Long id = 1L;

    public UserDaoImpl() {
        save(new User(id, "Admin", "12345", UserRole.ADMIN));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        for (User user : users.values()) {
            if (user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            return users.get(id);
        } else {
            throw new IllegalArgumentException("User: \"" + user + "\" doesn't exist");
        }
    }

    @Override
    public void delete(User entity) {
        if (users.containsValue(entity)) {
            users.remove(entity.getId());
        } else {
            throw new IllegalArgumentException("User: \""
                    + entity.toString() + "\" doesn't exist");
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User save(User entity) {
        entity.setId(id++);
        users.put(entity.getId(), entity);
        return users.get(entity.getId());
    }
}
