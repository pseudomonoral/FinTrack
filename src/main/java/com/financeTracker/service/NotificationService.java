package com.financeTracker.service;

public interface NotificationService {

    void notifySpendingLimitExceeded(Long userId);

    void notifySpendingLimitExceededByEmail(Long userId,
                                              String email);
}
