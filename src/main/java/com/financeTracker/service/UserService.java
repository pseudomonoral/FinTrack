package com.financeTracker.service;

import com.financeTracker.model.entity.User;

import java.util.List;

/**
 * The UserService interface provides methods for managing user accounts.
 * It includes operations for retrieving, deleting, updating, and blocking/unblocking users.
 */
public interface UserService {

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return User The user entity associated with the given ID.
     */
    User getUserById(Long id);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to be deleted.
     */
    void deleteUserById(Long id);

    /**
     * Deletes a user by their unique ID and cascades the deletion of related entities.
     *
     * @param id The ID of the user to be deleted.
     */
    void deleteAccountUserById(Long id);

    /**
     * Updates an existing user.
     *
     * @param user The user entity to be updated.
     */
    void updateUser(User user);

    /**
     * Blocks a user by their unique ID.
     *
     * @param id The ID of the user to be blocked.
     */
    void blockedUserById(Long id);

    /**
     * Unblocks a previously blocked user by their unique ID.
     *
     * @param id The ID of the user to be unblocked.
     */
    void unblockedUserById(Long id);

    /**
     * Retrieves a list of all users.
     *
     * @return List<User> A list of all user entities in the system.
     */
    List<User> showAll();
}

