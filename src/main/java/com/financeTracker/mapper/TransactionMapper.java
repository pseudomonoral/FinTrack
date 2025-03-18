package com.financeTracker.mapper;

import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;

public class TransactionMapper {
    public Transaction mapToTransaction(Long userId, double amount, String category, String description,
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
