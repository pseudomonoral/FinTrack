package com.financeTracker.service.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.exceptions.authorization.InvalidPasswordException;
import com.financeTracker.exceptions.authorization.UserBlockedException;
import com.financeTracker.exceptions.authorization.UserNotFoundException;
import com.financeTracker.exceptions.registration.UserAlreadyExistsException;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.financeTracker.model.type.UserStatus.BLOCKED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDao userDAO;


    @Transactional
    @Override
    public User registration(AuthRequestDto authRequestDto) {
        if (userDAO.findByLogin(authRequestDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User newUser = User.builder()
                .login(authRequestDto.getLogin())
                .password(authRequestDto.getPassword())
                .build();
        newUser.onCreate();
        return userDAO.save(newUser);
    }


    @Override
    public User authorize(AuthRequestDto authRequestDto) {
        User user = userDAO.findByLogin(authRequestDto.getLogin())
                .orElseThrow(UserNotFoundException::new);

        if (user.getUserStatus().equals(BLOCKED)) {
            throw new UserBlockedException();
        }

        if (!user.getPassword().equals(authRequestDto.getPassword())) {
            throw new InvalidPasswordException();
        }

        return user;
    }
}
