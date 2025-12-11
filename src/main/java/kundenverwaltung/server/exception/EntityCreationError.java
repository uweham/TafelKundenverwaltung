package kundenverwaltung.server.exception;

/**
 * Exception thrown when an error occurs during the creation of an entity on the server.
 */
public class EntityCreationError extends Exception
{
    private static final String ENTITY_CREATION_ERROR = "There was an error while creating a new entity! ";

    /**
     * Constructs a new EntityCreationError with a detail message.
     *
     * @param message A message providing more details on the error.
     */
    public EntityCreationError(String message)
    {
        super(ENTITY_CREATION_ERROR + message);
    }
}