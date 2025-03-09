package com.financeTracker.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
public class Budget extends BaseEntity {

    private Long userId;

    private double limit;

    public Budget(Long id, Long userId, double limit) {
        super(id);
        this.userId = userId;
        this.limit = limit;
    }
}
