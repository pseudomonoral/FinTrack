package com.financeTracker.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Budget extends BaseEntity {

    Long userId;

    double limit;

    public Budget(Long id, Long userId, double limit) {
        super(id);
        this.userId = userId;
        this.limit = limit;
    }
}
