package kundenverwaltung.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kundenverwaltung.server.dto.ErrorReportDTO;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JSONConverter<T>
{
    /**
     * Serializes an object to its JSON string representation.
     *
     * @param obj The object to serialize.
     * @return The JSON string representation of the object.
     * @throws RuntimeException if JSON serialization fails.
     */
    public static String toJson(Object obj)
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * Saves an entity as a JSON file with a unique name.
     *
     * @param fileName The base name for the file. A UUID will be appended.
     * @param entity   The entity to save.
     * @return The created File object.
     * @throws IOException if an error occurs during file writing.
     */
    public File saveToJSON(String fileName, T entity) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        File jsonFile = new File(fileName + UUID.randomUUID() + ".json");
        objectMapper.writeValue(jsonFile, entity);

        return jsonFile;
    }

    /**
     * Loads an entity from a JSON file.
     *
     * @param jsonFile The JSON file to read from.
     * @param tClass   The class of the entity to be loaded.
     * @return The loaded entity object.
     * @throws IOException if an error occurs during file reading or parsing.
     */
    public T loadFromJSON(File jsonFile, Class<T> tClass) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.readValue(jsonFile, tClass);
    }
}