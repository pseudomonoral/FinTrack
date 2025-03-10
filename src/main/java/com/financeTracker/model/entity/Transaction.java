package com.financeTracker.model.entity;

import com.financeTracker.model.type.TransactionType;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

    private Long userId;

    private double amount;

    private String category;

    private String description;

    private TransactionType type;
}
