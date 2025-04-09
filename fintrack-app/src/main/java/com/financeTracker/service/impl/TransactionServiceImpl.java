package com.financeTracker.service.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.GoalDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dto.TargetUserDto;
import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.exceptions.entity.EntityNotFoundException;
import com.financeTracker.mapper.TransactionMapper;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.financeTracker.model.type.BudgetStatus.OVER_BUDGET;
import static com.financeTracker.model.type.BudgetStatus.WITHIN_BUDGET;
import static com.financeTracker.model.type.EntityName.*;
import static com.financeTracker.model.type.GoalStatus.ACHIEVED;
import static com.financeTracker.model.type.NotificationType.BUDGET_EXCEEDED;
import static com.financeTracker.model.type.NotificationType.GOAL_ACHIEVED;
import static com.financeTracker.model.type.TransactionType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BudgetService budgetService;
    private final NotificationService notificationService;
    private final TransactionDao transactionDao;
    private final GoalDao goalDao;
    private final BudgetDao budgetDao;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(Long userId, TransactionRequestDto transactionRequestDto) {
        Transaction transaction = transactionDao.save(mapToTransaction(userId, transactionRequestDto.getAmount(),
                transactionRequestDto.getCategory(),
                transactionRequestDto.getDescription(),
                transactionRequestDto.getType()));
        applyTransaction(transaction);
        return transactionMapper.toResponseTransactionDto(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionDao.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException(TRANSACTION,
                        transactionId));
        transaction.setType(transaction.getType().equals(INCOME) ? EXPENSE : INCOME);

        applyTransaction(transaction);
        transactionDao.delete(transaction.getId());
    }

    @Override
    @Transactional
    public void updateTransaction(Long userId, Long transactionId,
                                  TransactionRequestDto transactionRequestDto) {
        Optional<Transaction> old = transactionDao.findById(transactionId);
        if (old.isPresent()) {
            Transaction newTransaction = mapToTransaction(userId,
                    transactionRequestDto.getAmount(),
                    transactionRequestDto.getCategory(),
                    transactionRequestDto.getDescription(),
                    transactionRequestDto.getType());
            newTransaction.setId(transactionId);
            newTransaction.setCreatedAt(old.get().getCreatedAt());

            if (!old.get().getType().equals(newTransaction.getType())
            || old.get().getAmount() != newTransaction.getAmount()) {
                applyTransaction(getDiffTransaction(old.get(), newTransaction));
            }

            transactionDao.updateTransaction(newTransaction);
        } else {
            throw new EntityNotFoundException(TRANSACTION, transactionId);
        }
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByUserId(Long userId) {
        return transactionDao.findAllByUserId(userId).stream()
                .map(transactionMapper::toResponseTransactionDto).toList();
    }

    @Override
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionDao.findAll().stream()
                .map(transactionMapper::toResponseTransactionDto).toList();
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByDate(Long userId, LocalDateTime date) {
        return transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(date))
                .map(transactionMapper::toResponseTransactionDto)
                .toList();
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByCategory(Long userId, String category) {
        return transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> transaction.getCategory().equalsIgnoreCase(category))
                .map(transactionMapper::toResponseTransactionDto)
                .toList();
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByType(Long userId, String type) {
        TransactionType transactionType = TransactionType.valueOf(type);
        return transactionDao.findAll().stream()
                .filter(transaction -> Objects.equals(transaction.getUserId(), userId))
                .filter(transaction -> transaction.getType() == transactionType)
                .map(transactionMapper::toResponseTransactionDto)
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

    private void applyTransaction(Transaction transaction){
        goalDao.findAllByUserId(transaction.getUserId()).stream()
                .filter(Goal::isGoalActual)
                .findFirst()
                .ifPresent(goal -> applyTransactionToGoal(transaction, goal));

        if (budgetDao.findByUserId(transaction.getUserId()).isPresent()) {
            applyTransactionToBudget(transaction);
        }
    }

    private void applyTransactionToBudget(Transaction transaction) {
        Long userId = transaction.getUserId();
        if(budgetService.isBudgetExceeded(userId).isExceeded()) {
            notificationService.createNotification(BUDGET_EXCEEDED,
                    new TargetUserDto(userId));
        }
    }

    private void applyTransactionToGoal(Transaction transaction, Goal actualGoal) {
        Long userId = transaction.getUserId();
        double amount = transaction.getAmount();
        if (transaction.getType().equals(EXPENSE)){
            amount = amount * -1;
        }
        actualGoal.setCurrentAmount(actualGoal.getCurrentAmount() + amount);

        if (getGoalProgress(actualGoal) == 100.0){
            actualGoal.setGoalActual(false);
            actualGoal.onUpdate();
            notificationService.createNotification(GOAL_ACHIEVED,
                    new TargetUserDto(userId));
        }

        goalDao.updateGoal(actualGoal);
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

    private double getGoalProgress(Goal goal) {
        return goal.getTargetAmount() == 0 ? 100.0 :
                Math.min((goal.getCurrentAmount() / goal.getTargetAmount()) * 100, 100.0);
    }
}
