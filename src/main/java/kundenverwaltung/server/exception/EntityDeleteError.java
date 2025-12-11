package kundenverwaltung.server.exception;

/**
 * Exception thrown when an error occurs during the deletion of an entity on the server.
 */
public class EntityDeleteError extends Exception
{
    private static final String ENTITY_DELETE_ERROR = "There was an error while deleting an entity! ";

    /**
     * Constructs a new EntityDeleteError with a detail message.
     *
     * @param message A message providing more details on the error.
     */
    public EntityDeleteError(String message)
    {
        super(ENTITY_DELETE_ERROR + message);
    }
}