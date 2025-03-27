package com.financeTracker.mapper;

import com.financeTracker.dto.UserDto;
import com.financeTracker.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
}
