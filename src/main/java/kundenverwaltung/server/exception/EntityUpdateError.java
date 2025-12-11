package kundenverwaltung.server.exception;

/**
 * Exception thrown when an error occurs during the update of an entity on the server.
 */
public class EntityUpdateError extends Exception
{
    private static final String ENTITY_UPDATE_ERROR = "There was an error while updating an entity! ";

    /**
     * Constructs a new EntityUpdateError with a detail message.
     *
     * @param message A message providing more details on the error.
     */
    public EntityUpdateError(String message)
    {
        super(ENTITY_UPDATE_ERROR + message);
    }
}