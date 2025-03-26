package com.financeTracker;

import com.financeTracker.controller.*;
import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.GoalDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.BudgetDaoImpl;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dao.impl.TransactionDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.service.*;
import com.financeTracker.service.impl.*;
import com.financeTracker.util.Config;
import com.financeTracker.util.ConfigLoader;
import com.financeTracker.util.DatabaseManager;
import com.financeTracker.util.LiquibaseMigrator;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    static {
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
    }

    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadControllers() {
        AuthController authController = new AuthController((SecurityService) CONTEXT.get("securityService"));
        CONTEXT.put("authController", authController);

        BudgetController budgetController = new BudgetController((BudgetService) CONTEXT.get("budgetService"));
        CONTEXT.put("budgetController", budgetController);

        GoalController goalController = new GoalController((GoalService) CONTEXT.get("goalService"));
        CONTEXT.put("goalController", goalController);

        NotificationController notificationController = new NotificationController((NotificationService)
                CONTEXT.get("notificationService"));
        CONTEXT.put("notificationController", notificationController);

        StatisticsController statisticsController = new StatisticsController((StatisticsService)
                CONTEXT.get("statisticsService"));
        CONTEXT.put("statisticsController", statisticsController);

        TransactionController transactionController = new TransactionController((TransactionService)
                CONTEXT.get("transactionService"));
        CONTEXT.put("transactionController", transactionController);

        UserController userController = new UserController((UserService) CONTEXT.get("userService"));
        CONTEXT.put("userController", userController);
    }

    private static void loadServiceLayer() {
        CONTEXT.put("securityService", new SecurityServiceImpl((UserDao) CONTEXT.get("userDao")));
        CONTEXT.put("budgetService", new BudgetServiceImpl((BudgetDao) CONTEXT.get("budgetDao"),
                (TransactionDao) CONTEXT.get("transactionDao")));
        CONTEXT.put("goalService", new GoalServiceImpl((GoalDao) CONTEXT.get("goalDao")));
        CONTEXT.put("userService", new UserServiceImpl((UserDao) CONTEXT.get("userDao"),
                (BudgetService) CONTEXT.get("budgetService"),
                (GoalService) CONTEXT.get("goalService")));
        CONTEXT.put("transactionService",
                new TransactionServiceImpl((UserService) CONTEXT.get("userService"),
                        (TransactionDao) CONTEXT.get("transactionDao"),
                        (BudgetService) CONTEXT.get("budgetService"),
                        (GoalService) CONTEXT.get("goalService")));
        CONTEXT.put("statisticsService", new StatisticsServiceImpl((TransactionService) CONTEXT.get("transactionService"),
                (GoalService) CONTEXT.get("goalService")));
    }

    private static void loadDAOLayer() {
        Config config = ConfigLoader.loadConfig("config.yaml");
        DatabaseManager databaseManager = new DatabaseManager(config.database);
        LiquibaseMigrator liquibaseMigrator = new LiquibaseMigrator(databaseManager, config.liquibase.changelog);
        liquibaseMigrator.executeMigrations();
        DataSource dataSource = databaseManager.getDataSource();

        CONTEXT.put("userDao", new UserDaoImpl(dataSource));
        CONTEXT.put("transactionDao", new TransactionDaoImpl(dataSource));
        CONTEXT.put("budgetDao", new BudgetDaoImpl(dataSource));
        CONTEXT.put("goalDao", new GoalDaoImpl(dataSource));
    }
}
