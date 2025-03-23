package com.financeTracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String userRole;
    private String userStatus;
}

