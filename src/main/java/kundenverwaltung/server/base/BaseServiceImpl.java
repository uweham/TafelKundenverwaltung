package kundenverwaltung.server.base;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.exception.*;
import kundenverwaltung.server.util.JSONConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public abstract class BaseServiceImpl<T> implements BaseService<T>
{
    protected final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);

    /**
     * Retrieves all entities of a specific type from the server.
     *
     * @param tClass The class of the entity DTO to retrieve.
     * @return A list of all entities, or an empty list if the request fails or returns no content.
     */
    @Override
    public List<T> getAll(Class<T> tClass)
    {
        ServerClient client = new ServerClient();
        String path = "/api/" + getAPIKeyWord() + "/getAll";
        int httpStatus = client.sendRequest("GET", path, null);

        if (httpStatus != 200)
        {
            return Collections.emptyList();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try
        {
            String jsonResponse = client.getResponse();

            if (jsonResponse == null || jsonResponse.trim().isEmpty())
            {
                return Collections.emptyList();
            }

            JavaType listType = objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, tClass);

            return objectMapper.readValue(jsonResponse, listType);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new entity on the server.
     *
     * @param entity The entity to create.
     * @param tClass The class of the entity DTO.
     * @return The created entity as returned by the server, or null if the response is empty.
     * @throws EntityCreationError if the server returns a non-200 status code.
     */
    @Override
    public T create(T entity,  Class<T> tClass) throws EntityCreationError
    {
        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/create";
        String jsonPayload = JSONConverter.toJson(entity);

        int httpStatus = client.sendRequest("POST", path, jsonPayload);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);

            throw new EntityCreationError("httpStatus: " + httpStatus);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try
        {
            String jsonResponse = client.getResponse();

            if (jsonResponse == null || jsonResponse.trim().isEmpty())
            {
                return null;
            }

            return objectMapper.readValue(jsonResponse, tClass);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing entity on the server.
     *
     * @param entity The entity with updated information.
     * @param tClass The class of the entity DTO.
     * @return The updated entity as returned by the server, or null if the response is empty.
     * @throws EntityUpdateError if the server returns a non-200 status code.
     */
    @Override
    public T update(T entity, Class<T> tClass) throws EntityUpdateError
    {
        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/update";
        String jsonPayload = JSONConverter.toJson(entity);

        int httpStatus = client.sendRequest("PUT", path, jsonPayload);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);

            throw new EntityUpdateError("httpStatus: " + httpStatus);

        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try
        {
            String jsonResponse = client.getResponse();

            if (jsonResponse == null || jsonResponse.trim().isEmpty())
            {
                return null;
            }
            return objectMapper.readValue(jsonResponse, tClass);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error parsing update response", e);
        }
    }

    /**
     * Deletes an entity from the server by its UUID.
     *
     * @param uuid The unique identifier of the entity to delete.
     * @throws EntityDeleteError if the server returns a non-200 status code.
     */
    @Override
    public void delete(UUID uuid) throws EntityDeleteError
    {
        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/delete/" + uuid;

        int httpStatus = client.sendRequest("DELETE", path, null);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);

            throw new EntityDeleteError("httpStatus: " + httpStatus);

        }

    }

    /**
     * Returns the specific API keyword for the entity type.
     * This is used to construct the API endpoint path.
     *
     * @return The API keyword as a String.
     */
    public abstract String getAPIKeyWord();
}