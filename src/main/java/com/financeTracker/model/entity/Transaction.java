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

    double amount;

    String category;

    String description;

    TransactionType type;

}
