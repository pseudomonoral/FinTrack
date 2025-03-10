package com.constants;

import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;

public interface TestConstants {

    User USER = (User) User.builder()
            .login("login")
            .password("password")
            .id(1L)
            .build();

    Budget BUDGET = (Budget) Budget.builder()
            .limit(1000.0)
            .userId(1L)
            .id(1L)
            .build();

    Goal GOAL = (Goal) Goal.builder()
            .userId(1L)
            .name("Buy BMW x5")
            .targetAmount(2000.0)
            .id(1L)
            .build();

    Transaction TRANSACTION = (Transaction) Transaction.builder()
            .userId(1L)
            .amount(100L)
            .type(TransactionType.INCOME)
            .description("some description")
            .category("savings")
            .id(1L)
            .build();
}
