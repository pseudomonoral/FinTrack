package com.financeTracker.service;

import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    void addTransaction(Transaction transaction);

    List<Transaction> getTransactions(int userId);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByDate(int userId, LocalDateTime date);

    List<Transaction> getTransactionsByCategory(int userId, String category);

    List<Transaction> getTransactionsByType(int userId, TransactionType type);
}
