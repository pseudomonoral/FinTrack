package com.financeTracker.service;


import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction createTransaction(double amount, String category, String description, TransactionType type);

    void deleteTransaction(Long transactionId);

    void updateTransaction(Transaction transaction);

    List<Transaction> getTransactions(Long userId);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByDate(Long userId, LocalDateTime date);

    List<Transaction> getTransactionsByCategory(Long userId, String category);

    List<Transaction> getTransactionsByType(Long userId, TransactionType type);
}
