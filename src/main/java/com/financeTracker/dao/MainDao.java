package com.financeTracker.dao;

import java.util.List;
import java.util.Optional;

/**
 * The MainDao interface provides an abstraction for performing basic CRUD operations
 * (Create, Read, Update, Delete) on entities in the database.
 *
 * @param <I> The type of the entity identifier.
 * @param <T> The type of the entity (model to be saved, retrieved, or deleted).
 */
public interface MainDao <I,T>{

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id The identifier of the entity.
     * @return Optional<T> An Optional containing the entity if found, or an empty Optional if not found.
     */
    Optional<T> findById(I id);

    /**
     * Retrieves a list of all entities.
     *
     * @return List<T> A list of all entities of type T.
     */
    List<T> findAll();

    /**
     * Saves or updates an entity in the database.
     *
     * @param entity The entity to be saved or updated.
     * @return T The saved entity with its updated state (e.g., with an assigned ID).
     */
    T save(T entity);

    /**
     * Deletes an entity from the database.
     *
     * @param entity The entity to be deleted.
     */
    void delete(T entity);
}
