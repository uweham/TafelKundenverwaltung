package kundenverwaltung.server.base;

import kundenverwaltung.server.exception.*;

import java.util.List;
import java.util.UUID;

public interface BaseService<T>
{
    /**
     * Retrieves all entities of a specific type from the server.
     *
     * @param tClass The class of the entity DTO to retrieve.
     * @return A list of all entities.
     */
    List<T> getAll(Class<T> tClass);

    /**
     * Creates a new entity on the server.
     *
     * @param entity The entity to create.
     * @param tClass The class of the entity DTO.
     * @return The created entity, as returned by the server.
     * @throws EntityCreationError if the server fails to create the entity.
     */
    T create(T entity, Class<T> tClass) throws EntityCreationError;

    /**
     * Updates an existing entity on the server.
     *
     * @param entity The entity with updated information.
     * @param tClass The class of the entity DTO.
     * @return The updated entity, as returned by the server.
     * @throws EntityUpdateError if the server fails to update the entity.
     */
    T update(T entity, Class<T> tClass) throws EntityUpdateError;

    /**
     * Deletes an entity from the server.
     *
     * @param uuid The unique identifier of the entity to delete.
     * @throws EntityDeleteError if the server fails to delete the entity.
     */
    void delete(UUID uuid) throws EntityDeleteError;
}