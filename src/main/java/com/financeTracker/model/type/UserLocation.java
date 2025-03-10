package com.financeTracker.model.type;

/**
 * Enum representing various user locations or states within an application.
 * These locations define different screens or menus the user can navigate to.
 */
public enum UserLocation {

    /**
     * Represents the security or login screen.
     */
    SECURITY_LOC,

    /**
     * Represents the main menu screen.
     */
    MAIN_MENU_LOC,

    /**
     * Represents the admin menu screen.
     */
    ADMIN_MENU_LOC,

    USER_MENU_LOC,

    BUDGET_MENU_LOC,

    TRANSACTION_MENU_LOC,

    GOALS_MENU_LOC,

    STATISTICS_MENU_LOC,

    /**
     * Represents the state where the user exits the application.
     */
    EXIT
}
