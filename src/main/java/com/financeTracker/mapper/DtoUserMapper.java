package com.financeTracker.mapper;

import com.financeTracker.dto.UserDto;
import com.financeTracker.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface DtoUserMapper {
    UserDto toUserDto(User user);
}