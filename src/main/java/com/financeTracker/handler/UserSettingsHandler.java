package com.financeTracker.handler;

import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

public class UserSettingsHandler {

    /**
     * Edits the user's profile name.
     *
     * @param inputData The input interface for receiving data.
     * @param outputData The output interface for displaying messages.
     * @param userController The controller that manages user operations.
     * @param userId The ID of the user whose name is to be changed.
     * @return The location to navigate to after the operation (USER_MENU_LOC).
     */
    public static UserLocation editProfileName(Input inputData, OutputData outputData, UserController userController, Long userId) {
        final String currentName = "Ваше текущее имя - %s, введите новое:";

        User user = userController.getUserById(userId);

        outputData.output(String.format(currentName, user.getName()));
        String newName = inputData.in().toString().trim();

        if (newName.isEmpty()) {
            outputData.output("Имя не может быть пустым. Попробуйте снова.");
            return UserLocation.USER_SETTINGS_LOC;
        }

        user.setName(newName);
        userController.updateUser(user);

        return UserLocation.USER_SETTINGS_LOC;
    }

    /**
     * Resets the user's password and email.
     *
     * @param inputData The input interface for receiving data.
     * @param outputData The output interface for displaying messages.
     * @param userController The controller that manages user operations.
     * @param userId The ID of the user whose password and email are to be reset.
     * @return The location to navigate to after the operation (SECURITY_LOC).
     */
    public static UserLocation resetPasswordAndEmail(Input inputData, OutputData outputData, UserController userController, Long userId) {
        userController.deleteUserById(userId);

        return UserLocation.SECURITY_LOC;
    }

    /**
     * Deletes the user's profile along with all associated data.
     *
     * @param inputData The input interface for receiving data.
     * @param outputData The output interface for displaying messages.
     * @param userController The controller that manages user operations.
     * @param userId The ID of the user to be deleted.
     * @return The location to navigate to after the operation (SECURITY_LOC).
     */
    public static UserLocation deleteProfile(Input inputData, OutputData outputData, UserController userController, Long userId) {
        outputData.output("Вы уверены, что хотите удалить свой профиль? Это действие невозможно отменить. Введите 'да' для подтверждения:");
        String confirmation = inputData.in().toString().trim();

        if ("да".equalsIgnoreCase(confirmation)) {
            userController.deleteCascadeUserById(userId);
            outputData.output("Ваш профиль был успешно удалён.");
        } else {
            outputData.output("Удаление отменено.");
        }

        return UserLocation.SECURITY_LOC;
    }
}

