package kundenverwaltung.server.exception;

/**
 * Exception thrown when an authentication attempt fails due to incorrect credentials.
 */
public class AuthLoginError extends Exception
{
    private static final String AUTH_LOGIN_ERROR = "Auth failed because the given username or password is wrong!";

    /**
     * Constructs a new AuthLoginError with a predefined detail message.
     */
    public AuthLoginError()
    {
        super(AUTH_LOGIN_ERROR);
    }
}