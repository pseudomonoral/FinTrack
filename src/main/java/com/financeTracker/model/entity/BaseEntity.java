package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

    Long id;

    Long userId;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public BaseEntity(Long id, Long userId) {
        this.id = id;
        this.userId = userId;

    }

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
