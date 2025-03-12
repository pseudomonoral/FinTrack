package com.financeTracker.dao.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.model.entity.Goal;
import com.financeTracker.model.entity.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionDaoImpl implements TransactionDao {

    private final Map<Long, Transaction> transactions = new HashMap<>();

    private Long id = 1L;

    @Override
    public Optional<Transaction> findByUserId(Long userId) {
        return transactions.values().stream()
                .filter(goal-> goal.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        if (transactions.containsKey(transaction.getId())) {
            Transaction oldTransaction = transactions.get(transaction.getId());
            transaction.setCreatedAt(oldTransaction.getCreatedAt());
            transaction.onUpdate();
            return transactions.put(id, transaction);
        } else {
            throw new IllegalArgumentException("Transaction with id:" + id + " doesn't exist");
        }
    }

    @Override
    public void delete(Transaction transaction) {
        if(transactions.containsValue(transaction)) {
           transactions.remove(transaction.getId());
        }else {
            throw new IllegalArgumentException("Transaction with id:" + id + " doesn't exist");
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactions.values());
    }

    @Override
    public Transaction save(Transaction entity) {
        entity.setId(id++);
        transactions.put(entity.getId(), entity);
        return transactions.get(entity.getId());
    }
}
