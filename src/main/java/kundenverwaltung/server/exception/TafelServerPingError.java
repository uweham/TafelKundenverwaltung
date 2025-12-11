package kundenverwaltung.server.exception;

/**
 * Exception thrown when a ping to the Tafel server fails.
 */
public class TafelServerPingError extends Exception
{
    private static final String TAFEL_SERVER_PING_ERROR = "There was an error while trying to ping the tafel-server! ";

    /**
     * Constructs a new TafelServerPingError with a detail message.
     *
     * @param message A message providing more details on the error.
     */
    public TafelServerPingError(String message)
    {
        super(TAFEL_SERVER_PING_ERROR + message);
    }
}