package com.financeTracker.model.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Budget extends BaseEntity {

    private Long userId;

    private double limit;
}
