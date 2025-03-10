package com.financeTracker;

import com.financeTracker.controller.AuthController;
import com.financeTracker.exceptions.authorization.AuthorizationException;
import com.financeTracker.exceptions.registration.RegistrationException;
import com.financeTracker.exceptions.validation.NotValidArgumentException;
import com.financeTracker.in.Input;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

import static com.financeTracker.handler.SecurityHandler.handleAuthorization;
import static com.financeTracker.handler.SecurityHandler.handleRegistration;
import static com.financeTracker.util.ConsoleConstants.*;


/**
 * The ApplicationRunner class is responsible for running the main loop of the application.
 * It handles user input, manages different user locations, and delegates tasks to appropriate handlers.
 */
public class ApplicationRunner {

    private static AuthController authController;
    private static UserLocation userLocation;

    /**
     * The main entry point of the application. It loads the application context,
     * retrieves beans, and starts the main loop for processing user input.
     */
    public static void run() {
        ApplicationContext.loadContext();
        Input inputData = (Input) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        authController = (AuthController) ApplicationContext.getBean("authController");
        userLocation = UserLocation.SECURITY;

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userLocation) {
                    case SECURITY -> handleSecurity(inputData, outputData);
                    case MAIN_MENU -> handleMenu(inputData, outputData);
                    case ADMIN_MENU -> handleAdmin(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizationException |
                     NotValidArgumentException |
                     RegistrationException e) {
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                outputData.errOutput("Unknown error. More details " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeIn();
    }

    /**
     * Handles the security stage where users can register, login, or exit the application.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     */
    public static void handleSecurity(Input inputData, OutputData outputData) {
        while (true) {
            outputData.output(SECURITY_MENU);
            Object input = inputData.in();
            if (input.toString().equals("1")) {
                userLocation = handleRegistration(inputData, outputData, authController);
                break;
            } else if (input.toString().equals("2")) {
                userLocation = handleAuthorization(inputData, outputData, authController);
                break;
            } else if (input.toString().equals("3")) {
                userLocation = UserLocation.EXIT;
                break;
            } else {
                outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }



    /**
     * Handles the exit process, displaying a farewell message and cleaning up resources.
     *
     * @param outputData The output data provider.
     */
    private static void exitProcess(OutputData outputData) {
        outputData.output("До свидания!");
        ApplicationContext.cleanAuthorizedUser();
    }
}
