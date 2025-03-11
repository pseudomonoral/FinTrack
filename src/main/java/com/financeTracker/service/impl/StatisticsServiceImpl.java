package com.financeTracker.service.impl;

import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.StatisticsService;
import com.financeTracker.service.TransactionService;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.TransactionType.*;

@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionService transactionService;
    private final UserService userService;

    @Override
    public double calculateCurrentBalance(Long userId) {
        double income = transactionService.getTransactions(userId).stream()
                .filter(transaction -> transaction.getType().equals(INCOME))
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = transactionService.getTransactions(userId).stream()
                .filter(transaction -> transaction.getType().equals(EXPENSE))
                .mapToDouble(Transaction::getAmount)
                .sum();
        return income - expense;
    }

    @Override
    public double calculateIncomeForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionService.getTransactionsByDate(userId, startDate.atStartOfDay()).stream()
                .filter(transaction -> transaction.getCreatedAt().toLocalDate()
                        .isBefore(endDate.plusDays(1)))
                .filter(transaction -> transaction.getType() == INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double calculateExpenseForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionService.getTransactionsByDate(userId, startDate.atStartOfDay()).stream()
                .filter(transaction -> transaction.getCreatedAt().toLocalDate()
                        .isBefore(endDate.plusDays(1)))
                .filter(transaction -> transaction.getType() == EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public Map<String, Double> analyzeExpensesByCategory(Long userId) {
        return transactionService.getTransactions(userId).stream()
                .filter(transaction -> transaction.getType() == EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));
    }
}
