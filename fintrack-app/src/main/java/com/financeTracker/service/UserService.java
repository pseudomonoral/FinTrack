package com.financeTracker.service;

import com.financeTracker.dto.user.UserDto;
import com.financeTracker.dto.user.UserUpdateRequestDto;
import com.financeTracker.exceptions.entity.EntityAlreadyExistException;
import com.financeTracker.exceptions.entity.EntityNotFoundException;

import java.util.List;

/**
 * Service interface for managing users.
 * Provides methods for retrieving, updating, deleting, blocking, and unblocking users.
 */
public interface UserService {

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a {@link UserDto} representing the user
     * @throws EntityNotFoundException if the user is not found
     */
    UserDto getUserById(Long id);

    /**
     * Deletes a user account by their ID.
     * Used for direct account removal, for example, by the user themself.
     *
     * @param userId the ID of the user to delete
     */
    void deleteAccountUserById(Long userId);

    /**
     * Updates user information such as login, password, and name.
     *
     * @param userId        the ID of the user to update
     * @param userUpdateRequestDto the DTO containing the new user data
     * @throws EntityAlreadyExistException if the new login is already taken by another user
     * @throws EntityNotFoundException     if the user is not found
     */
    void updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto);

    /**
     * Blocks a user by their ID.
     * This will set the user status to {@code BLOCKED}.
     *
     * @param id the ID of the user to block
     * @throws EntityNotFoundException if the user is not found
     */
    void blockedUserById(Long id);

    /**
     * Unblocks a user by their ID.
     * This will set the user status to {@code ACTIVE}.
     *
     * @param id the ID of the user to unblock
     * @throws EntityNotFoundException if the user is not found
     */
    void unblockedUserById(Long id);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of {@link UserDto}
     */
    List<UserDto> showAll();
}