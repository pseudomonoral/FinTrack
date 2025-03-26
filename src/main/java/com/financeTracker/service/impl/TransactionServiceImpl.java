package com.financeTracker.service.impl;

import com.financeTracker.ApplicationContext;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.financeTracker.model.type.BudgetStatus.OVER_BUDGET;
import static com.financeTracker.model.type.BudgetStatus.WITHIN_BUDGET;
import static com.financeTracker.model.type.EntityName.TRANSACTION;
import static com.financeTracker.model.type.GoalStatus.ACHIEVED;
import static com.financeTracker.model.type.TransactionType.*;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserService userService;
    private final TransactionDao transactionDao;
    private final BudgetService budgetService;
    private final GoalService goalService;

    @Override
    public Transaction createTransaction(Long userId, double amount, String category, String description, TransactionType type) {
        Transaction transaction = transactionDao.save(mapToTransaction(userId, amount, category, description, type));
        applyTransaction(transaction);
        return transaction;
    }

    private void applyTransaction(Transaction transaction){
        goalService.getActualGoalByUserId(transaction.getUserId())
                .ifPresent(goal -> applyTransactionToGoal(transaction, goal));

        budgetService.getBudget(transaction.getUserId())
                .ifPresent(budget -> applyTransactionToBudget(transaction));
    }

    private void applyTransactionToBudget(Transaction transaction) {
        Long userId = transaction.getUserId();
        User user = userService.getUserById(userId);
        if(budgetService.isBudgetExceeded(userId)) {
            user.setBudgetStatus(OVER_BUDGET);
        }else {
            user.setBudgetStatus(WITHIN_BUDGET);
        }
        userService.updateUser(user);
    }

    private void applyTransactionToGoal(Transaction transaction, Goal actualGoal) {
        Long userId = transaction.getUserId();
        double amount = transaction.getAmount();
        if (transaction.getType().equals(EXPENSE)){
            amount = amount * -1;
        }
        actualGoal.setCurrentAmount(actualGoal.getCurrentAmount() + amount);

        if (goalService.getGoalProgress(actualGoal) == 100.0){
            actualGoal.setGoalActual(false);
            actualGoal.onUpdate();
            User user = userService.getUserById(userId);
            user.setGoalStatus(ACHIEVED);
            userService.updateUser(user);
        }
        goalService.updateGoal(userId, actualGoal.getName(),
                actualGoal.getTargetAmount(), actualGoal.getCurrentAmount(), actualGoal.isGoalActual());
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRANSACTION, id));
        transaction.setType(transaction.getType().equals(INCOME) ? EXPENSE : INCOME);

        applyTransaction(transaction);
        transactionDao.delete(transaction.getId());
    }

    @Override
    public void updateTransaction(Long userId, Long id, double amount, String category, String description,
                                  TransactionType type) {
        Optional<Transaction> old = transactionDao.findById(id);
        if (old.isPresent()) {
            Transaction newTransaction = mapToTransaction(userId, amount, category, description, type);
            newTransaction.setId(id);
            newTransaction.setCreatedAt(old.get().getCreatedAt());
            if (!old.get().getType().equals(newTransaction.getType())
            || old.get().getAmount() != newTransaction.getAmount()) {
                applyTransaction(getDiffTransaction(old.get(), newTransaction));
            }

            transactionDao.updateTransaction(newTransaction);
        } else {
            throw new EntityNotFoundException(TRANSACTION, id);
        }
    }

    private Transaction getDiffTransaction(Transaction old, Transaction newTransaction) {
        double oldAmount = old.getAmount();
        double newAmount = newTransaction.getAmount();

        double diffAmount;
        if (old.getType() == newTransaction.getType()) {
            diffAmount = newAmount - oldAmount;
        } else {
            diffAmount = newAmount + oldAmount;
        }
        return Transaction.builder()
                .type(diffAmount > 0 ? INCOME : EXPENSE)
                .amount(Math.abs(diffAmount))
                .id(old.getId())
                .userId(old.getUserId())
                .build();
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
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(date))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByCategory(Long userId, String category) {
        return transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> transaction.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByType(Long userId, TransactionType type) {
        return transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> transaction.getType() == type)
                .toList();
    }

    private Transaction mapToTransaction(Long userId, double amount, String category, String description,
                                         TransactionType type){
        return Transaction.builder()
                .amount(amount)
                .userId(userId)
                .category(category)
                .description(description)
                .type(type)
                .build();
    }
}
