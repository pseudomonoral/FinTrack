package com.financeTracker.model.entity;


import com.financeTracker.model.type.BudgetStatus;
import com.financeTracker.model.type.GoalStatus;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private String login;

    private String password;

    private String name;

    @Builder.Default
    private UserRole userRole = UserRole.USER;

    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Builder.Default
    private GoalStatus goalStatus = GoalStatus.NOT_ACHIEVED;

    @Builder.Default
    private BudgetStatus budgetStatus = BudgetStatus.WITHIN_BUDGET;
}
