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

    double limit;

}
