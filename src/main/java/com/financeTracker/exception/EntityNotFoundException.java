package com.financeTracker.exception;

import com.financeTracker.model.type.EntityName;

public class EntityNotFoundException extends RuntimeException {
    private final static String EXCEPTION_MESSAGE = "Entity %s with id %s not found";

    public EntityNotFoundException(EntityName entityType, Long id) {
        super(EXCEPTION_MESSAGE.formatted(entityType.getValue(), id));
    }
}
