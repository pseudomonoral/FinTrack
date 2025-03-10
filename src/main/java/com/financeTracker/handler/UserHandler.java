package com.financeTracker.handler;

import com.financeTracker.controller.UserController;
import com.financeTracker.in.Input;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.UserLocation;
import com.financeTracker.out.OutputData;

public class UserHandler {

    public static UserLocation editProfileName(Input inputData, OutputData outputData, UserController userController, Long userId) {
        final String currentName = "Ваше текущее имя - %s, введите новое:";

        User user = userController.getUserById(userId);

        outputData.output(String.format(currentName, user.getName()));
        String newName = inputData.in().toString();
        user.setName(newName);

        userController.updateUser(user);

        return UserLocation.USER_MENU;
    }

    public static UserLocation resetPasswordAndEmail(Input inputData, OutputData outputData, UserController userController, Long userId) {
        userController.deleteUserById(userId);

        return UserLocation.SECURITY;
    }

    public static UserLocation deleteProfile(Input inputData, OutputData outputData, UserController userController, Long userId) {
        userController.deleteCascadeUserById(userId);

        return UserLocation.SECURITY;
    }
}
