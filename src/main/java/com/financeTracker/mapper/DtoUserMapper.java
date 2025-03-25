package com.financeTracker.mapper;

import com.financeTracker.dto.UserDto;
import com.financeTracker.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DtoUserMapper {

    @Mapping(source = "userRole", target = "userRole")
    @Mapping(source = "userStatus", target = "userStatus")
    UserDto toDto(User user);
}

