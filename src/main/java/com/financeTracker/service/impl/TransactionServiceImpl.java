package com.financeTracker.service.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;

    @Override
    public void addTransaction(Transaction transaction) {
        transactionDao.save(transaction);
    }

    @Override
    public List<Transaction> getTransactions(int userId) {
        List<Transaction> transactions = transactionDao.findAll();

        return transactions.stream()
                .filter(transaction ->
                        transaction.getUserId() == userId)
                .collect(Collectors.toList());
    }
    //TODO:создание транзакции
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByDate(int userId, LocalDateTime date) {
        return transactionDao.findAll().stream()
                .filter(transaction ->
                        transaction.getUserId() == userId)
                .filter(transaction ->
                        !transaction.getCreatedAt().isBefore(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTransactionsByCategory(int userId, String category) {
        return transactionDao.findAll().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .filter(transaction ->
                        transaction.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTransactionsByType(int userId, TransactionType type) {
        return transactionDao.findAll().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .filter(transaction -> transaction.getType() == type)
                .collect(Collectors.toList());
    }
}
