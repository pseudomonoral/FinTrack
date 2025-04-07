package com.financeTracker.controller;

import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.mapper.UserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserDto register(AuthRequestDto authRequest) {
        User user = securityService.registration(authRequest.getLogin(), authRequest.getPassword());
        return userMapper.toUserDto(user);
    }

    public UserDto authorize(AuthRequestDto authRequest) {
        User user = securityService.authorize(authRequest.getLogin(), authRequest.getPassword());
        return userMapper.toUserDto(user);
    }
}



