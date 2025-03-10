package com.financeTracker;

import com.financeTracker.controller.AuthController;
import com.financeTracker.dao.BudgetDao;
import com.financeTracker.dao.GoalDao;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.dao.UserDao;
import com.financeTracker.dao.impl.BudgetDaoImpl;
import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.dao.impl.TransactionDaoImpl;
import com.financeTracker.dao.impl.UserDaoImpl;
import com.financeTracker.in.ConsoleInput;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.mapper.GoalMapper;
import com.financeTracker.model.entity.User;
import com.financeTracker.out.ConsoleOutputData;
import com.financeTracker.service.*;
import com.financeTracker.service.impl.*;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    public static void loadContext() {
        loadDAOLayer();
        loadMapper();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayers();
    }

    public static void loadAuthorizedUser(User user) {
        CONTEXT.put("authorize", user);
    }

    public static void cleanAuthorizedUser() {
        CONTEXT.remove("authorize");
    }

    public static User getAuthorizeUser() {
        return (User) CONTEXT.get("authorize");
    }

    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadInputOutputLayers() {
        CONTEXT.put("input", new ConsoleInput());
        CONTEXT.put("output", new ConsoleOutputData());
    }

    private static void loadControllers() {
        AuthController authController = new AuthController((SecurityService) CONTEXT.get("securityService"));
        CONTEXT.put("authController", authController);
    }

    private static void loadServiceLayer() {
        CONTEXT.put("securityService", new SecurityServiceImpl((UserDao) CONTEXT.get("userDao")));
        CONTEXT.put("budgetService", new BudgetServiceImpl((BudgetDao) CONTEXT.get("budgetDao"),
                (TransactionService) CONTEXT.get("transactionService"),
                (BudgetMapper) CONTEXT.get("budgetMapper")));
        CONTEXT.put("goalService", new GoalServiceImpl((GoalDao) CONTEXT.get("goalDao"),
                (GoalMapper) CONTEXT.get("goalMapper")));
        CONTEXT.put("userService", new UserServiceImpl((UserDao) CONTEXT.get("userDao"),
                        (BudgetService) CONTEXT.get("budgetService"),
                        (GoalService) CONTEXT.get("goalService")));
        CONTEXT.put("transactionService",
                new TransactionServiceImpl((UserService) CONTEXT.get("userService"),
                (TransactionDao) CONTEXT.get("transactionDao"),
                (BudgetService) CONTEXT.get("budgetService"),
                (GoalService) CONTEXT.get("goalService")));
    }

    private static void loadDAOLayer() {
        CONTEXT.put("userDao", new UserDaoImpl());
        CONTEXT.put("transactionDao", new TransactionDaoImpl());
        CONTEXT.put("budgetDao", new BudgetDaoImpl());
        CONTEXT.put("goalDao", new GoalDaoImpl());
    }

    private static void loadMapper(){
        CONTEXT.put("budgetMapper", new BudgetDaoImpl());
        CONTEXT.put("goalMapper", new GoalMapper());
    }
}
