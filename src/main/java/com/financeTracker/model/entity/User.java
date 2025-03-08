package com.financeTracker.model.entity;


import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.financeTracker.model.type.UserRole.USER;
import static com.financeTracker.model.type.UserStatus.ACTIVE;

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
    UserRole userRole = USER;

    @Builder.Default
    UserStatus userStatus = ACTIVE;

    public User(Long id, String login, String password, UserRole userRole, UserStatus userStatus) {
        super(id);
        this.login = login;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }
}
