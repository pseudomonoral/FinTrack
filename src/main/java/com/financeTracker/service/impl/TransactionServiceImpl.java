package com.financeTracker.service.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.exception.EntityNotFoundException;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.financeTracker.model.type.EntityName.TRANSACTION;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;

    @Override
    public Transaction createTransaction(Long userId,
                                  double amount, String category,
                                  String description,
                                  TransactionType type) {
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .userId(userId)
                .category(category)
                .description(description)
                .type(type)
                .build();
        //TODO: взаимодействие с сервисом бюджета и целей
        transaction.onCreate();
        return transactionDao.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRANSACTION, id));
        transactionDao.delete(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        if (transactionDao.findById(transaction.getId()).isPresent()){
            transactionDao.updateTransaction(transaction);
        }else {
            throw new EntityNotFoundException(TRANSACTION, transaction.getId());
        }
    }

    @Override
    public List<Transaction> getTransactions(int userId) {
        List<Transaction> transactions = transactionDao.findAll();

        return transactions.stream()
                .filter(transaction ->
                        transaction.getUserId() == userId)
                .collect(Collectors.toList());
    }

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
