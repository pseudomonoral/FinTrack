package com.financeTracker.dao;

import com.financeTracker.model.entity.User;

import java.util.Optional;


/**
 * The UserDao interface extends MainDao to provide data access operations specific to User entities.
 * It adds additional methods for finding a user by login and updating a user.
 *
 * @see MainDao
 */
public interface UserDao extends MainDao<Long, User> {

    /**
     * Finds a user by their login.
     *
     * @param login The login of the user.
     * @return Optional<User> An Optional containing the user if found, or an empty Optional if not found.
     */
    Optional<User> findByLogin(String login);

    /**
     * Updates a user's information in the database.
     *
     * @param user The user entity to be updated.
     */
    void update(User user);
}