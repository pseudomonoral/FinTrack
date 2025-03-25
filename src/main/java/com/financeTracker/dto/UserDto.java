package com.financeTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email не может быть пустым")
    private String login;

    private String userRole;
    private String userStatus;
}