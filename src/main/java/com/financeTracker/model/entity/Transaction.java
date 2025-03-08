package com.financeTracker.model.entity;

import com.financeTracker.model.type.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity{

    Long userId;

    double amount;

    String category;

    String description;

    TransactionType type;

    public Transaction(Long id, Long userId,
                       double amount, String category,
                       String description,
                       TransactionType type) {
        super(id);
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.type = type;
    }
}
