package com.financeTracker.model.entity;


import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * Represents a user in the system.
 */
@Data
@Builder
public class User {

    /**
     * Unique identifier of the entity.
     */
    private Long id;

    /**
     * Timestamp when the entity was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Initializes timestamps when the entity is created.
     */
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the timestamp when the entity is modified.
     */
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * User's login (username).
     */
    private String login;

    /**
     * User's password (hashed for security).
     */
    private String password;

    /**
     * User's full name.
     */
    private String name;

    /**
     * Role of the user in the system. Default is USER.
     */
    @Builder.Default
    private UserRole userRole = UserRole.USER;

    /**
     * Status of the user (active, inactive, etc.). Default is ACTIVE.
     */
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;
}
