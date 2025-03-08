package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//TODO: убрать
public abstract class BaseEntity {

    Long id;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
