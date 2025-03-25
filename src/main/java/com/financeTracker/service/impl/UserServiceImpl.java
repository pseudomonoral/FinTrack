package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.GoalService;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.USER;
import static com.financeTracker.model.type.UserStatus.*;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final BudgetService budgetService;
    private final GoalService goalService;

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
    }


    @Override
    public void deleteUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        userDao.delete(user.getId());
    }

    @Override
    public void deleteAccountUserById(Long userId) {
        userDao.delete(userId);
        if (budgetService.getBudget(userId).isPresent()) {
            budgetService.deleteBudget(userId);
        }
        List<Goal> goalsByUser = goalService.getAllGoals(userId);
        for (Goal goal : goalsByUser) {
            goalService.deleteGoal(goal.getName(), goal.getId());
        }
    }

    @Override
    public void updateUser(User user) {
        if (userDao.findById(user.getId()).isPresent()) {
            Optional<User> old = userDao.findByLogin(user.getLogin());
            if (old.isPresent() && !old.get().getId().equals(user.getId())) {
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
