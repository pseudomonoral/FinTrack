package com.financeTracker.model.entity;


import com.financeTracker.model.type.BudgetStatus;
import com.financeTracker.model.type.GoalStatus;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.*;

import static com.financeTracker.model.type.BudgetStatus.*;
import static com.financeTracker.model.type.GoalStatus.NOT_ACHIEVED;
import static com.financeTracker.model.type.UserRole.USER;
import static com.financeTracker.model.type.UserStatus.ACTIVE;

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
public class User extends BaseEntity {

    private String login;

    private String password;

    private String name;

    @Builder.Default
    private UserRole userRole = USER;

    @Builder.Default
    private UserStatus userStatus = ACTIVE;

    @Builder.Default
    private GoalStatus goalStatus = NOT_ACHIEVED;

    @Builder.Default
    private BudgetStatus budgetStatus = WITHIN_BUDGET;

    public User(Long id, String login, String password, UserRole userRole) {
        super(id);
        this.login = login;
        this.password = password;
        this.userRole = userRole;
    }
}
