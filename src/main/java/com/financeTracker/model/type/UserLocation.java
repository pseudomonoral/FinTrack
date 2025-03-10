package com.financeTracker.model.type;

/**
 * Enum representing various user locations or states within an application.
 * These locations define different screens or menus the user can navigate to.
 */
public enum UserLocation {

    /**
     * Represents the security or login screen.
     */
    SECURITY,

    /**
     * Represents the main menu screen.
     */
    MAIN_MENU,

    /**
     * Represents the admin menu screen.
     */
    ADMIN_MENU,

    USER_MENU,

    BUDGET_MENU,

    TRANSACTION_MENU,

    GOALS_MENU,

    /**
     * Represents the state where the user exits the application.
     */
    EXIT
}
