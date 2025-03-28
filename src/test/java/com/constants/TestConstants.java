package com.constants;

import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.model.type.UserRole;

public interface TestConstants {

    User USER = User.builder()
            .id(1L)
            .login("login")
            .password("password")
            .userRole(UserRole.USER)
            .build();

    Budget BUDGET = Budget.builder()
            .id(1L)
            .userId(1L)
            .limit(1000.0)
            .build();

    Goal GOAL = Goal.builder()
            .id(1L)
            .userId(1L)
            .goalCompleted(false)
            .name("Buy BMW x5")
            .targetAmount(2000.0)
            .goalActual(true)
            .build();

    Transaction TRANSACTION = Transaction.builder()
            .id(1L)
            .userId(1L)
            .amount(100L)
            .category("savings")
            .description("some description")
            .type(TransactionType.INCOME)
            .build();

}
