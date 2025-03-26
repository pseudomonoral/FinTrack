package com.financeTracker.controller;

import com.financeTracker.dto.AuthRequestDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.mapper.DtoUserMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;
    private final DtoUserMapper dtoUserMapper = Mappers.getMapper(DtoUserMapper.class);

    public UserDto register(AuthRequestDto authRequest) {
        User user = securityService.registration(authRequest.getLogin(), authRequest.getPassword());
        return dtoUserMapper.toUserDto(user);
    }

    public UserDto authorize(AuthRequestDto authRequest) {
        User user = securityService.authorize(authRequest.getLogin(), authRequest.getPassword());
        return dtoUserMapper.toUserDto(user);
    }
}



