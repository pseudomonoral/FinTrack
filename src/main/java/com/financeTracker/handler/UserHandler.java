package com.financeTracker.handler;

import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.User;
import com.financeTracker.out.OutputData;

public class UserHandler {

    public static void editProfileName(Input inputData, OutputData outputData, UserController userController, Long userId) {
        final String currentName = "Ваше текущее имя - %s, введите новое:";

        User user = userController.getUserById(userId);

        outputData.output(String.format(currentName, user.getName()));
        String newName = inputData.in().toString();
        user.setName(newName);

        userController.updateUser(user);
    }

    public static void resetPasswordAndEmail(Input inputData, OutputData outputData, UserController userController, Long userId) {
        final String currentName = "Ваш текущий email - %s, введите новый:";
        User user = userController.getUserById(userId);
        outputData.output(String.format(currentName, user.getLogin()));

        String newName = inputData.in().toString();
        user.setName(newName);

        userController.updateUser(user);
    }
}
