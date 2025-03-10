package com.constants;

import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.model.type.UserRole;

public interface TestConstants {

    User USER = new User(1L, "login", "password", UserRole.USER);

    Budget BUDGET = new Budget(1L, 1L, 1000.0);

    Goal GOAL = new Goal(1L, 1L, false, "Buy BMW x5",
            2000.0, true);

    Transaction TRANSACTION = new Transaction(1L, 1L, 100L,
            "savings", "some description", TransactionType.INCOME);
}
