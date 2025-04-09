package com.financeTracker.mapper;

import com.financeTracker.dto.user.UserDto;
import com.financeTracker.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
}