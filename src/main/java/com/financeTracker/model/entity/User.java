package com.financeTracker.model.entity;


import com.financeTracker.model.type.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    String login;

    String password;

    @Builder.Default
    UserRole userRole = UserRole.USER;

}
