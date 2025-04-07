package com.financeTracker.dao;

import com.financeTracker.model.entity.User;

import java.util.List;
import java.util.Optional;


/**
 * The UserDao interface extends MainDao to provide data access operations specific to User entities.
 * It adds additional methods for finding a user by login and updating a user.
 */
public interface UserDao {

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

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id The identifier of the entity.
     * @return Optional<T> An Optional containing the entity if found, or an empty Optional if not found.
     */
    Optional<User> findById(Long id);

    /**
     * Retrieves a list of all entities.
     *
     * @return List<T> A list of all entities of type T.
     */
    List<User> findAll();

    /**
     * Saves or updates an entity in the database.
     *
     * @param entity The entity to be saved or updated.
     * @return T The saved entity with its updated state (e.g., with an assigned ID).
     */
    User save(User entity);

    /**
     * Deletes an entity from the database.
     */
    void delete(Long id);
}