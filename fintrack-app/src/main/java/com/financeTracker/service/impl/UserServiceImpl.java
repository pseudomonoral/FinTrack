package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.dto.user.UserUpdateDto;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.model.type.EntityName.USER;
import static com.financeTracker.model.type.UserStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id)));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        userDao.delete(user.getId());
    }

    @Override
    @Transactional
    public void deleteAccountUserById(Long userId) {
        userDao.delete(userId);
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserUpdateDto userUpdateDto) {
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            Optional<User> old = userDao.findByLogin(userUpdateDto.getLogin());
            if (old.isPresent() && !old.get().getId().equals(userId)) {
                throw new EntityAlreadyExistException(USER, userId);
            }
            User userForUpdate = user.get();
            userForUpdate.setLogin(userUpdateDto.getLogin());
            userForUpdate.setPassword(userUpdateDto.getPassword());
            userForUpdate.setName(userUpdateDto.getName());

            userDao.update(userForUpdate);
        }else {
            throw new EntityNotFoundException(USER, userId);
        }
    }

    @Override
    @Transactional
    public void blockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(BLOCKED);
        userDao.update(user);
    }

    @Override
    @Transactional
    public void unblockedUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER, id));
        user.setUserStatus(ACTIVE);
        userDao.update(user);
    }

    @Override
    public List<UserDto> showAll() {
        return userDao.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }
}
