package com.financeTracker.service.impl;

import com.financeTracker.ApplicationContext;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.BudgetStatus;
import com.financeTracker.model.type.GoalStatus;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.BudgetService;
import com.financeTracker.service.GoalService;
import com.financeTracker.service.TransactionService;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.financeTracker.model.type.BudgetStatus.OVER_BUDGET;
import static com.financeTracker.model.type.BudgetStatus.WITHIN_BUDGET;
import static com.financeTracker.model.type.EntityName.TRANSACTION;
import static com.financeTracker.model.type.GoalStatus.ACHIEVED;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserService userService;
    private final TransactionDao transactionDao;
    private final BudgetService budgetService;
    private final GoalService goalService;

    @Override
    public Transaction createTransaction(double amount, String category, String description, TransactionType type) {
        Long userId = ApplicationContext.getAuthorizeUser().getId();

        Transaction newTransaction = Transaction.builder()
                .amount(amount)
                .userId(userId)
                .category(category)
                .description(description)
                .type(type)
                .build();
        newTransaction.onCreate();
        Transaction transaction = transactionDao.save(newTransaction);

        goalService.getActualGoalByUserId(userId)
                .ifPresent(goal -> applyTransactionToGoal(transaction, goal));

        budgetService.getBudget(userId)
                .ifPresent(budget -> applyTransactionToBudget(transaction));

        return transaction;
    }

    public void applyTransactionToBudget(Transaction transaction) {
        Long userId = transaction.getUserId();
        User user = userService.getUserById(userId);
        if(budgetService.isBudgetExceeded(userId)) {
            user.setBudgetStatus(OVER_BUDGET);
        }else {
            user.setBudgetStatus(WITHIN_BUDGET);
        }
        userService.updateUser(user);
    }

    public void applyTransactionToGoal(Transaction transaction, Goal actualGoal) {
        Long userId = transaction.getUserId();
        double amount = transaction.getAmount();
        actualGoal.setCurrentAmount(actualGoal.getCurrentAmount() + amount);

        if (goalService.getGoalProgress(actualGoal) == 100.0){
            actualGoal.setGoalActual(false);
            actualGoal.onUpdate();
            User user = userService.getUserById(userId);
            user.setGoalStatus(ACHIEVED);
            userService.updateUser(user);
        }
        goalService.updateGoal(userId, actualGoal.getName(),
                actualGoal.getTargetAmount(), actualGoal.isGoalActual());
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRANSACTION, id));

        transactionDao.delete(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        if (transactionDao.findById(transaction.getId()).isPresent()) {
            transactionDao.updateTransaction(transaction);
        } else {
            throw new EntityNotFoundException(TRANSACTION, transaction.getId());
        }
    }



    @Override
    public List<Transaction> getTransactions(Long userId) {
        List<Transaction> transactions = transactionDao.findAll();

        return transactions.stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .toList();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByDate(Long userId, LocalDateTime date) {
        return transactionDao.findAll().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .filter(transaction -> !transaction.getCreatedAt().isBefore(date))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByCategory(Long userId, String category) {
        return transactionDao.findAll().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .filter(transaction -> transaction.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByType(Long userId, TransactionType type) {
        return transactionDao.findAll().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .filter(transaction -> transaction.getType() == type)
                .toList();
    }
}
