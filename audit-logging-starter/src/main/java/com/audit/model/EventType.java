package com.audit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    REGISTER("Register"),
    LOGIN("Login"),
    LOGOUT("Logout"),
    UPDATE_PROFILE("Update Profile"),
    DELETE_ACCOUNT("Delete Account"),
    BLOCK_ACCOUNT("Block Account"),
    UNBLOCK_ACCOUNT("Unblock Account");


    private final String description;
}