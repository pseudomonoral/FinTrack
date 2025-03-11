package com.financeTracker.controller;

import com.financeTracker.model.entity.User;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.financeTracker.model.type.UserRole.*;

@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user corresponding to the given ID.
     */
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUserById(Long id) {
        userService.deleteUserById(id);
    }

    /**
     * Updates the user information.
     *
     * @param user The user object with updated information.
     */
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    /**
     * Blocks a user by their ID.
     *
     * @param id The ID of the user to block.
     */
    public void blockedUserById(Long id) {
        userService.blockedUserById(id);
    }

    /**
     * Unblocks a user by their ID.
     *
     * @param id The ID of the user to unblock.
     */
    public void unblockedUserById(Long id) {
        userService.unblockedUserById(id);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list containing all users.
     */
    public List<User> showAllUsers() {
        return userService.showAll();
    }

    /**
     * Deletes a user and all associated (without transactions) data by their ID.
     * This operation removes the user along with related entities,
     *
     * @param id The ID of the user to delete.
     */
    public void deleteCascadeUserById(Long id) {
        userService.deleteCascadeUserById(id);
    }

    public boolean checkAdminAccess(Long id) {
        User user = userService.getUserById(id);
        return user.getUserRole().equals(ADMIN);
    }

}
